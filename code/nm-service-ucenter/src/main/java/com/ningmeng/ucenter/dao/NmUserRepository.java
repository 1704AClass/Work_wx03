package com.ningmeng.ucenter.dao;

import com.ningmeng.framework.domain.ucenter.NmUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NmUserRepository extends JpaRepository<NmUser,String> {

    NmUser findNmUserByUsername(String username);

}
