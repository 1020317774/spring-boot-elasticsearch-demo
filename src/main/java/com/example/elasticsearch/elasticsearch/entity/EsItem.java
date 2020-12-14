package com.example.elasticsearch.elasticsearch.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;

/**
 * EsItem
 *
 * @author knox
 * @date 2020/12/14
 * @since 1.0.0
 */
@Document(indexName = "item", shards = 1, replicas = 1)
public class EsItem implements Serializable {

    private static final long serialVersionUID = 6786605894957324153L;

    @Id
    private String id;

    /**
     * keyword类型的字段只能通过精确值搜索到。filter
     */
    @Field(analyzer = "ik_max_word", type = FieldType.Text)
    private String content;


}
