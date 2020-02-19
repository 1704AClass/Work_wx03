package com.ningmeng.manage_course.controller;

import com.ningmeng.api.courseapi.CourseControllerApi;
import com.ningmeng.framework.domain.course.Teachplan;
import com.ningmeng.framework.domain.course.ext.TeachplanNode;
import com.ningmeng.framework.model.response.ResponseResult;
import com.ningmeng.manage_course.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/course")
public class CourseController implements CourseControllerApi {

    @Autowired
    private CourseService service;

    @Override
    @GetMapping("/findTeachplanList/{courseId}")
    public TeachplanNode findTeachplanList(@PathVariable("courseId") String courseId) {

        return service.findTeachplanList(courseId);
    }

    @PostMapping("/addTeachplan")
    @Override
    public ResponseResult addTeachplan(@RequestBody Teachplan teachplan) {
        return service.addTeachplan(teachplan);
    }


}
