package com.ningmeng.manage_course.controller;

import com.ningmeng.api.courseapi.CourseControllerApi;
import com.ningmeng.framework.domain.course.CourseBase;
import com.ningmeng.framework.domain.course.CourseMarket;
import com.ningmeng.framework.domain.course.CoursePic;
import com.ningmeng.framework.domain.course.Teachplan;
import com.ningmeng.framework.domain.course.ext.CategoryNode;
import com.ningmeng.framework.domain.course.response.CoursePublishResult;
import com.ningmeng.framework.domain.course.response.CourseView;
import com.ningmeng.framework.domain.course.ext.TeachplanNode;
import com.ningmeng.framework.domain.course.response.AddCourseResult;
import com.ningmeng.framework.domain.system.SysDictionary;
import com.ningmeng.framework.model.response.CommonCode;
import com.ningmeng.framework.model.response.QueryResponseResult;
import com.ningmeng.framework.model.response.ResponseResult;
import com.ningmeng.manage_course.service.CourseService;
import com.ningmeng.manage_course.service.SysdictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/course")
public class CourseController implements CourseControllerApi {

    @Autowired
    private CourseService service;
    @Autowired
    private SysdictionaryService sysdictionaryService;

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

    @Override
    @GetMapping("/findCourseList/{pageNo}/{pageSize}")
    public QueryResponseResult findCourseList(@PathVariable("pageNo") int pageNo,@PathVariable("pageSize") int pageSize, String companyId) {
        return service.findCourseListPage(pageNo,pageSize,companyId);
    }
    @Override
    @GetMapping("/findList")
    public CategoryNode findList() {
        return service.findList();
    }

    @Override
    @GetMapping("/getByType/{dType}")
    public SysDictionary getByType(@PathVariable("dType") String dType) {
        return sysdictionaryService.findDictionaryByType(dType);
    }

    @Override
    @PostMapping("/coursebase/addCourseBase")
    public AddCourseResult addCourseBase(@RequestBody CourseBase courseBase) {
        return service.addCourseBase(courseBase);
    }

    @Override
    @GetMapping("/coursebase/get/{courseId}")
    public CourseBase getCourseBaseById(@PathVariable("courseId") String courseId) throws RuntimeException {
        return service.getCoursebaseById(courseId);
    }

    @Override
    @PutMapping("/coursebase/update/{id}")
    public ResponseResult updateCourseBase(@PathVariable("id") String id, CourseBase courseBase) {
        return service.updateCoursebase(id,courseBase);
    }

    @Override
    @GetMapping("/coursemarket/getCourseMarketById/{courseId}")
    public CourseMarket getCourseMarketById(@PathVariable("courseId") String courseId) {
        return service.getCourseMarketById(courseId);
    }


    @Override
    @PostMapping("/coursemarket/update/{id}")
    public ResponseResult updateCourseMarket(@PathVariable("id") String id, @RequestBody CourseMarket courseMarket) {
      try {
          service.updateCourseMarket(id, courseMarket);
          return new ResponseResult(CommonCode.SUCCESS);
      }catch (Exception e){
          return new ResponseResult(CommonCode.FAIL);
      }
        }

    @Override
    @PostMapping("/addCoursePic")
    public ResponseResult addCoursePic(@RequestParam(value = "courseId",required = true) String courseId,@RequestParam(value = "pic",required = true) String pic) {
        return service.saveCoursePic(courseId,pic);
    }

    @Override
    @GetMapping("/findCoursePic/{courseId}")
    public CoursePic findCoursePic(@PathVariable("courseId") String courseId) {
        return service.findCoursePic(courseId);
    }

    @Override
    @DeleteMapping("/deleteCoursePic/{courseId}")
    public ResponseResult deleteCoursePic(@PathVariable("courseId") String courseId) {
        return service.deleteCoursePic(courseId);
    }

    @Override
    @GetMapping("/getcourseview/{id}")
    public CourseView getcourseview(@PathVariable("id") String id) {
        return service.getcourseview(id);
    }

    @Override
    @PostMapping("/preview/{id}")
    public CoursePublishResult preview(@PathVariable("id") String id) {
        return service.preview(id);
    }

}
