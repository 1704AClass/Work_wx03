package com.ningmeng.manage_cms.controller;

import com.netflix.discovery.converters.Auto;
import com.ningmeng.api.cmsapi.CmsPageControllerApi;
import com.ningmeng.framework.domain.cms.CmsPage;
import com.ningmeng.framework.domain.cms.request.QueryPageRequest;
import com.ningmeng.framework.domain.cms.response.CmsPageResult;
import com.ningmeng.framework.domain.cms.response.CmsPostPageResult;
import com.ningmeng.framework.domain.course.response.CoursePublishResult;
import com.ningmeng.framework.model.response.QueryResponseResult;
import com.ningmeng.framework.model.response.Response;
import com.ningmeng.framework.model.response.ResponseResult;
import com.ningmeng.manage_cms.service.CmsPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/cms")
public class CmsPageController implements CmsPageControllerApi {
    @Autowired
    private CmsPageService service;
    @Autowired
    private HttpServletResponse response;
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

    @Override
    @PostMapping("/postPageQuick")
    public CmsPostPageResult postPageQuick(@RequestBody CmsPage cmsPage) {
        return service.postPageQuick(cmsPage);
    }

    @GetMapping("/preview/{pageId}")
    public void preview(@PathVariable("pageId") String pageId){
       try{
           String pageHtml = service.preview(pageId);
           ServletOutputStream outputStream = response.getOutputStream();
           response.setHeader("Content-type","text/html;charset=utf-8");
           outputStream.write(pageHtml.getBytes("utf-8"));
       }catch (Exception e){
             e.printStackTrace();
       }

    }
}
