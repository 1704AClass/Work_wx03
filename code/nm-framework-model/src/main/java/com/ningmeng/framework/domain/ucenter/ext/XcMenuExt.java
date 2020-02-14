package com.ningmeng.framework.domain.ucenter.ext;

import com.ningmeng.framework.domain.course.ext.CategoryNode;
import com.ningmeng.framework.domain.ucenter.XcMenu;
import lombok.Data;
import lombok.ToString;

import java.util.List;


@Data
@ToString
public class XcMenuExt extends XcMenu {

    List<CategoryNode> children;
}
