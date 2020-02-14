package com.ningmeng.manage_cms;

import com.ningmeng.framework.domain.cms.CmsPage;
import com.ningmeng.manage_cms.dao.cmsPageRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

@SpringBootTest
@RunWith(SpringRunner.class)
public class cmsPageRepositoryTest  {

    @Autowired
    private cmsPageRepository cmspageRepository;

    @Test
    public void findAll(){
        List<CmsPage> all = cmspageRepository.findAll();
        for (CmsPage cms:all){
            System.out.println(cms);
        }
    }

    @Test
    public void findByPage(){
        int page=1;
        int size=10;
        PageRequest of = PageRequest.of(page, size);
        Page<CmsPage> all = cmspageRepository.findAll(of);
        System.out.println(all.getSize());
    }
    @Test
    public void update(){
        Optional<CmsPage> byId = cmspageRepository.findById("");
        if(byId.isPresent()){
            CmsPage cmsPage = byId.get();
            cmsPage.setPageAliase("小米");
            cmspageRepository.save(cmsPage);
        }
    }
    @Test
    public void findbyname(){
        CmsPage equals = cmspageRepository.findByPageNameEquals("preview_297e7c7");
        System.out.println(equals);
    }
    @Test
    public void findbypagename(){
        ExampleMatcher matching = ExampleMatcher.matching();
        matching= matching.withMatcher("pageName", ExampleMatcher.GenericPropertyMatchers.contains());
        matching= matching.withMatcher("pageAliase", ExampleMatcher.GenericPropertyMatchers.startsWith());
        CmsPage cmsPage = new CmsPage();
        cmsPage.setPageName("4");
        cmsPage.setPageAliase("课程");
        Example<CmsPage> example = Example.of(cmsPage,matching);
        Pageable pageRequest = new PageRequest(0, 10);
        Page<CmsPage> pages = cmspageRepository.findAll(example, pageRequest);
        System.out.println(pages.getContent());
    }
}
