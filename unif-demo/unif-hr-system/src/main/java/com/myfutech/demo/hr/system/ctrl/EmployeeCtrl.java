package com.myfutech.demo.hr.system.ctrl;

import com.myfutech.common.util.Responses;
import com.myfutech.common.util.vo.IdVO;
import com.myfutech.common.util.vo.Page;
import com.myfutech.common.util.vo.Pageable;
import com.myfutech.demo.user.service.api.remote.RemoteEmployeeService;
import com.myfutech.demo.user.service.api.vo.request.employee.EmployeeModifyVO;
import com.myfutech.demo.user.service.api.vo.request.employee.EmployeePageableVO;
import com.myfutech.demo.user.service.api.vo.response.employee.EmployeeVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 员工 相关接口
 */
@Api(tags = "员工相关接口", description="员工",
        consumes= MediaType.APPLICATION_JSON_UTF8_VALUE,
        produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
@RestController
@RequestMapping("/employee")
public class EmployeeCtrl {

    private static final Logger log = LoggerFactory.getLogger(EmployeeCtrl.class);

    @Resource
    private RemoteEmployeeService employeeService;

    @ApiOperation(value = "查询员工分页列表")
    @PostMapping("/page")
    public Responses<Page<EmployeeVO>> page( @RequestBody Pageable pageable){
        Page<EmployeeVO> list = employeeService.page(pageable);
        return Responses.success(list);
    }

    @ApiOperation(value = "查询员工分页列表")
    @PostMapping("/pageByCondition")
    public Responses<Page<EmployeeVO>> pageByCondition( @RequestBody EmployeePageableVO employeePageableVO){
        Page<EmployeeVO> list = employeeService.pageByCondition(employeePageableVO);
        return Responses.success(list);
    }

    @ApiOperation(value = "根据id查询员工") 
    @PostMapping("/get")
    public Responses<EmployeeVO> getOne(@Validated @RequestBody IdVO idVO){
        EmployeeVO data = employeeService.getOne(idVO);
        return Responses.success(data);
    }
    @ApiOperation(value = "新增员工") 
    @PostMapping("/add")
    public Responses add(@Validated(EmployeeModifyVO.Create.class) @RequestBody EmployeeModifyVO employeeVO){
        employeeService.add(employeeVO);
        return Responses.success();
    }
    @ApiOperation(value = "更新员工") 
    @PostMapping("/update")
    public Responses update(@Validated(EmployeeModifyVO.Update.class) @RequestBody EmployeeModifyVO employeeVO){
        employeeService.update(employeeVO);
        return Responses.success();
    }
    @ApiOperation(value = "删除员工") 
    @PostMapping("/delete")
    public Responses delete(@Validated @RequestBody IdVO idVO){
        employeeService.delete(idVO);
        return Responses.success();
    }
}