package com.ningmeng.ucenter.dao;

import com.ningmeng.framework.domain.ucenter.NmMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface NmMenuMapper {

    public List<NmMenu> selectPermissionByUserId(@Param("userid") String userid);

}
