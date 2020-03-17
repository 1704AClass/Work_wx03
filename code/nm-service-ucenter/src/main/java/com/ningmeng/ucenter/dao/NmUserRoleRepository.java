package com.ningmeng.ucenter.dao;

import com.ningmeng.framework.domain.ucenter.NmUserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NmUserRoleRepository extends JpaRepository<NmUserRole, String> {
    List<NmUserRole> findXcUserRoleByUserId(String userId);
}
