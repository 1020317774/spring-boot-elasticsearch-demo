package com.example.elasticsearch.elasticsearch.repository;

import com.example.elasticsearch.model.Item;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest
class ItemRepositoryTest {
    @Autowired
    private ItemRepository itemRepository;

    @Test
    void index() {
        Item build = Item.builder()
                .id(111L)
                .title("小米10Pro")
                .category("智能手机")
                .brand("小米")
                .price(2000D)
                .build();

        // 索引一条
        itemRepository.save(build);

        // 批量索引
        List<Item> items = new ArrayList<>();
        items.add(Item.builder().id(1L).title("iphone12").category("智能手机").brand("苹果").price(6000D).build());
        items.add(Item.builder().id(2L).title("iphone12 Mini").category("智能手机").brand("苹果").price(3000D).build());

        itemRepository.saveAll(items);
    }

    @Test
    void delete() {
        itemRepository.deleteById(1L);
    }

    @Test
    void find() {
        Optional<Item> byId = itemRepository.findById(1L);
        byId.ifPresent(System.out::println);
    }

    @Test
    void query() {
        // // 查询所有
        // Iterable<Item> all = itemRepository.findAll();
        // all.forEach(System.out::println);
        //
        // // 排序查询
        Iterable<Item> idSort = itemRepository.findAll(Sort.by(Sort.Order.asc("id")));
        idSort.forEach(System.out::println);
        //
        // // 排序查询
        // List<Sort.Order> list = new ArrayList<>();
        // list.add(Sort.Order.asc("price"));
        // list.add(Sort.Order.asc("id"));
        // // 默认asc
        // list.add(Sort.Order.by("id"));
        // Iterable<Item> sortAll = itemRepository.findAll(Sort.by(list));
        // sortAll.forEach(System.out::println);
        //
        // // ID查询
        // Optional<Item> byId = itemRepository.findById(1L);
        // System.out.println(byId);

        // 分页排序查询
        // Pageable pageable = PageRequest.of(1, 10, Sort.by("id"));
        // Page<Item> all = itemRepository.findAll(pageable);


        // 自定义方法
        // List<Item> byPriceBetween = itemRepository.findByPriceBetween(3000L, 6000L);
        // byPriceBetween.forEach(System.out::println);

        // List<Item> allByPriceBetween = itemRepository.findAllByPriceBetween(3000L, 6000L);
        // allByPriceBetween.forEach(System.out::println);
    }
}