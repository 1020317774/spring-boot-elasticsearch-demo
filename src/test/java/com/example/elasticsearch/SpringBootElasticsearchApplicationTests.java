package com.example.elasticsearch;

import com.example.elasticsearch.model.Item;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;

@SpringBootTest
class SpringBootElasticsearchApplicationTests {

    @Autowired
    private ElasticsearchRestTemplate template;

    @Test
    void contextLoads() {
        // // 创建索引，会根据Item类的@Document注解信息来创建
        // template.createIndex(Item.class);
        // // 配置映射，会根据Item类中的id、Field等字段来自动完成映射
        // template.putMapping(Item.class);

        // template.createIndex(Item.class);

        template.deleteIndex(Item.class);
    }

}
