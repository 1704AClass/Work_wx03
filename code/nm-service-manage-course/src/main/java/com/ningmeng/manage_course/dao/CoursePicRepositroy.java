package com.ningmeng.manage_course.dao;

import com.ningmeng.framework.domain.course.CoursePic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoursePicRepositroy extends JpaRepository<CoursePic,String> {

    //删除成功返回1否则返回0
    long deleteByCourseid(String courseId);
}
