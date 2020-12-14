package com.example.elasticsearch.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.elasticsearch.model.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.elasticsearch.core.SearchHit;

import java.util.List;

/**
 * 接口
 *
 * @author Yeeep
 * @date 2020/12/2
 */
public interface ItemService extends IService<Item> {

    /**
     * 根据id创建商品
     *
     * @param id
     * @return
     */
    Item create(Item id);

    /**
     * 根据id删除商品
     *
     * @param id
     * @return
     */
    void delete(Long id);

    /**
     * 从数据库中导入所有商品到ES
     *
     * @return
     */
    int importAll();

    /**
     * 根据关键字搜索名称或者副标题
     *
     * @param keyword
     * @param pageNum
     * @param pageSize
     * @return
     */
    Page<Item> search(String keyword, Integer pageNum, Integer pageSize);

    /**
     * 高亮
     *
     * @param keyword
     * @return
     */
    List<SearchHit<Item>> searchByKeyword(String keyword);
}
