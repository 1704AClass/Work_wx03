package com.ningmeng;

import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.IndicesClient;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.query.*;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TestIndex {

    @Resource
    RestHighLevelClient client;

    @Resource
    RestClient restClient;

    //创建索引库
    @Test
    public void testCreateIndex() throws IOException{
        //创建索引请求对象，并设置索引名称
        CreateIndexRequest createIndexRequest = new CreateIndexRequest("nm_course");
        //设置索引参数
        createIndexRequest.settings(Settings.builder().put("number_of_shards",1)
                .put("number_of_replicas",0));
        //设置映射
        Map nameMap = new HashMap();
        nameMap.put("type","text");
        nameMap.put("analyzer","ik_max_word");
        nameMap.put("search_analyzer","ik_smart");
        Map descriptionMap = new HashMap();
        descriptionMap.put("type","text");
        descriptionMap.put("analyzer","ik_max_word");
        descriptionMap.put("search_analyzer","ik_smart");
        Map studymodelMap = new HashMap();
        studymodelMap.put("type","keyword");
        Map priceMap = new HashMap();
        priceMap.put("type","float");
        Map propertiesMap = new HashMap();
        propertiesMap.put("name",nameMap);
        propertiesMap.put("description",descriptionMap);
        propertiesMap.put("studymodel",studymodelMap);
        propertiesMap.put("price",priceMap);
        Map map = new HashMap();
        map.put("properties",propertiesMap);
        createIndexRequest.mapping("doc",map);
        //创建索引操作客户端
        IndicesClient indices = client.indices();
        //创建响应对象
        CreateIndexResponse createIndexResponse = indices.create(createIndexRequest);
        //得到响应结果
        boolean acknowledged = createIndexResponse.isAcknowledged();
        System.out.println(acknowledged);
    }

    //删除索引库
    @Test
    public void testDeleteIndex() throws IOException{
        //删除索引请求对象
        DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest("nm_course");
        //删除索引
        DeleteIndexResponse deleteIndexResponse = client.indices().delete(deleteIndexRequest);
        //删除索引响应对象
        boolean acknowledged = deleteIndexResponse.isAcknowledged();
        System.out.println(acknowledged);
    }


    //添加文档
    @Test
    public void testAddDoc() throws IOException{
        //准备json数据
        Map<String,Object> jsonMap = new HashMap<>();
        jsonMap.put("name","spring Cloud实战");
        jsonMap.put("description","本课程主要从四个章节进行讲解：" +
                "1.微服务架构入门" +
                "2.spring cloud基础入门" +
                "3.实战Spring Boot" +
                "4.注册中心Eureka。");
        jsonMap.put("studymodel","201001");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        jsonMap.put("timestamp",dateFormat.format(new Date()));
        jsonMap.put("price",5.6f);
        //索引请求
        IndexRequest indexRequest = new IndexRequest("nm_course", "doc");
        //指定索引文档内容
        indexRequest.source(jsonMap);
        //索引响应对象
        IndexResponse indexResponse = client.index(indexRequest);
        //获取响应结果
        DocWriteResponse.Result result = indexResponse.getResult();
        System.out.println(result);
    }


    //查询文档
    @Test
    public void getDoc() throws IOException{
        GetRequest getRequest = new GetRequest("nm_course", "doc", "4028e581617f945f01617f9dabc40000");
        GetResponse getResponse = client.get(getRequest);
        boolean exists = getResponse.isExists();
        Map<String,Object> sourceAsMap = getResponse.getSourceAsMap();
        System.out.println(sourceAsMap);
    }

    //更新文档
    @Test
    public void updateDoc() throws IOException{
        UpdateRequest updateRequest = new UpdateRequest("nm_course", "doc", "4028e581617f945f01617f9dabc40000");
        Map<String,Object> map = new HashMap<>();
        map.put("name","Spring Cloud 实战");
        updateRequest.doc(map);
        UpdateResponse update = client.update(updateRequest);
        RestStatus status = update.status();
        System.out.println(status);
    }

    //根据id删除文档
    @Test
    public void testDelDoc() throws IOException{
        //删除文档id
        String id = "eqP_amQBKsGOdwJ4fHiC";
        //删除索引请求对象
        DeleteRequest deleteRequest = new DeleteRequest("nm_course", "doc", id);
        //响应对象
        DeleteResponse deleteResponse = client.delete(deleteRequest);
        //获取响应结果
        DocWriteResponse.Result result = deleteResponse.getResult();
        System.out.println(result);
    }


    //搜索type下的全部记录
    @Test
    public void testSearchAll() throws IOException{
        SearchRequest searchRequest = new SearchRequest("nm_course");
        searchRequest.types("doc");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        //source源字段过滤
        searchSourceBuilder.fetchSource(new String[]{"name","studymodel"},new String[]{});
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest);
        SearchHits hits = searchResponse.getHits();
        SearchHit[] searchHits = hits.getHits();
        for (SearchHit hit: searchHits) {
            String index = hit.getIndex();
            String type = hit.getType();
            String id = hit.getId();
            float score = hit.getScore();
            String sourceAsString = hit.getSourceAsString();
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            String name = (String) sourceAsMap.get("name");
            String studymodel = (String) sourceAsMap.get("studymodel");
            String description = (String) sourceAsMap.get("description");
            System.out.println(name);
            System.out.println(studymodel);
            System.out.println(description);
        }
    }


    //根据关键字搜索
    @Test
    public void testMatchQuery() throws IOException{
        SearchRequest searchRequest = new SearchRequest("nm_course");
        searchRequest.types("doc");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //source原字段过滤
        searchSourceBuilder.fetchSource(new String[]{"name","studymodel"},new String[]{});
        //匹配关键字
        searchSourceBuilder.query(QueryBuilders.matchQuery("description","Spring开发").operator(Operator.OR));
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest);
        SearchHits hits = searchResponse.getHits();
        SearchHit[] searchHits = hits.getHits();
        for (SearchHit hit: searchHits) {
            String index = hit.getIndex();
            String type = hit.getType();
            String id = hit.getId();
            float score = hit.getScore();
            String sourceAsString = hit.getSourceAsString();
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            String name = (String) sourceAsMap.get("name");
            String studymodel = (String) sourceAsMap.get("studymodel");
            String description = (String) sourceAsMap.get("description");
            System.out.println(name);
            System.out.println(studymodel);
            System.out.println(description);
        }
    }


    //BoolQuery，将搜索关键字分词，拿分词去索引库搜索
    @Test
    public void testBoolQuery() throws IOException{
        //创建搜索请求对象
        SearchRequest searchRequest = new SearchRequest("nm_course");
        searchRequest.types("doc");
        //创建搜索原配置对象
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.fetchSource(new String[]{"name","pic","studymodel"},new String[]{});
        //multiQuery
        String keyword = "Spring开发框架";
        MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery("spring框架","name","description").minimumShouldMatch("50%");
        multiMatchQueryBuilder.field("name",10);
        //TermQuery
        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("studymodel","201001");
        //布尔查询
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(multiMatchQueryBuilder);
        boolQueryBuilder.must(termQueryBuilder);
        //设置布尔查询对象
        searchSourceBuilder.query(boolQueryBuilder));
        searchRequest.source(searchSourceBuilder);//设置搜索源配置
        SearchResponse searchResponse = client.search(searchRequest);
        SearchHits hits = searchResponse.getHits();
        SearchHit[] searchHits = hits.getHits();
        for (SearchHit hit: searchHits) {
            Map<String,Object> sourceAsMap = hit.getSourceAsMap();
            System.out.println(sourceAsMap);
        }
    }


    //布尔查询使用过滤器
    @Test
    public void testFilter() throws IOException{
        //创建搜索请求对象
        SearchRequest searchRequest = new SearchRequest("nm_course");
        searchRequest.types("doc");
        //创建搜索原配置对象
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.fetchSource(new String[]{"name","studymodel","price","description"},new String[]{});
        MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery("spring框架","name","description").minimumShouldMatch("50%");
        multiMatchQueryBuilder.field("name",10);
        searchSourceBuilder.query(multiMatchQueryBuilder);
        //布尔查询
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(searchSourceBuilder.query());
        //过滤
        boolQueryBuilder.filter(QueryBuilders.termQuery("studymodel","201001"));
        boolQueryBuilder.filter(QueryBuilders.rangeQuery("price").gte(60).lte(100));

        SearchResponse searchResponse = client.search(searchRequest);
        SearchHits hits = searchResponse.getHits();
        SearchHit[] searchHits = hits.getHits();
        for (SearchHit hit: searchHits) {
            String index = hit.getIndex();
            String type = hit.getType();
            String id = hit.getId();
            float score = hit.getScore();
            String sourceAsString = hit.getSourceAsString();
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            String name = (String) sourceAsMap.get("name");
            String studymodel = (String) sourceAsMap.get("studymodel");
            String description = (String) sourceAsMap.get("description");
            System.out.println(name);
            System.out.println(studymodel);
            System.out.println(description);
        }
    }



}
