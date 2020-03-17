package com.ningmeng.manage_course.controller;

import com.ningmeng.api.course.SysDicthinaryControllerApi;
import com.ningmeng.framework.domain.system.SysDictionary;
import com.ningmeng.manage_course.service.SysdictionaryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/sys/dictionary")
public class SysDicthinaryController implements SysDicthinaryControllerApi {

    @Resource
    private SysdictionaryService sysdictionaryService;


    /**
     * 根据字典分类id 查询字典信息
     * @param type
     * @return
     */
    @Override
    @GetMapping("/get/{type}")
    public SysDictionary getByType(@PathVariable("type") String type) {
        return sysdictionaryService.getByType(type);
    }
}
