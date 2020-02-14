package com.ningmeng.manage_cms.service;

import com.ningmeng.framework.domain.cms.CmsPage;
import com.ningmeng.framework.domain.cms.request.QueryPageRequest;
import com.ningmeng.framework.domain.cms.response.CmsCode;
import com.ningmeng.framework.domain.cms.response.CmsPageResult;
import com.ningmeng.framework.exception.CustomExceptionCast;
import com.ningmeng.framework.model.response.CommonCode;
import com.ningmeng.framework.model.response.QueryResponseResult;
import com.ningmeng.framework.model.response.QueryResult;
import com.ningmeng.manage_cms.dao.cmsPageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CmsPageService {
    @Autowired
    private cmsPageRepository repository;

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

    public CmsPageResult add(CmsPage cmsPage) {
        if(cmsPage==null){
            CustomExceptionCast.cast(CommonCode.FAIL);
        }
        CmsPage cms = repository.findBySiteIdAndPageNameAndPageWebPath(cmsPage.getSiteId(), cmsPage.getPageName(), cmsPage.getPageWebPath());
        if (cms!= null) {
         CustomExceptionCast.cast(CmsCode.CMS_ADDPAGE_EXISTSNAME);
        }
        cmsPage.setPageId(null);
        repository.save(cmsPage);
        CmsPageResult cmsPageResult = new CmsPageResult(CommonCode.SUCCESS, cmsPage);
        return cmsPageResult;
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
}
