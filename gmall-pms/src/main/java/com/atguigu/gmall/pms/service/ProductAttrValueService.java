package com.atguigu.gmall.pms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.atguigu.gmall.pms.entity.ProductAttrValueEntity;
import com.atguigu.core.bean.PageVo;
import com.atguigu.core.bean.QueryCondition;

import java.util.List;


/**
 * spu属性值
 *
 * @author luzuquan
 * @email lzq@atguigu.com
 * @date 2019-09-22 13:41:37
 */
public interface ProductAttrValueService extends IService<ProductAttrValueEntity> {

    PageVo queryPage(QueryCondition params);

    List<ProductAttrValueEntity> queryBySpuId(Long spuId);
}

