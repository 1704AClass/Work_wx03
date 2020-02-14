package com.ningmeng.framework.domain.ucenter.ext;

import com.ningmeng.framework.domain.ucenter.XcMenu;
import com.ningmeng.framework.domain.ucenter.XcUser;
import lombok.Data;
import lombok.ToString;

import java.util.List;


@Data
@ToString
public class XcUserExt extends XcUser {

    //权限信息
    private List<XcMenu> permissions;

    //企业信息
    private String companyId;
}
