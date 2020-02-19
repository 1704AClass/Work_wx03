package com.ningmeng.manage_course.service;

import com.ningmeng.framework.domain.course.CourseBase;
import com.ningmeng.framework.domain.course.Teachplan;
import com.ningmeng.framework.domain.course.ext.TeachplanNode;
import com.ningmeng.framework.exception.CustomExceptionCast;
import com.ningmeng.framework.model.response.CommonCode;
import com.ningmeng.framework.model.response.ResponseResult;
import com.ningmeng.manage_course.dao.CourseBaseRepository;
import com.ningmeng.manage_course.dao.TeachplanMapper;
import com.ningmeng.manage_course.dao.TeachplanRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseService {
    @Autowired
    private TeachplanMapper teachplanMapper;
    @Autowired
    private TeachplanRepository repository;
    @Autowired
    private CourseBaseRepository courseBaseRepository;

    public TeachplanNode findTeachplanList(String courseId){
        return teachplanMapper.findTeachplanList(courseId);
    }

    public ResponseResult addTeachplan(Teachplan teachplan) {

        //先去判断课程id和课程计划名称是否为空  如果为空直接报错
        if(teachplan==null || StringUtils.isEmpty(teachplan.getCourseid()) || StringUtils.isEmpty(teachplan.getPname())){
            CustomExceptionCast.cast(CommonCode.FAIL);
        }
        //不为空的话 获取两者
        String courseid = teachplan.getCourseid();
        String parentid = teachplan.getParentid();
        //判断是否选择父节点，如果没有选，代表根节点 创建节点
        if(StringUtils.isEmpty(parentid)){
            parentid=getTeachplanRoot(courseid);
        }
        //如果有，取出父节点，对象的信息
        Optional<Teachplan> teachplanOptiona = repository.findById(parentid);
        if(!teachplanOptiona.isPresent()){
            CustomExceptionCast.cast(CommonCode.FAIL);
        }
        Teachplan teachplan1 = teachplanOptiona.get();
        //取出级别 来决定我是第几级别
        String grade = teachplan1.getGrade();
        System.err.println("-------------------------"+grade);
        teachplan.setParentid(parentid);
        teachplan.setStatus("0");
        if(grade.equals("1")){
            teachplan.setGrade("2");
        }else if(grade.equals("2")){
            teachplan.setGrade("3");
        }
        teachplan.setCourseid(teachplan1.getCourseid());
        repository.save(teachplan);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    private String getTeachplanRoot(String courseid) {
        //判断课程id
        Optional<CourseBase> optional = courseBaseRepository.findById(courseid);
        if(!optional.isPresent()){
            return null;
        }
        CourseBase courseBase = optional.get();
        //判断是否当前的这个课程 是否有相同的父节点信息，如果没有，就添加一个根节点（最大的一级），如果有父级，直接返回父级的id
        List<Teachplan> teachplanList = repository.findAllByAndCourseidAndAndParentid(courseid, "0");
        if(teachplanList==null || teachplanList.size()==0){
            Teachplan teachplanRoot = new Teachplan();
            teachplanRoot.setCourseid(courseid);
            teachplanRoot.setPname(courseBase.getName());
            teachplanRoot.setParentid("0");
            teachplanRoot.setGrade("1");
            teachplanRoot.setStatus("0");
            repository.save(teachplanRoot);
            return teachplanRoot.getId();
        }
        Teachplan teachplan = teachplanList.get(0);
        return teachplan.getId();
    }

}
