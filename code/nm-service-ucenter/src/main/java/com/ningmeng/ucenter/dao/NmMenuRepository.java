package com.ningmeng.ucenter.dao;

import com.ningmeng.framework.domain.ucenter.NmMenu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NmMenuRepository extends JpaRepository<NmMenu, String> {
}
