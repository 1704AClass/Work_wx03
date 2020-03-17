package com.ningmeng.ucenter.controller;

import com.ningmeng.api.ucenter.UcenterControllerApi;
import com.ningmeng.framework.domain.ucenter.ext.NmUserExt;
import com.ningmeng.ucenter.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/ucenter")
public class UcenterController implements UcenterControllerApi {

    @Resource
    private UserService userService;

    @Override
    @GetMapping("/getuserext")
    public NmUserExt getUserext(@RequestParam("username") String username) {
        NmUserExt nmUser = userService.getUserext(username);
        return nmUser;
    }



}
