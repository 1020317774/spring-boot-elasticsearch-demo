package com.example.elasticsearch.repository;

import com.example.elasticsearch.model.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

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
     * @param title    标题
     * @param category 类目
     * @param brand    品牌
     * @param pageable 分页
     * @return
     */
    Page<Item> findByTitleOrCategoryOrBrand(String title, String category, String brand, Pageable pageable);

}
