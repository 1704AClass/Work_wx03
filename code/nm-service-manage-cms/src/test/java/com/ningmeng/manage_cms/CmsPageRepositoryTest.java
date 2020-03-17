package com.ningmeng.manage_cms;

import com.ningmeng.framework.domain.cms.CmsPage;
import com.ningmeng.framework.domain.cms.CmsPageParam;
import com.ningmeng.manage_cms.dao.CmsPageRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@RunWith(SpringRunner.class)
public class CmsPageRepositoryTest{
    @Resource
    private CmsPageRepository cmsPageRepository;

    @Resource
    private RestTemplate restTemplate;


    //分页测试
    @Test
    public void testFindPage(){
        int page = 0;//从0开始
        int size = 10;//每页记录数
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<CmsPage> all = cmsPageRepository.findAll(pageRequest);
        for (CmsPage cmsPage: all) {
            System.out.println(cmsPage);
        }

    }

    //添加
    @Test
    public void testInsert(){
        //定义实体类
        CmsPage cmsPage = new CmsPage();
        cmsPage.setSiteId("s01");
        cmsPage.setTemplateId("t01");
        cmsPage.setPageName("测试页面");
        cmsPage.setPageCreateTime(new Date());
        List<CmsPageParam> cmsPageParams = new ArrayList<>();
        CmsPageParam cmsPageParam = new CmsPageParam();
        cmsPageParam.setPageParamName("param1");
        cmsPageParam.setPageParamValue("value1");
        cmsPageParams.add(cmsPageParam);
        cmsPage.setPageParams(cmsPageParams);
        cmsPageRepository.save(cmsPage);
        System.out.println(cmsPage);
    }

    //删除
    @Test
    public void testDelete() {
        cmsPageRepository.deleteById("5e42b5769ea35758444596e1");
    }

    //修改
    @Test
    public void testUpdate() {
        Optional<CmsPage> optional = cmsPageRepository.findById("5abefd525b05aa293098fca6");
        if(optional.isPresent()){
            CmsPage cmsPage = optional.get();
            cmsPage.setPageName("index2.html");
            cmsPageRepository.save(cmsPage);
        }
    }

    //自定义条件查询测试
    @Test
    public void testFindAll(){
        //条件匹配器
        ExampleMatcher exampleMatcher = ExampleMatcher.matching();
        //页面名称模糊查询，需要自定义字符串的匹配器实现模糊查询
        exampleMatcher = exampleMatcher.withMatcher("pageName",ExampleMatcher.GenericPropertyMatchers.contains());

        exampleMatcher = exampleMatcher.withMatcher("pageAliase",ExampleMatcher.GenericPropertyMatchers.startsWith());

        //条件值
        CmsPage cmsPage = new CmsPage();
       /* //站点ID
        cmsPage.setSiteId("5a751fab6abb5044e0d19ea1");
        //模板ID
        cmsPage.setTemplateId("5a962c16b00ffc514038fafd");*/

        cmsPage.setPageAliase("ccc");
        cmsPage.setPageName("index2");
        //创建了一个条件实例
        Example<CmsPage> example = Example.of(cmsPage,exampleMatcher);

        PageRequest pageRequest = new PageRequest(0, 10);
        Page<CmsPage> all = cmsPageRepository.findAll(example,pageRequest);
        System.out.println(all.getContent());
    }


    //负载均衡
    @Test
    public void testRibbon(){
        //服务id
        String serviceId = "NM-SERVICE-MANAGE-CMS";
        for (int i=0;i<5;i++){
            //通过服务id调用
            ResponseEntity<CmsPage> forEntity = restTemplate.getForEntity("http://" + serviceId + "/cms/page/get/5a754adf6abb500ad05688d9", CmsPage.class);
            CmsPage cmsPage = forEntity.getBody();
            System.out.println(cmsPage);
        }
    }





}
