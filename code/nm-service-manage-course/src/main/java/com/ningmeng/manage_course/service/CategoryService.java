package com.ningmeng.manage_course.service;

import com.ningmeng.framework.domain.course.CourseBase;
import com.ningmeng.framework.domain.course.CourseMarket;
import com.ningmeng.framework.domain.course.ext.CategoryNode;
import com.ningmeng.framework.exception.ExceptionCast;
import com.ningmeng.framework.model.response.CommonCode;
import com.ningmeng.framework.model.response.ResponseResult;
import com.ningmeng.manage_course.dao.CategoryMapper;
import com.ningmeng.manage_course.dao.CourseBaseRepository;
import com.ningmeng.manage_course.dao.CourseMarketRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Optional;

@Service
public class CategoryService {

    @Resource
    private CategoryMapper categoryMapper;

    @Resource
    private CourseBaseRepository courseBaseRepository;

    @Resource
    private CourseMarketRepository courseMarketRepository;


    @Transactional
    public CategoryNode findList() {
        return categoryMapper.selectList();
    }

    public CourseBase getCourseBaseById(String courseId) {
        Optional<CourseBase> optional = courseBaseRepository.findById(courseId);
        if(optional.isPresent()){
            return optional.get();
        }
        return null;
    }

    @Transactional
    public ResponseResult updateCourseBase(String id, CourseBase courseBase) {
        CourseBase one = this.getCourseBaseById(id);
        if(one == null){
            ExceptionCast.cast(CommonCode.FAIL);
        }
        //修改课程信息
        one.setName(courseBase.getName());
        one.setMt(courseBase.getMt());
        one.setSt(courseBase.getSt());
        one.setGrade(courseBase.getGrade());
        one.setStudymodel(courseBase.getStudymodel());
        one.setUsers(courseBase.getUsers());
        one.setDescription(one.getDescription());
        CourseBase save = courseBaseRepository.saveAndFlush(one);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    public CourseMarket getCourseMarketById(String courseId) {
        Optional<CourseMarket> optional = courseMarketRepository.findById(courseId);
        if(optional.isPresent()){
            return optional.get();
        }
        return null;
    }

    @Transactional
    public ResponseResult updateCourseMarket(String id, CourseMarket courseMarket) {
        CourseMarket market = this.getCourseMarketById(id);
        if(market != null){
            market.setCharge(courseMarket.getCharge());
            market.setStartTime(courseMarket.getStartTime());
            market.setEndTime(courseMarket.getEndTime());
            market.setPrice(courseMarket.getPrice());
            market.setQq(courseMarket.getQq());
            market.setValid(courseMarket.getValid());
            courseMarketRepository.save(market);
        }else{
            //添加课程营销信息
            market = new CourseMarket();
            BeanUtils.copyProperties(courseMarket,market);
            //设置课程id
            market.setId(id);
            courseMarketRepository.save(market);
        }
        return new ResponseResult(CommonCode.SUCCESS);
    }
}
