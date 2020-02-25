package com.ningmeng.manage_course.dao;

import com.ningmeng.framework.domain.course.CoursePic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoursePicRepository extends JpaRepository<CoursePic,String> {

    public long deleteByCourseid(String courseId);
}
