package com.example.elasticsearch.elasticsearch.repository;

import com.example.elasticsearch.model.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Highlight;
import org.springframework.data.elasticsearch.annotations.HighlightField;
import org.springframework.data.elasticsearch.annotations.HighlightParameters;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

/**
 * 继承ElasticsearchRepository
 *
 * @author Yeeep
 * @date 2020/12/2
 */
public interface ItemRepository extends ElasticsearchRepository<Item, Long> {

    /**
     * 关键字检索(匹配title和category)
     *
     * @param title 标题
     * @return
     */
    @Highlight(
            fields = {
                    @HighlightField(name = "title")
            },
            parameters = @HighlightParameters(
                    preTags = "<strong><font style='color:red'>",
                    postTags = "</font></strong>",
                    fragmentSize = 500,
                    numberOfFragments = 3
            )
    )
    List<SearchHit<Item>> findByTitle(String title);


    /**
     * 关键字检索(匹配title和category)
     *
     * @param title    标题
     * @param category 类目
     * @param brand    品牌
     * @param pageable 分页
     * @return
     */
    @Highlight(
            fields = {
                    @HighlightField(name = "title")
            },
            parameters = @HighlightParameters(
                    preTags = "<strong><font style='color:red'>",
                    postTags = "</font></strong>",
                    fragmentSize = 500,
                    numberOfFragments = 3
            )
    )
    Page<Item> findByTitleOrCategoryOrBrand(String title, String category, String brand, Pageable pageable);


}
