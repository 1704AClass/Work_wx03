package com.ningmeng.manage_course.dao;

import com.ningmeng.framework.domain.course.Teachplan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeachplanRepository extends JpaRepository<Teachplan,String > {
    public List<Teachplan> findAllByAndCourseidAndAndParentid(String courseId,String parentId);
}
