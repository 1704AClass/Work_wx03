package com.ningmeng.manage_course.dao;

import com.ningmeng.framework.domain.course.CourseBase;
import org.apache.ibatis.annotations.Mapper;

/**
 * Created by Administrator.
 */
@Mapper
public interface CourseMapper {
   CourseBase findCourseBaseById(String id);
}
