package com.myfutech.common.spring.jpa.base.impl;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.internal.util.ReflectHelper;
import org.hibernate.property.access.internal.PropertyAccessStrategyBasicImpl;
import org.hibernate.property.access.internal.PropertyAccessStrategyChainedImpl;
import org.hibernate.property.access.internal.PropertyAccessStrategyFieldImpl;
import org.hibernate.property.access.internal.PropertyAccessStrategyMapImpl;
import org.hibernate.property.access.spi.Setter;
import org.hibernate.transform.AliasedTupleSubsetResultTransformer;

/**
 *  @see  org.hibernate.transform.AliasToBeanResultTransformer
 * @author max
 */
public class AliasToBeanResultTransformer extends AliasedTupleSubsetResultTransformer {

	// IMPL NOTE : due to the delayed population of setters (setters cached
	// 		for performance), we really cannot properly define equality for
	// 		this transformer

	private final Class resultClass;
	private volatile boolean isInitialized;
	private String[] aliases;
	private Setter[] setters;
	private Map<String, Method> changeValueTypeMap;

	public AliasToBeanResultTransformer(Class resultClass) {
		if ( resultClass == null ) {
			throw new IllegalArgumentException( "resultClass cannot be null" );
		}
		isInitialized = false;
		this.resultClass = resultClass;
	}

	@Override
	public boolean isTransformedValueATupleElement(String[] aliases, int tupleLength) {
		return false;
	}

	@Override
	public Object transformTuple(Object[] tuple, String[] aliases) {
		Object result;
		try {
			if ( ! isInitialized ) {
				initialize( aliases );
			}
			else {
				check( aliases );
			}
			
			result = resultClass.newInstance();

			for ( int i = 0; i < aliases.length; i++ ) {
				if ( setters[i] != null ) {
					//值不为空时
					if (tuple[i] != null){

						Method method;
						if (changeValueTypeMap.containsKey(aliases[i])){
							method = changeValueTypeMap.get(aliases[i]);
						}else {
							method = findNeedChangeTypeMethod(tuple, aliases[i], i);
							changeValueTypeMap.put(aliases[i], method);
						}

						if (method != null){
							tuple[i] = method.invoke(null, tuple[i].toString());
						}
					}
					setters[i].set( result, tuple[i], null );
				}
			}
		} catch ( InstantiationException |IllegalAccessException | NoSuchMethodException |InvocationTargetException e ) {
			throw new HibernateException( "Could not instantiate resultClass: " + resultClass.getName() );
		}
		return result;
	}

	private Method findNeedChangeTypeMethod(Object[] tuple, String alias, int i) throws NoSuchMethodException {
		Method method = null;
		final Field field = ReflectHelper.findField( resultClass, alias);
		//当值的类型不是属性类型或其子类时
		if (!field.getType().isInstance(tuple[i])){
			//如果值类型和属性类型都是数字 则将值类型转为属性类型
			if (Number.class.isAssignableFrom(tuple[i].getClass()) && Number.class.isAssignableFrom(field.getType())){
				method = field.getType().getDeclaredMethod("valueOf", String.class);
			}
		}
		return method;
	}

	private synchronized void initialize(String[] aliases) {
		PropertyAccessStrategyChainedImpl propertyAccessStrategy = new PropertyAccessStrategyChainedImpl(
				PropertyAccessStrategyBasicImpl.INSTANCE,
				PropertyAccessStrategyFieldImpl.INSTANCE,
				PropertyAccessStrategyMapImpl.INSTANCE
		);
		this.aliases = new String[ aliases.length ];
		setters = new Setter[ aliases.length ];
		for ( int i = 0; i < aliases.length; i++ ) {
			String alias = aliases[ i ];
			if ( alias != null ) {
				this.aliases[ i ] = alias;
				setters[ i ] = propertyAccessStrategy.buildPropertyAccess( resultClass, alias ).getSetter();
			}
		}
		changeValueTypeMap = new HashMap<>(aliases.length);
		isInitialized = true;
	}

	private void check(String[] aliases) {
		if ( ! Arrays.equals( aliases, this.aliases ) ) {
			throw new IllegalStateException(
					"aliases are different from what is cached; aliases=" + Arrays.asList( aliases ) +
							" cached=" + Arrays.asList( this.aliases ) );
		}
	}

	@Override
	public boolean equals(Object o) {
		if ( this == o ) {
			return true;
		}
		if ( o == null || getClass() != o.getClass() ) {
			return false;
		}

		AliasToBeanResultTransformer that = ( AliasToBeanResultTransformer ) o;

		if ( ! resultClass.equals( that.resultClass ) ) {
			return false;
		}
		if ( ! Arrays.equals( aliases, that.aliases ) ) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		int result = resultClass.hashCode();
		result = 31 * result + ( aliases != null ? Arrays.hashCode( aliases ) : 0 );
		return result;
	}
}
