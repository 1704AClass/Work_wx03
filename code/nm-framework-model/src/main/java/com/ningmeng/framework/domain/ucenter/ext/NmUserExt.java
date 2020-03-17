package com.ningmeng.framework.domain.ucenter.ext;

import com.ningmeng.framework.domain.ucenter.NmMenu;
import com.ningmeng.framework.domain.ucenter.NmUser;
import lombok.Data;
import lombok.ToString;

import java.util.List;


@Data
@ToString
public class NmUserExt extends NmUser {

    //权限信息
    private List<NmMenu> permissions;

    //企业信息
    private String companyId;
}
