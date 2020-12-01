package com.example.elasticsearch.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.elasticsearch.model.Item;
import org.apache.ibatis.annotations.Mapper;

/**
 * Mapper
 *
 * @author Yeeep
 * @date 2020/12/2
 */
@Mapper
public interface ItemMapper extends BaseMapper<Item> {

}
