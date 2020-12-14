package com.example.elasticsearch.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author Yeeep
 * @date 2020/12/2
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("item")
@Document(indexName = "item", shards = 1, replicas = 1)
public class Item implements Serializable {
    private static final long serialVersionUID = 3953564069670004701L;

    @Id
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 标题
     */
    @Field(analyzer = "ik_max_word", type = FieldType.Text)
    @TableField("`title`")
    private String title;

    /**
     * 分类
     */
    @Field(analyzer = "ik_max_word", type = FieldType.Text)
    @TableField("category")
    private String category;

    /**
     * 品牌
     */
    @Field(type = FieldType.Keyword)
    @TableField("brand")
    private String brand;

    /**
     * 价格
     */
    @Field(type = FieldType.Double)
    @TableField("price")
    private Double price;

    /**
     * 图片地址
     */
    @TableField("thumbnail")
    private String thumbnail;
}
