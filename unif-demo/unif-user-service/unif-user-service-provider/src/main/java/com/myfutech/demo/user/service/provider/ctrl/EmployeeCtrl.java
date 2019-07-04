package com.myfutech.demo.user.service.provider.ctrl;

import com.myfutech.common.util.vo.IdVO;
import com.myfutech.common.util.vo.Page;
import com.myfutech.common.util.vo.Pageable;
import com.myfutech.demo.user.service.api.vo.request.employee.EmployeeModifyVO;
import com.myfutech.demo.user.service.api.vo.request.employee.EmployeePageableVO;
import com.myfutech.demo.user.service.api.vo.response.employee.EmployeeVO;
import com.myfutech.demo.user.service.provider.model.Employee;
import com.myfutech.demo.user.service.provider.service.EmployeeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 员工 相关接口
 */
@Api(tags = "员工相关接口", description="员工",
        consumes= MediaType.APPLICATION_JSON_UTF8_VALUE,
        produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
@EnableAsync
@RestController
@RequestMapping("/employee")
public class EmployeeCtrl {

    private static final Logger log = LoggerFactory.getLogger(EmployeeCtrl.class);

    @Resource(name="employeeService") 
    private EmployeeService employeeService;

    @ApiOperation(value = "查询员工分页列表")
    @PostMapping("/page")
    public Page<EmployeeVO> page( @RequestBody Pageable pageable){
        Page<EmployeeVO> list = employeeService.page(pageable);
        return list;
    }

    @ApiOperation(value = "查询员工分页列表")
    @PostMapping("/pageByCondition")
    public Page<EmployeeVO> pageByCondition( @RequestBody EmployeePageableVO employeePageableVO){
        Page<EmployeeVO> list = employeeService.pageByCondition(employeePageableVO);
        return list;
    }

    @ApiOperation(value = "根据id查询员工") 
    @PostMapping("/get")
    public EmployeeVO getOne(@Validated @RequestBody IdVO vo){
        Employee data = employeeService.getOne(vo.getId());
        if (data == null){
            return null;
        }
        EmployeeVO employeeVO = new EmployeeVO();
        BeanUtils.copyProperties(data, employeeVO);
        return employeeVO;
    }
    @ApiOperation(value = "新增员工") 
    @PostMapping("/add")
    public Long add(@Validated(EmployeeModifyVO.Create.class) @RequestBody EmployeeModifyVO vo){
        Employee employee = new Employee();
        BeanUtils.copyProperties(vo, employee);
        employee = employeeService.save(employee);
        return employee.getId();
    }
    @ApiOperation(value = "更新员工") 
    @PostMapping("/update")
    public void update(@Validated(EmployeeModifyVO.Update.class) @RequestBody EmployeeModifyVO vo){
        Employee employee = new Employee();
        BeanUtils.copyProperties(vo, employee);
        employeeService.saveNotNull(employee);
    }
    @ApiOperation(value = "删除员工") 
    @PostMapping("/delete")
    public void delete(@Validated @RequestBody IdVO vo){
        employeeService.deleteById(vo.getId());
    }

    @ApiOperation(value = "异步调用")
    @GetMapping("/testAsync")
    public void testAsync(){
        System.out.println("主线程.......");
        System.out.println("主线程:"+Thread.currentThread().getName());
        employeeService.testAsync("测试");
    }
}