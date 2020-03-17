package com.ningmeng.framework.domain.cms.request;

import com.ningmeng.framework.model.request.RequestData;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QueryPageRequest extends RequestData {

    //站点
    @ApiModelProperty("站点id")
    private String siteId;

    //页面id
    @ApiModelProperty("页面id")
    private String pageId;

    //页面名称
    @ApiModelProperty("页面名称")
    private String pageName;

    //别名
    @ApiModelProperty("页面别名")
    private String pageAliase;

    //模板id
    @ApiModelProperty("模板id")
    private String templateId;



}
