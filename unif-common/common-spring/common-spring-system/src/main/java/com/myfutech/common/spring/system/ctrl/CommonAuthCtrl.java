package com.myfutech.common.spring.system.ctrl;

import com.myfutech.common.spring.system.util.AuthUserUtils;
import com.myfutech.common.util.Responses;
import com.myfutech.common.util.constant.HttpHeader;
import com.myfutech.common.util.tree.TreeVO;
import com.myfutech.common.util.vo.CodeVO;
import com.myfutech.common.util.vo.IdVO;
import com.myfutech.common.util.vo.auth.CommonUserInfo;
import com.myfutech.common.util.vo.auth.SectionPositionInfoVO;
import com.myfutech.common.util.vo.auth.UserInfo;
import com.myfutech.user.service.api.enums.section.SectionTypeV2;
import com.myfutech.user.service.api.remote.RemoteSubsystemService;
import com.myfutech.user.service.api.remote.RemoteSysResourceService;
import com.myfutech.user.service.api.vo.response.resource.TreeResourceVO;
import com.myfutech.user.service.api.vo.response.subsystem.SubsystemVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Set;

/**
 * 基本权限体系
 *
 * 1、获取页面导航菜单
 * 2、获取每个导航菜单下的可见资源code
 */
@Api(tags = "菜单按钮权限相关接口", description = "菜单按钮权限",
        consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@RestController
@RequestMapping("/common")
public class CommonAuthCtrl {

    @Resource
    private RemoteSysResourceService sysResourceService;

    @Resource
    private RemoteSubsystemService remoteSubsystemService;

    @ApiOperation(value = "加载登陆用户系统菜单")
    @PostMapping("/loadMenuList")
    public Responses<Collection<TreeVO<String>>> loadMenuList(@RequestHeader(HttpHeader.SUBSYSTEM_KEY) String systemCode) {
        SubsystemVO subsystemVO = remoteSubsystemService.getBySystemCode(CodeVO.newCodeVO(systemCode));
        if (subsystemVO == null){
            return Responses.error("没有找到对应系统信息");
        }
        Collection<TreeVO<String>> collection = sysResourceService.menuTreeBySystemId(IdVO.newIdVO(subsystemVO.getId()));
        return Responses.success(collection);
    }

    @ApiOperation(value = "查询当前页面登陆用户权限")
    @PostMapping("/loadPageResourceCode")
    public Responses<Set<String>> loadPageResourceCode(@RequestBody IdVO vo) {
        Set<String> set = sysResourceService.findResourceCodeByParentId(vo);
        return Responses.success(set);
    }

    @ApiOperation(value = "加载登陆用户系统下所有的资源权限")
    @PostMapping("/loadAllResource")
    public Responses<Collection<TreeVO<TreeResourceVO>>> loadAllResource(@RequestHeader(HttpHeader.SUBSYSTEM_KEY) String systemCode) {
        SubsystemVO subsystemVO = remoteSubsystemService.getBySystemCode(CodeVO.newCodeVO(systemCode));
        if (subsystemVO == null){
            return Responses.error("没有找到对应系统信息");
        }
        Collection<TreeVO<TreeResourceVO>> collection = sysResourceService.resourceTreeBySystemId(IdVO.newIdVO(subsystemVO.getId()));
        return Responses.success(collection);
    }

    @ApiOperation(value = "查询登陆用户信息")
    @PostMapping("/loadLoginUserInfo")
    public Responses<CommonUserInfo> loadLoginUserInfo() {
        CommonUserInfo commonUserInfo = new CommonUserInfo();
        UserInfo userInfo = AuthUserUtils.getUserInfo();
        BeanUtils.copyProperties(userInfo, commonUserInfo);
        commonUserInfo.setIsTeamManager(false);
        if (CollectionUtils.isNotEmpty(userInfo.getSectionPositionInfoList())){
            for (SectionPositionInfoVO vo : userInfo.getSectionPositionInfoList()) {
                if (1 == vo.getIsLeader() && SectionTypeV2.TEAM.getCode().equals(vo.getSectionTypeCode())){
                    commonUserInfo.setIsTeamManager(true);
                    break;
                }
            }
        }
        return Responses.success(commonUserInfo);
    }
}
