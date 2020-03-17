package com.ningmeng.framework.domain.course;

import com.ningmeng.framework.domain.course.ext.TeachplanNode;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
@Data
@ToString
@NoArgsConstructor
public class CourseView implements Serializable {

    CourseBase courseBase; //基础信息
    CourseMarket courseMarket; //课程营销
    CoursePic coursePic; //课程图片
    TeachplanNode teachplanNode; //数学计划


}
