package com.ningmeng.learning.controller;

import com.ningmeng.api.learning.CourseLearningControllerApi;
import com.ningmeng.framework.domain.learning.GetMediaResult;
import com.ningmeng.learning.service.LearningService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/learning/course")
public class CourseLearningController implements CourseLearningControllerApi {


    @Resource
    private LearningService learningService;

    @Override
    @GetMapping("/getmedia/{courseId}/{teachplanId}")
    public GetMediaResult getmedia(@PathVariable("courseId") String courseId,@PathVariable("teachplanId") String teachplanId) {
        //获取课程学习地址
        return learningService.getMedia(courseId,teachplanId);
    }




}
