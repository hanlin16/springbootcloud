package com.myfutech.demo.user.service.provider.service;

import com.myfutech.common.spring.jpa.base.BaseService;
import com.myfutech.common.util.vo.Page;
import com.myfutech.common.util.vo.Pageable;
import com.myfutech.demo.user.service.api.vo.request.employee.EmployeePageableVO;
import com.myfutech.demo.user.service.api.vo.response.employee.EmployeeVO;
import com.myfutech.demo.user.service.provider.model.Employee;

/**
 * 员工 service接口
 */
public interface EmployeeService extends BaseService<Employee> {


    Page<EmployeeVO> page(Pageable pageable);

    Page<EmployeeVO> pageByCondition(EmployeePageableVO employeePageableVO);

    void testAsync(String param);
}