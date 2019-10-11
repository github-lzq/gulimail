package com.atguigu.gmall.pms.dao;

import com.atguigu.gmall.pms.CategoryVO;
import com.atguigu.gmall.pms.entity.CategoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 商品三级分类
 * 
 * @author luzuquan
 * @email lzq@atguigu.com
 * @date 2019-09-22 13:41:37
 */
@Mapper
public interface CategoryDao extends BaseMapper<CategoryEntity> {
	List<CategoryVO> querySubCategories(Long pid);
}
