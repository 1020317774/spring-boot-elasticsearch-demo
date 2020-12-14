package com.example.elasticsearch.controller;

import com.example.elasticsearch.common.R;
import com.example.elasticsearch.model.Item;
import com.example.elasticsearch.service.ItemService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Yeeep
 * @date 2020/12/2
 */
@RestController
@RequestMapping("/item")
@Api(value = "ItemController", tags = "商品管理")
public class ItemController {

    @Autowired
    private ItemService itemService;

    /**
     * 根据id创建商品
     *
     * @param item 商品对象
     * @return
     */
    @ApiOperation(value = "创建商品")
    @PostMapping(value = "/create")
    public R create(@RequestBody Item item) {
        Item esProduct = itemService.create(item);
        if (esProduct != null) {
            return R.ok().data(esProduct).message("数据同步成功");
        } else {
            return R.error();
        }
    }

    /**
     * 根据id删除商品
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "根据id删除商品")
    @DeleteMapping(value = "/delete/{id}")
    public R delete(@PathVariable Long id) {
        itemService.delete(id);
        return R.ok();
    }

    /**
     * 全量同步，导入所有数据库中商品到ES
     *
     * @return
     */
    @ApiOperation(value = "导入所有数据库中商品到ES")
    @RequestMapping(value = "/importAll", method = RequestMethod.POST)
    public R importAllList() {
        int count = itemService.importAll();
        return R.ok().data(count);
    }

    /**
     * 简单搜索
     *
     * @param keyword
     * @param pageNum
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "简单搜索")
    @GetMapping(value = "/search/simple/{keyword}")
    public R search(@PathVariable(value = "keyword") String keyword,
                    @ApiParam(value = "pageNum", name = "页码") @RequestParam(value = "pageNum", required = false, defaultValue = "0") Integer pageNum,
                    @ApiParam(value = "pageSize", name = "每页查询数量") @RequestParam(value = "pageSize", required = false, defaultValue = "5") Integer pageSize) {
        Page<Item> esProductPage = itemService.search(keyword, pageNum, pageSize);
        return R.ok().data(esProductPage);
    }

    @GetMapping("/search/{keyword}")
    public R searchByKeyword(@PathVariable("keyword") String keyword) {
        List<SearchHit<Item>> items = itemService.searchByKeyword(keyword);
        return R.ok().data(items);
    }

}
