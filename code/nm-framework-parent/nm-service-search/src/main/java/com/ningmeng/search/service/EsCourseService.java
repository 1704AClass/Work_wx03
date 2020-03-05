package com.ningmeng.search.service;

import com.ningmeng.framework.domain.course.CoursePub;
import com.ningmeng.framework.domain.search.CourseSearchParam;
import com.ningmeng.framework.model.response.CommonCode;
import com.ningmeng.framework.model.response.QueryResponseResult;
import com.ningmeng.framework.model.response.QueryResult;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.swing.text.Highlighter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class EsCourseService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EsCourseService.class);

    @Value("${ningmeng.elasticsearch.course.index}")
    private String es_index;
    @Value("${ningmeng.elasticsearch.course.type}")
    private String es_type;
    @Value("${ningmeng.elasticsearch.course.source_field}")
    private String source_field;
    @Autowired
    RestHighLevelClient restHighLevelClient;
    public QueryResponseResult list(int page, int size, CourseSearchParam courseSearchParam) {
        if(page<=0){
            page=1;
        }
        if(size<=0){
            size=10;
        }
            //设置索引
            SearchRequest searchRequest = new SearchRequest(es_index);
            //设置查询类型
            searchRequest.types(es_type);
            //创建条件查询对象
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.from((page-1)*size);
            searchSourceBuilder.size(size);
            //注明这是boolean方式查询（filter）
            BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
            String[] source_fields = source_field.split(",");
            searchSourceBuilder.fetchSource(source_fields, new String[]{});

            if(StringUtils.isNotEmpty(courseSearchParam.getKeyword())){
                //匹配关键字，绑定列名  根据什么域进行查询
                MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery(courseSearchParam.getKeyword(), "name", "teachplan","description");
                multiMatchQueryBuilder.minimumShouldMatch("70%");
                multiMatchQueryBuilder.field("name",10);
                boolQueryBuilder.must(multiMatchQueryBuilder);
            }

        //过虑
        if(StringUtils.isNotEmpty(courseSearchParam.getMt())){
            boolQueryBuilder.filter(QueryBuilders.termQuery("mt",courseSearchParam.getMt()));
        }
        if(StringUtils.isNotEmpty(courseSearchParam.getSt())){
            boolQueryBuilder.filter(QueryBuilders.termQuery("st",courseSearchParam.getSt()));
        }
        if(StringUtils.isNotEmpty(courseSearchParam.getGrade())){
            boolQueryBuilder.filter(QueryBuilders.termQuery("grade",courseSearchParam.getGrade()));
        }
            //绑定查询
            searchSourceBuilder.query(boolQueryBuilder);
        //高亮显示
        HighlightBuilder highlightBuilder= new HighlightBuilder();
        highlightBuilder.preTags("<font class='eslight'>");
        highlightBuilder.postTags("</font>");
        highlightBuilder.fields().add(new HighlightBuilder.Field("name"));
        searchSourceBuilder.highlighter(highlightBuilder);
        //请求搜索
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = null;
        try {
            //执行搜索
                searchResponse = restHighLevelClient.search(searchRequest);
            } catch (IOException e) {
                e.printStackTrace();
                LOGGER.error("xuecheng search error..{}",e.getMessage());
                return new QueryResponseResult(CommonCode.SUCCESS,new QueryResult<CoursePub>());
            }
            SearchHits hits = searchResponse.getHits();
            SearchHit[] searchHits = hits.getHits();
            long totalHits = hits.getTotalHits();
            List<CoursePub> list = new ArrayList<>();
            for (SearchHit hit : searchHits) {
                CoursePub coursePub = new CoursePub();
                Map<String, Object> sourceAsMap = hit.getSourceAsMap(); //取出名称
                String name = (String) sourceAsMap.get("name");
                Map<String, HighlightField> highlightFields = hit.getHighlightFields();
                if(highlightFields!=null){
                    HighlightField namefield = highlightFields.get("name");
                    if(namefield!=null){
                        Text[] fragments = namefield.getFragments();
                        StringBuffer stringBuffer = new StringBuffer();
                        for (Text str:fragments){
                            stringBuffer.append(str.toString());
                        }
                        name=stringBuffer.toString();
                    }
                }
                coursePub.setName(name);
                String pic = (String) sourceAsMap.get("pic");
                coursePub.setPic(pic);
                Float price = null;
                try {
                    if(sourceAsMap.get("price")!=null ){
                        price = Float.parseFloat((String) sourceAsMap.get("price"));
                    } } catch (Exception e) {
                    e.printStackTrace();
                }
                coursePub.setPrice(price);
                Float price_old = null;
                try {
                    if(sourceAsMap.get("price_old")!=null ){
                        price_old = Float.parseFloat((String) sourceAsMap.get("price_old"));
                    } } catch (Exception e) {
                    e.printStackTrace();
                }
                coursePub.setPrice_old(price_old);
                list.add(coursePub);
            }
        QueryResult<CoursePub> queryResult = new QueryResult<>();
            queryResult.setList(list);
            queryResult.setTotal(totalHits);
            QueryResponseResult coursePubQueryResponseResult =
                    new QueryResponseResult(CommonCode.SUCCESS,queryResult);
            return coursePubQueryResponseResult;
    }
}
