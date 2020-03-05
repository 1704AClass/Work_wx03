package com.ningmeng.search;

import org.elasticsearch.action.DocWriteResponse;
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
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class SearchDemo {

    @Autowired
    private RestHighLevelClient client;


    @Test
    public void testAddDoc()throws IOException{

        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("name", "spring");
        jsonMap.put("description", "spring");
        jsonMap.put("studymodel", "201001");
        jsonMap.put("price", 5.6f);
        jsonMap.put("st","1-2-3");
        jsonMap.put("mt","1-3");
        jsonMap.put("grade","20000");

        IndexRequest indexRequest = new IndexRequest("nm_course","doc");
        indexRequest = indexRequest.source(jsonMap);

        IndexResponse indexResponse = client.index(indexRequest);
        DocWriteResponse.Result result = indexResponse.getResult();
        System.out.println("---------"+result);
    }

    @Test
    public void getDoc() throws IOException{
        GetRequest getRequest = new GetRequest("nm_scoure","doc","3");
        GetResponse getResponse = client.get(getRequest);
        boolean exists = getResponse.isExists();
        Map<String,Object> sourceAsMap = getResponse.getSourceAsMap();
        System.out.println(sourceAsMap);

    }
    @Test
    public void updateDoc()throws IOException{
        UpdateRequest updateRequest = new UpdateRequest("nm_course","doc","SBiPgHABwWH81361XA-z");
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("name", "spring cloud实战GAN JIU WAN LE");

        updateRequest = updateRequest.doc(jsonMap);
        UpdateResponse updateResponse = client.update(updateRequest);
        System.out.println(updateResponse.getResult());
    }
    @Test
    public void delete() throws IOException{

        DeleteRequest deleteRequest = new DeleteRequest("nm__course","doc","SBiPgHABwWH8136XA-Z");
        DeleteResponse deleteResponse = client.delete(deleteRequest);
        System.out.println("::::::::::::::::::"+deleteResponse.getResult());

    }

    //booleanQuery过滤查询
    @Test
    public void getDSLByHighlight() throws IOException{

        SearchRequest searchRequest = new SearchRequest("nm_course");

        searchRequest.types("doc");

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery("spring框架","name","escription").minimumShouldMatch("50%").field("name",10);

        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();

        boolQuery.should(multiMatchQueryBuilder);

//        boolQuery.filter(QueryBuilders.termQuery("studymodel",201001));

        boolQuery.filter(QueryBuilders.rangeQuery("price").gte(0).lte(100000000));

        searchSourceBuilder.query(boolQuery);

        searchSourceBuilder.sort("studymodel", SortOrder.DESC);
        searchSourceBuilder.sort("price", SortOrder.DESC);

        HighlightBuilder highlightBuilder = new HighlightBuilder();

        highlightBuilder.preTags("<tag>");
        highlightBuilder.postTags("</tag>");

        highlightBuilder.fields().add(new HighlightBuilder.Field("name"));
        highlightBuilder.fields().add(new HighlightBuilder.Field("description"));
        searchSourceBuilder.highlighter(highlightBuilder);



        searchSourceBuilder.fetchSource(new String[]{"name","studymodel"},new String[]{});

        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = client.search(searchRequest);

        SearchHits searchHits = searchResponse.getHits();

        long total = searchHits.getTotalHits();

        System.out.println(total);

        SearchHit[] searchHits1 = searchHits.getHits();

        for (SearchHit searchHit:searchHits1){
            Map<String,Object> map = searchHit.getSourceAsMap();

            String a = (String) map.get("studymodel");

            String b = "";

            Map<String, HighlightField> highlightFieldMap = searchHit.getHighlightFields();
            if (highlightFieldMap!=null){
                HighlightField nameField = highlightFieldMap.get("name");
                if (nameField!=null){
                    Text[] texts = nameField.getFragments();
                    if (texts!=null&&texts.length>0){
                        b=texts[0].toString();
                    }
                }

            }




            System.out.println("name"+b+"++++++++++studymodel:"+a);
        }
        System.out.println("111111111111");
    }





    //booleanQuery过滤查询
    @Test
    public void getDSLByFilter() throws IOException{

        SearchRequest searchRequest = new SearchRequest("nm_course");

        searchRequest.types("doc");

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery("spring框架","name","escription").minimumShouldMatch("50%").field("name",10);

        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();

        boolQuery.should(multiMatchQueryBuilder);

        boolQuery.filter(QueryBuilders.termQuery("studymodel",201001));

        boolQuery.filter(QueryBuilders.rangeQuery("price").gte(0).lte(100000000));

        searchSourceBuilder.query(boolQuery);

        searchSourceBuilder.sort("studymodel", SortOrder.DESC);
        searchSourceBuilder.sort("price", SortOrder.DESC);


        searchSourceBuilder.fetchSource(new String[]{"name","studymodel"},new String[]{});

        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = client.search(searchRequest);

        SearchHits searchHits = searchResponse.getHits();

        long total = searchHits.getTotalHits();

        System.out.println(total);

        SearchHit[] searchHits1 = searchHits.getHits();

        for (SearchHit searchHit:searchHits1){
            Map<String,Object> map = searchHit.getSourceAsMap();

            String a = (String) map.get("studymodel");

            String b = (String) map.get("name");

            System.out.println("name"+b+"++++++++++studymodel:"+a);
        }
        System.out.println("111111111111");
    }



    //booleanQuery
    @Test
    public void getDSLBool() throws IOException{


        SearchRequest searchRequest = new SearchRequest("nm_course");

        searchRequest.types("doc");

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery("spring框架","name","escription").minimumShouldMatch("50%").field("name",10);

        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("studymodel","201001");

        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        boolQuery.must(multiMatchQueryBuilder);
        boolQuery.must(termQueryBuilder);

        searchSourceBuilder.query(boolQuery);

        searchSourceBuilder.fetchSource(new String[]{"name","studymodel"},new String[]{});

        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = client.search(searchRequest);

        SearchHits searchHits = searchResponse.getHits();

        long total = searchHits.getTotalHits();

        System.out.println(total);

        SearchHit[] searchHits1 = searchHits.getHits();

        for (SearchHit searchHit:searchHits1){
            Map<String,Object> map = searchHit.getSourceAsMap();

            String a = (String) map.get("studymodel");

            String b = (String) map.get("name");

            System.out.println("name"+b+"++++++++++studymodel:"+a);
        }
        System.out.println("111111111111");
    }




    //multi Query
    @Test
    public void getDSLMultiQuery() throws IOException{


        SearchRequest searchRequest = new SearchRequest("nm_course");

        searchRequest.types("doc");

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        searchSourceBuilder.query(QueryBuilders.multiMatchQuery("spring框架","name","description").minimumShouldMatch("50%").field("name",10));

        searchSourceBuilder.fetchSource(new String[]{"name","studymodel"},new String[]{});

        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = client.search(searchRequest);

        SearchHits searchHits = searchResponse.getHits();

        long total = searchHits.getTotalHits();

        System.out.println(total);

        SearchHit[] searchHits1 = searchHits.getHits();

        for (SearchHit searchHit:searchHits1){
            Map<String,Object> map = searchHit.getSourceAsMap();

            String a = (String) map.get("studymodel");

            String b = (String) map.get("name");

            System.out.println("name"+b+"++++++++++studymodel:"+a);
        }
        System.out.println("111111111111");
    }



    //match方法
    @Test
    public void getDSLByMatchQuery() throws IOException{


        SearchRequest searchRequest = new SearchRequest("nm_course");

        searchRequest.types("doc");

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        searchSourceBuilder.query(QueryBuilders.matchQuery("description","spring开发" ).operator(Operator.OR));

        searchSourceBuilder.fetchSource(new String[]{"name","studymodel"},new String[]{});

        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = client.search(searchRequest);

        SearchHits searchHits = searchResponse.getHits();

        long total = searchHits.getTotalHits();

        System.out.println(total);

        SearchHit[] searchHits1 = searchHits.getHits();

        for (SearchHit searchHit:searchHits1){
            Map<String,Object> map = searchHit.getSourceAsMap();

            String a = (String) map.get("studymodel");

            String b = (String) map.get("name");

            System.out.println("name"+b+"++++++++++studymodel:"+a);
        }
        System.out.println("111111111111");
    }

    //ids方法精准查询
    @Test
    public void getDSLByIds() throws IOException{


        SearchRequest searchRequest = new SearchRequest("nm_course");

        searchRequest.types("doc");

        String[] split = new String[]{"lE7-iXAB5AqGkXjCwkj7","2"};
        List<String> idList = Arrays.asList(split);


        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        searchSourceBuilder.query(QueryBuilders.termsQuery("_id", idList));

        searchSourceBuilder.fetchSource(new String[]{"name","studymodel"},new String[]{});

        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = client.search(searchRequest);

        SearchHits searchHits = searchResponse.getHits();

        long total = searchHits.getTotalHits();

        System.out.println(total);

        SearchHit[] searchHits1 = searchHits.getHits();

        for (SearchHit searchHit:searchHits1){
            Map<String,Object> map = searchHit.getSourceAsMap();

            String a = (String) map.get("studymodel");

            String b = (String) map.get("name");

            System.out.println("name"+b+"++++++++++studymodel:"+a);
        }
        System.out.println("111111111111");
    }



//term查询
    @Test
    public void getTermDSL() throws IOException{


        SearchRequest searchRequest = new SearchRequest("nm_course");

        searchRequest.types("doc");

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        searchSourceBuilder.query(QueryBuilders.termQuery("name","spring"));

        searchSourceBuilder.fetchSource(new String[]{"name","studymodel"},new String[]{});

        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = client.search(searchRequest);

        SearchHits searchHits = searchResponse.getHits();

        long total = searchHits.getTotalHits();

        System.out.println(total);

        SearchHit[] searchHits1 = searchHits.getHits();

        for (SearchHit searchHit:searchHits1){
            Map<String,Object> map = searchHit.getSourceAsMap();

            String a = (String) map.get("studymodel");

            String b = (String) map.get("name");

            System.out.println("name"+b+"++++++++++studymodel:"+a);
        }
        System.out.println("111111111111");
    }




    @Test
    public void getESLByPage() throws IOException{
        int page = 1;
        int size = 2;
        SearchRequest searchRequest = new SearchRequest("nm_course");

        searchRequest.types("doc");

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.from((page-1)*size);
        searchSourceBuilder.size(size);


        searchSourceBuilder.query(QueryBuilders.matchAllQuery());

        searchSourceBuilder.fetchSource(new String[]{"name","studymodel"},new String[]{});

        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = client.search(searchRequest);

        SearchHits searchHits = searchResponse.getHits();

        long total = searchHits.getTotalHits();

        System.out.println(total);

        SearchHit[] searchHits1 = searchHits.getHits();

        for (SearchHit searchHit:searchHits1){
            Map<String,Object> map = searchHit.getSourceAsMap();

            String a = (String) map.get("studymodel");

            String b = (String) map.get("name");

            System.out.println("name"+b+"++++++++++studymodel:"+a);
        }



}



    @Test
    public void getDSL() throws Exception{

        SearchRequest searchRequest = new SearchRequest("nm_course");

        searchRequest.types("doc");

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        searchSourceBuilder.query(QueryBuilders.matchAllQuery());

        searchSourceBuilder.fetchSource(new String[]{"name","studymodel"},new String[]{});

        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = client.search(searchRequest);

        SearchHits searchHits = searchResponse.getHits();

        long total = searchHits.getTotalHits();

        System.out.println(total);

        SearchHit[] searchHits1 = searchHits.getHits();

        for (SearchHit searchHit:searchHits1){
            Map<String,Object> map = searchHit.getSourceAsMap();

            String a = (String) map.get("studymodel");

            String b = (String) map.get("name");

            System.out.println("name"+b+"++++++++++studymodel:"+a);
        }

    }


}
