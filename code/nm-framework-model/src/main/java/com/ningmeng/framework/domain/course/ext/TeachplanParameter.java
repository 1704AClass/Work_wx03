package com.ningmeng.framework.domain.course.ext;

import com.ningmeng.framework.domain.course.Teachplan;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class TeachplanParameter extends Teachplan {

    //二级分类ids
    List<String> bIds;
    //三级分类ids
    List<String> cIds;

}
