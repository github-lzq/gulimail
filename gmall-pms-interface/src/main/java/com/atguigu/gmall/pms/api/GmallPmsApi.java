package com.atguigu.gmall.pms.api;

import com.atguigu.core.bean.PageVo;
import com.atguigu.core.bean.QueryCondition;
import com.atguigu.core.bean.Resp;
import com.atguigu.gmall.pms.CategoryVO;
import com.atguigu.gmall.pms.entity.BrandEntity;
import com.atguigu.gmall.pms.entity.CategoryEntity;
import com.atguigu.gmall.pms.entity.ProductAttrValueEntity;
import com.atguigu.gmall.pms.entity.SkuInfoEntity;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface GmallPmsApi {
    @ApiOperation("查询spu下的sku")
    @GetMapping("pms/skuinfo/{spuId}")
    public Resp<List<SkuInfoEntity>> querySkuBySpuId(@PathVariable("spuId")Long spuId);

    @ApiOperation("详情查询")
    @GetMapping("pms/brand/info/{brandId}")
    public Resp<BrandEntity> brandinfo(@PathVariable("brandId") Long brandId);

    @ApiOperation("详情查询")
    @GetMapping("pms/category/info/{catId}")
    public Resp<CategoryEntity> categoryinfo(@PathVariable("catId") Long catId);

    @GetMapping("pms/skuinfo/page")
    public Resp<PageVo> querySpuByPageAndSale(QueryCondition condition);

    @GetMapping("pms/productattrvalue/{spuId}")
    public Resp<List<ProductAttrValueEntity>> queryBySpuId(@PathVariable("spuId") Long spuId);

    @GetMapping("pms/category")
    public Resp<List<CategoryEntity>> queryCategoriesByCidOrLevel(@RequestParam(value = "level",
            defaultValue = "0") Integer level, @RequestParam(value = "parentCid", required = false) Long parentCid);

    @GetMapping("pms/category/{pid}")
    public Resp<List<CategoryVO>> queryCategorysWithSub(@PathVariable("pid")Long pid);
}
