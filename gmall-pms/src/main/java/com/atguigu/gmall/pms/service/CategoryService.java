package com.atguigu.gmall.pms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.atguigu.gmall.pms.entity.CategoryEntity;
import com.atguigu.core.bean.PageVo;
import com.atguigu.core.bean.QueryCondition;

import java.util.List;


/**
 * 商品三级分类
 *
 * @author luzuquan
 * @email lzq@atguigu.com
 * @date 2019-09-22 13:41:37
 */
public interface CategoryService extends IService<CategoryEntity> {

    PageVo queryPage(QueryCondition params);

    /**
     * 根据分类等级或者分类的父id查询分类信息
     * @param level
     * @param parentCid
     * @return
     */
    List<CategoryEntity> queryCategoriesByCidOrLevel(Integer level, Long parentCid);
}

