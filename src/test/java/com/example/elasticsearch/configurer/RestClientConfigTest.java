package com.example.elasticsearch.configurer;

import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
class RestClientConfigTest {

    public static final String OK = "OK";
    public static final String CREATED = "CREATED";
    public static final String UPDATED = "UPDATED";
    public static final String DELETED = "DELETED";
    public static final String NOT_FOUND = "NOT_FOUND";

    // 复杂操作使用，简单CRUD使用Es提供的repository接口
    @Autowired
    private RestHighLevelClient restHighLevelClient;

    // 增加索引
    @Test
    void addIndex() {
        IndexRequest indexRequest = new IndexRequest("index");
        Map<String, Object> map = new HashMap<>();
        map.put("id", "1");
        map.put("content", "小黑");
        indexRequest.id("1");   // 设置索引ID
        // indexRequest.type("");// ES7.x之后，类型已被弃用，8.x彻底移除，默认_doc
        indexRequest.source(map);
        try {
            IndexResponse index = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
            System.out.println(index.status().getClass());
            if (OK.equalsIgnoreCase(String.valueOf(index.status()))) {
                System.out.println("索引添加成功");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 删除索引
    @Test
    void testDeleteIndex() {
        DeleteRequest deleteRequest = new DeleteRequest("index", "qRnKYHYBwA5tajsGohWc");
        try {
            DeleteResponse delete = restHighLevelClient.delete(deleteRequest, RequestOptions.DEFAULT);
            System.out.println(delete.status());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // 检索
    @Test
    void testSearchIndex() {
        // 索引请求
        SearchRequest searchRequest = new SearchRequest("item");
        // 索引条件
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.fetchField("title");
        // sourceBuilder.query(QueryBuilders.matchAllQuery())
        //         // 分页
        //         .from(0)
        //         .size(20)
        //         // 过滤
        //         .postFilter(QueryBuilders.matchAllQuery())
        //         // 排序
        //         .sort("price", SortOrder.ASC)
        //         // 高亮
        //         .highlighter(new HighlightBuilder().field("*").requireFieldMatch(false));

        searchRequest.source(sourceBuilder);
        try {
            SearchResponse search = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            System.out.println("命中数：" + search.getHits().getTotalHits());
            System.out.println("符合条件的最大得分：" + search.getHits().getMaxScore());
            // 命中结果
            SearchHit[] hits = search.getHits().getHits();
            for (SearchHit hit : hits) {
                System.out.println(hit.getSourceAsMap());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 批量更新，删除，添加
    @Test
    void testBulk() {
        BulkRequest bulkRequest = new BulkRequest();

        // 索引
        IndexRequest request = new IndexRequest("index");
        Map<String, Object> map = new HashMap<>();
        map.put("id", "2");
        map.put("content", "update2");

        request.source(map);
        bulkRequest.add(request);

        // // 删除
        // DeleteRequest request1 = new DeleteRequest();
        // request1.id("1");
        // bulkRequest.add(request1);
        //
        // // 更新
        //
        // UpdateRequest request2 = new UpdateRequest();
        // Map<String, Object> updateMap = new HashMap<>();
        // map.put("id", "2");
        // map.put("content", "我要更新");
        // request2.doc(updateMap);
        // bulkRequest.add(request2);

        try {
            BulkResponse bulk = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
            System.out.println(bulk.status());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 更新索引
    @Test
    void testUpdateIndex() {
        UpdateRequest updateRequest = new UpdateRequest("index", "qRnKYHYBwA5tajsGohWc");
        Map<String, Object> map = new HashMap<>();
        map.put("content", "更新索引");
        updateRequest.doc(map);
        try {
            UpdateResponse update = restHighLevelClient.update(updateRequest, RequestOptions.DEFAULT);
            System.out.println(update.status());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}