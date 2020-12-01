package com.example.elasticsearch.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.elasticsearch.mapper.ItemMapper;
import com.example.elasticsearch.model.Item;
import com.example.elasticsearch.repository.ItemRepository;
import com.example.elasticsearch.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
import java.util.List;

/**
 * 实现类
 *
 * @author Yeeep
 * @date 2020/12/2
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ItemServiceImpl extends ServiceImpl<ItemMapper, Item> implements ItemService {

    @Autowired
    private ItemRepository itemRepository;


    @Override
    public Item create(Item item) {
        // 创建插入数据库
        int insert = this.baseMapper.insert(item);
        if (1 == insert) {
            // 索引
            itemRepository.save(item);
            return item;
        }
        return null;
    }

    @Override
    public void delete(Long id) {
        // 删除索引
        itemRepository.deleteById(id);
        // 同步删除数据库
        this.baseMapper.deleteById(id);
    }

    @Override
    public Page<Item> search(String keyword, Integer pageNum, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNum, pageSize);
        return itemRepository.findByTitleOrCategoryOrBrand(keyword, keyword, keyword, pageable);
    }


    /**
     * 导入所有数据库中商品到ES
     *
     * @return
     */
    @Override
    public int importAll() {
        // 查询数据库使用商品
        List<Item> esProductList = this.baseMapper.selectList(null);
        // 索引到ES
        Iterable<Item> esProductIterable = itemRepository.saveAll(esProductList);
        Iterator<Item> iterator = esProductIterable.iterator();
        int result = 0;
        while (iterator.hasNext()) {
            result++;
            iterator.next();
        }
        return result;
    }

}
