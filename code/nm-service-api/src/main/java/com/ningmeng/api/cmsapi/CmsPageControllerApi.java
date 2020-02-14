package com.ningmeng.api.cmsapi;

import com.ningmeng.framework.domain.cms.CmsPage;
import com.ningmeng.framework.domain.cms.request.QueryPageRequest;
import com.ningmeng.framework.domain.cms.response.CmsPageResult;
import com.ningmeng.framework.model.response.QueryResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Api(value = "cms管理接口",description = "cms的管理接口，负责页面的增删改查")
public interface CmsPageControllerApi {
    @ApiOperation("分页查询列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name="page",value = "起始页",defaultValue = "1",required = true,paramType = "path",dataType = "int"),
            @ApiImplicitParam(name="size",value = "页数",defaultValue = "0",required = true,paramType = "path",dataType = "int"),
    })
    public QueryResponseResult findList(int page, int size, QueryPageRequest queryPageRequest);

    @ApiOperation("添加方法")
    public CmsPageResult add(CmsPage cmsPage);
    @ApiOperation("查询")
    public CmsPage findOne(String id);
    @ApiOperation("修改方法")
    public CmsPageResult update(CmsPage cmsPage);
    @ApiOperation("删除方法")
    public CmsPageResult delete(String id);
}
