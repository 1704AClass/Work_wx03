package com.ningmeng.manage_cms.service;

import com.alibaba.fastjson.JSON;
import com.ningmeng.framework.domain.cms.CmsPage;
import com.ningmeng.framework.domain.cms.CmsSite;
import com.ningmeng.framework.domain.cms.request.QueryPageRequest;
import com.ningmeng.framework.domain.cms.response.CmsCode;
import com.ningmeng.framework.domain.cms.response.CmsPageResult;
import com.ningmeng.framework.domain.cms.response.CmsPostPageResult;
import com.ningmeng.framework.exception.CustomExceptionCast;
import com.ningmeng.framework.model.response.CommonCode;
import com.ningmeng.framework.model.response.QueryResponseResult;
import com.ningmeng.framework.model.response.QueryResult;
import com.ningmeng.framework.model.response.ResponseResult;
import com.ningmeng.manage_cms.config.RabbitmqConfig;
import com.ningmeng.manage_cms.dao.CmsSiteRepository;
import com.ningmeng.manage_cms.dao.cmsPageRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class CmsPageService {
    @Autowired
    private cmsPageRepository repository;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private CmsSiteRepository cmsSiteRepository;

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
        Map model=getModelByPageId(pageId);
        if(model==null){
            CustomExceptionCast.cast(CmsCode.CMS_GENERATEHTML_DATAISNULL);
        }
        String template=getTemplateByPageId(pageId);
        if(StringUtils.isEmpty(template)){
            CustomExceptionCast.cast(CmsCode.CMS_GENERATEHTML_TEMPLATEISNULL);
        }
        String html=generateHtml(template,model);
        return html;
    }

    private String generateHtml(String template, Map model) {
        return "<html></html>";
    }

    private String getTemplateByPageId(String pageId) {
        Optional<CmsPage> byId = repository.findById(pageId);
        if(byId.isPresent()){
            CmsPage cmsPage = byId.get();
            String templateId = cmsPage.getTemplateId();
            return templateId;
        }
        return null;
    }

    private Map getModelByPageId(String pageId) {
        CmsPage cmsPage = repository.findById(pageId).get();
        String dataUrl = cmsPage.getDataUrl();
        ResponseEntity<Map> forEntity = restTemplate.getForEntity(dataUrl, Map.class);
        Map body = forEntity.getBody();
        return body;
    }

    public CmsPostPageResult postPageQuick(CmsPage cmsPage) {
        if(cmsPage==null){
            CustomExceptionCast.cast(CommonCode.FAIL);
        }
        ResponseResult responseResult=this.add(cmsPage);
        if(!responseResult.isSuccess()){
            return new CmsPostPageResult(CommonCode.FAIL,null);
        }
        CmsPage cmsPage1 = JSON.parseObject(responseResult.getMessage(), CmsPage.class);
        ResponseResult responseResult1 = this.postPage(cmsPage1.getPageId());
        if(!responseResult1.isSuccess()){
            return new CmsPostPageResult(CommonCode.FAIL,null);
        }
        //得到页面的url
        // 页面url=站点域名+站点webpath+页面webpath+页面名称
        // 站点id
        //站点id
        String siteId = cmsPage1.getSiteId();
        //查询站点信息
        CmsSite cmsSite=findCmsSiteById(siteId);
        System.err.println("--------------------------"+cmsSite);
        //站点域名
        String siteDomain = cmsSite.getSiteDomain();
        //站点web路劲
        String siteWebPath = cmsSite.getSiteWebPath();
        //页面web路径
        String pageWebPath = cmsPage1.getPageWebPath();
        //页面名称
        String pageName = cmsPage1.getPageName();
        //页面的web访问地址
        String pageUrl=siteDomain+siteWebPath+pageWebPath+pageName;
        return new CmsPostPageResult(CommonCode.SUCCESS,pageUrl);
    }

    private CmsSite findCmsSiteById(String siteId) {
        Optional<CmsSite> cmsSiteOptional = cmsSiteRepository.findById(siteId);
        if(!cmsSiteOptional.isPresent()){
            CmsSite cmsSite = cmsSiteOptional.get();
            return cmsSite;
        }
        return null;
    }

}
