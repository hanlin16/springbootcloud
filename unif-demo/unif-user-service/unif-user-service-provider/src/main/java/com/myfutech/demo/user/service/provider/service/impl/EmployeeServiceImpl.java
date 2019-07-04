package com.myfutech.demo.user.service.provider.service.impl;

import com.myfutech.common.spring.jpa.base.BaseDao;
import com.myfutech.common.spring.jpa.base.impl.BaseServiceImpl;
import com.myfutech.common.util.vo.Page;
import com.myfutech.common.util.vo.Pageable;
import com.myfutech.demo.user.service.api.vo.request.employee.EmployeePageableVO;
import com.myfutech.demo.user.service.api.vo.response.employee.EmployeeVO;
import com.myfutech.demo.user.service.provider.dao.EmployeeDao;
import com.myfutech.demo.user.service.provider.model.Employee;
import com.myfutech.demo.user.service.provider.service.EmployeeService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 员工 service接口实现类
 */
@Service("employeeService")
public class EmployeeServiceImpl extends BaseServiceImpl<Employee> implements EmployeeService {


    private static final Logger log = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    @Resource(name="employeeDao") 
    private EmployeeDao employeeDao;

    @Override
    protected BaseDao<Employee> getDao(){
        return employeeDao;
    }

    @Override
    public Page<EmployeeVO> page(Pageable pageable) {
        return employeeDao.nativeFindPage("select id,employeeCode,employeeName," +
                "phoneNumber,userImage,deleteStatus,creator,modifier,createTime,modifyTime,workEmail  from basic_employee", null, EmployeeVO.class, pageable);
    }

    @Override
    public Page<EmployeeVO> pageByCondition(EmployeePageableVO vo) {

        StringBuilder sb = new StringBuilder();
        Map<String, Object> map = new HashMap<>();

        sb.append("select id,employeeCode,employeeName,phoneNumber,userImage,deleteStatus,creator,modifier,createTime,modifyTime,workEmail from basic_employee where 1=1 ");

        if (StringUtils.isNotBlank(vo.getEmployeeName())){
            sb.append(" and employeeName like :employeeName ");
            map.put("employeeName", vo.getEmployeeName() + "%");
        }

        if (StringUtils.isNotBlank(vo.getPhoneNumber())){
            sb.append(" and phoneNumber like :phoneNumber ");
            map.put("phoneNumber", vo.getPhoneNumber() + "%");
        }
        return employeeDao.nativeFindPage(sb.toString(), map, EmployeeVO.class, vo);
    }

    @Override
    @Async
    public void testAsync(String param) {
        System.out.println("调用线程:"+Thread.currentThread().getName());
        System.out.println("测试线程_异步调用打印" + param);
    }
}