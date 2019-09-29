package com.atguigu.gmall.pms.service.impl;

import com.atguigu.gmall.pms.dao.SpuInfoDescDao;
import com.atguigu.gmall.pms.entity.*;
import com.atguigu.gmall.pms.feign.GmallSmsFeign;
import com.atguigu.gmall.pms.service.*;
import com.atguigu.gmall.pms.vo.ProductAttrValueVO;
import com.atguigu.gmall.pms.vo.SkuInfoVO;
import com.atguigu.gmall.pms.vo.SpuInfoVO;
import com.atguigu.gmall.sms.vo.SkuSaleVO;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.core.bean.PageVo;
import com.atguigu.core.bean.Query;
import com.atguigu.core.bean.QueryCondition;

import com.atguigu.gmall.pms.dao.SpuInfoDao;
import org.springframework.util.CollectionUtils;


@Service("spuInfoService")
public class SpuInfoServiceImpl extends ServiceImpl<SpuInfoDao, SpuInfoEntity> implements SpuInfoService {

    @Autowired
    private SpuInfoDescDao spuInfoDescDao;
    @Autowired
    private ProductAttrValueService productAttrValueService;
    @Autowired
    private SkuInfoService skuInfoService;
    @Autowired
    private SkuImagesService skuImagesService;
    @Autowired
    private SkuSaleAttrValueService skuSaleAttrValueService;
    @Autowired
    private GmallSmsFeign smsFeign;

    @Override
    public PageVo queryPage(QueryCondition params) {
        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                new QueryWrapper<SpuInfoEntity>()
        );

        return new PageVo(page);
    }

    @Override
    public PageVo querySpuByKeyPage(QueryCondition condition, Long catId) {
        QueryWrapper<SpuInfoEntity> wrapper = new QueryWrapper<>();
        //判断catId是否为null
        if (catId != 0) {
            wrapper.eq("catalog_id", catId);
        }
        //判断用户是否输入了查询条件
        String key = condition.getKey();
        if (StringUtils.isNotBlank(key)) {
            wrapper.and(t -> {
                //默认情况下，所有条件都是and关系，并且没有括号
                //or（）.拼接条件，变成or关系
                //or（t-》{拼接条件}）
                //and（t-》{拼接条件}）
                return t.eq("id", key).like("spu_name" +
                        "", key);
            });
        }

        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(condition),
                wrapper
        );
        return new PageVo(page);
    }

    @Override
    public void savaSpuWithSku(SpuInfoVO spuInfoVO) {
        //1.保存spu相关信息
        //1.1保存spuInfo
       SpuInfoEntity spuInfoEntity = new SpuInfoEntity();
        BeanUtils.copyProperties(spuInfoVO, spuInfoEntity);
        //spuInfoEntity.setCreateTime(LocalDateTime.now());
        spuInfoEntity.setCreateTime(new Date());
        spuInfoEntity.setUodateTime(spuInfoEntity.getCreateTime());
        this.save(spuInfoEntity);
        Long spuId = spuInfoEntity.getId();
        //1.2保存spuInfoDesc ==spuimages

        if (!CollectionUtils.isEmpty(spuInfoVO.getSpuImages())) {
            SpuInfoDescEntity spuInfoDescEntity = new SpuInfoDescEntity();
            spuInfoDescEntity.setSpuId(spuId);
            spuInfoDescEntity.setDecript(StringUtils.join(spuInfoVO.getSpuImages(), ","));
            this.spuInfoDescDao.insert(spuInfoDescEntity);
        }

        //1.3保存product_attr_value==baseAttrs
        List<ProductAttrValueVO> baseAttrs = spuInfoVO.getBaseAttrs();
        if (!CollectionUtils.isEmpty(baseAttrs)) {
            List<ProductAttrValueEntity> productAttrValueEntities = baseAttrs.stream().map(baseAttr -> {
                ProductAttrValueEntity productAttrValueEntity = new ProductAttrValueEntity();
                productAttrValueEntity.setAttrId(baseAttr.getAttrId());
                productAttrValueEntity.setAttrName(baseAttr.getAttrName());
                productAttrValueEntity.setAttrId(spuId);
                productAttrValueEntity.setAttrValue(baseAttr.getAttrValue());
                productAttrValueEntity.setAttrSort(1);
                productAttrValueEntity.setQuickShow(0);
                return productAttrValueEntity;
            }).collect(Collectors.toList());
            productAttrValueService.saveBatch(productAttrValueEntities);
        }
        //2.保存sku相关的
        List<SkuInfoVO> skuInfoVOS = spuInfoVO.getSkus();
        if (CollectionUtils.isEmpty(skuInfoVOS)) {
            return;
        }

        skuInfoVOS.forEach(skuInfoVO -> {
            //2.1保存skuInfo
            SkuInfoEntity skuInfoEntity = new SkuInfoEntity();
            BeanUtils.copyProperties(skuInfoVO,skuInfoEntity);
            skuInfoEntity.setSpuId(spuId);
            List<String> images = skuInfoVO.getImages();
            if (!CollectionUtils.isEmpty(images)) {
                skuInfoEntity.setSkuDefaultImg(StringUtils.isEmpty(skuInfoEntity.getSkuDefaultImg())?images.get(0):skuInfoEntity.getSkuDefaultImg());
            }
            skuInfoEntity.setSkuCode(UUID.randomUUID().toString().toUpperCase());
            skuInfoEntity.setCatalogId(spuInfoVO.getCatalogId());
            skuInfoEntity.setBrandId(skuInfoVO.getBrandId());
            this.skuInfoService.save(skuInfoEntity);
            Long skuId = skuInfoEntity.getBrandId();

            //2.2保存skuImages==sku.images
            if (!CollectionUtils.isEmpty(images)){
                String defaultImage = images.get(0);
                List<SkuImagesEntity> skuImagesEntities = images.stream().map(image -> {
                    SkuImagesEntity skuImagesEntity = new SkuImagesEntity();
                    skuImagesEntity.setSkuId(skuId);
                    skuImagesEntity.setImgUrl(image);
                    skuImagesEntity.setImgSort(1);
                    skuImagesEntity.setDefaultImg(StringUtils.equals(defaultImage,image)?1:0);

                    return skuImagesEntity;
                }).collect(Collectors.toList());
                this.skuImagesService.saveBatch(skuImagesEntities);
            }
            //2.3保存sku_asle_attr_value ==sku.saleAttrs
            List<SkuSaleAttrValueEntity> saleAttrs = skuInfoVO.getSaleAttrs();
            if (!CollectionUtils.isEmpty(saleAttrs)) {
                saleAttrs.forEach(saleAttr->{
                    saleAttr.setAttrId(skuId);
                    saleAttr.setAttrSort(1);
                });
                this.skuSaleAttrValueService.saveBatch(saleAttrs);
            }
            //3.保存优惠相关

            SkuSaleVO skuSaleVO = new SkuSaleVO();
            BeanUtils.copyProperties(skuInfoVO,skuSaleVO);
            skuSaleVO.setSkuId(skuId);
            this.smsFeign.saveSale(skuSaleVO);

        });



    }

}