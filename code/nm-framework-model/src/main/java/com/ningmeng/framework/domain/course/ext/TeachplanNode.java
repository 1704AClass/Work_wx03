package com.ningmeng.framework.domain.course.ext;

import com.ningmeng.framework.domain.course.Teachplan;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class TeachplanNode extends Teachplan {

    List<TeachplanNode> children;

}
