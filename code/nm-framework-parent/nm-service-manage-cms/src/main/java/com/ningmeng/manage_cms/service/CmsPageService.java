package com.ningmeng.manage_cms.service;

import com.alibaba.fastjson.JSON;
import com.ningmeng.framework.domain.cms.CmsPage;
import com.ningmeng.framework.domain.cms.request.QueryPageRequest;
import com.ningmeng.framework.domain.cms.response.CmsCode;
import com.ningmeng.framework.domain.cms.response.CmsPageResult;
import com.ningmeng.framework.exception.CustomExceptionCast;
import com.ningmeng.framework.model.response.*;
import com.ningmeng.manage_cms.config.RabbitmqConfig;
import com.ningmeng.manage_cms.dao.cmsPageRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class CmsPageService {
    @Autowired
    private cmsPageRepository repository;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public ResponseResult postPage(String pageId){
        boolean flag = createHtml();
        if(!flag){
            CustomExceptionCast.cast(CommonCode.FAIL);
        }
        //查询数据库
        CmsPage cmsPage = this.findOne(pageId);
        if(cmsPage == null){
            System.out.print("我是空的");
            CustomExceptionCast.cast(CommonCode.FAIL);
        }
        Map<String,String> msgMap = new HashMap<>();
        msgMap.put("pageId",pageId);
        //消息内容
        String msg = JSON.toJSONString(msgMap);
        //获取站点id作为routingKey
        String siteId = cmsPage.getSiteId();
        //发送jsonpageId
        rabbitTemplate.convertAndSend(RabbitmqConfig.EX_ROUTING_CMS_POSTPAGE,siteId,msg);
        return new ResponseResult(CommonCode.SUCCESS);
    }
    //创建静态页面
    public boolean createHtml(){
        System.out.println("静态化完成");
        return true;
    }
    public QueryResponseResult findList(int page, int size, QueryPageRequest queryPageRequest) {
        if (queryPageRequest == null) {
            queryPageRequest = new QueryPageRequest();
        }
        if (page < 0) {
            page = 1;
        }
        page = page - 1;
        PageRequest of = PageRequest.of(page, size);
        CmsPage cmsPage = new CmsPage();
        ExampleMatcher matching = ExampleMatcher.matching();
        if (queryPageRequest.getPageAliase() != null) {
            matching.withMatcher("pageAliase", ExampleMatcher.GenericPropertyMatchers.contains());
            cmsPage.setPageAliase(queryPageRequest.getPageAliase());
        }
        if (queryPageRequest.getSiteId() != null) {
            cmsPage.setSiteId(queryPageRequest.getSiteId());
        }
        if (queryPageRequest.getTemplateId() != null) {
            cmsPage.setTemplateId(queryPageRequest.getTemplateId());
        }
        Example<CmsPage> example = Example.of(cmsPage, matching);
        Page<CmsPage> listall = repository.findAll(example, of);
        QueryResult<CmsPage> queryResult = new QueryResult<CmsPage>();
        queryResult.setList(listall.getContent());
        queryResult.setTotal(listall.getTotalElements());
        QueryResponseResult queryResponseResult = new QueryResponseResult(CommonCode.SUCCESS, queryResult);
        return queryResponseResult;

    }

    public ResponseResult add(CmsPage cmsPage) {
        if(cmsPage==null){
            CustomExceptionCast.cast(CommonCode.FAIL);
        }
        CmsPage cms = repository.findBySiteIdAndPageNameAndPageWebPath(cmsPage.getSiteId(), cmsPage.getPageName(), cmsPage.getPageWebPath());
        if (cms!= null) {
         CustomExceptionCast.cast(CmsCode.CMS_ADDPAGE_EXISTSNAME);
        }
        cmsPage.setPageId(null);
        CmsPage cmsPage1 = repository.save(cmsPage);
        ResponseResult responseResult = new ResponseResult();
        responseResult.setCode(1000);
        responseResult.setSuccess(true);
        responseResult.setMessage(JSON.toJSONString(cmsPage1));
        return responseResult;
    }

    public CmsPage findOne(String id) {
        Optional<CmsPage> optionalS = repository.findById(id);
        if (optionalS.isPresent()) {
            return optionalS.get();
        }
        return null;
    }

    public CmsPageResult update(CmsPage cmsPage) {
        CmsPage cmsPage1 = this.findOne(cmsPage.getPageId());
        if (cmsPage != null) {
            repository.save(cmsPage);
            return new CmsPageResult(CommonCode.SUCCESS, cmsPage);
        }
        return new CmsPageResult(CommonCode.FAIL, null);
    }

    public CmsPageResult delete(String id) {
        CmsPage cmsPage = this.findOne(id);
        if (cmsPage!=null){
            repository.deleteById(id);
            return new CmsPageResult(CommonCode.SUCCESS, cmsPage);
        }
        return new CmsPageResult(CommonCode.FAIL, null);
    }

    public String preview(String pageId) {

        return null;
    }
}
