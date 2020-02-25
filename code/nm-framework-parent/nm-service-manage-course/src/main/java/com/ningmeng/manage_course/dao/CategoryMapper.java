package com.ningmeng.manage_course.dao;

import com.ningmeng.framework.domain.course.ext.CategoryNode;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CategoryMapper {
    //查询分类
    public CategoryNode selectList();
}
