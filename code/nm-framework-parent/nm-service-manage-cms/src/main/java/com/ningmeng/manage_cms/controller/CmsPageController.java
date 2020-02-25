package com.ningmeng.manage_cms.controller;

import com.ningmeng.api.cmsapi.CmsPageControllerApi;
import com.ningmeng.framework.domain.cms.CmsPage;
import com.ningmeng.framework.domain.cms.request.QueryPageRequest;
import com.ningmeng.framework.domain.cms.response.CmsPageResult;
import com.ningmeng.framework.model.response.QueryResponseResult;
import com.ningmeng.framework.model.response.ResponseResult;
import com.ningmeng.manage_cms.service.CmsPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cms")
public class CmsPageController implements CmsPageControllerApi {
    @Autowired
    private CmsPageService service;
    @Override
    @GetMapping("/list/{page}/{size}")
    public QueryResponseResult findList(@PathVariable("page") int page, @PathVariable("size") int size,QueryPageRequest queryPageRequest) {
       return service.findList(page,size,queryPageRequest);
    }
    @Override
    @PostMapping("/add")
    public ResponseResult add(@RequestBody CmsPage cmsPage) {
       return service.add(cmsPage);
    }

    @Override
    @GetMapping("/findOne/{id}")
    public CmsPage findOne(@PathVariable("id") String id) {
        return service.findOne(id);
    }

    @Override
    @PutMapping("/update")
    public CmsPageResult update(@RequestBody CmsPage cmsPage) {
        return service.update(cmsPage);
    }

    @Override
    @DeleteMapping("/delete/{id}")
    public CmsPageResult delete(@PathVariable("id") String id) {
        return service.delete(id);
    }

    @Override
    @PostMapping("/postPage/{pageId}")
    public ResponseResult post(String pageId) {
        return service.postPage(pageId);
    }
    @GetMapping("/preview/{pageId}")
    public String preview(@PathVariable("pageId") String pageId){
        return service.preview(pageId);
    }
}
