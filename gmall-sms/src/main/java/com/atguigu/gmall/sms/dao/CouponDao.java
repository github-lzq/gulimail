package com.atguigu.gmall.sms.dao;

import com.atguigu.gmall.sms.entity.CouponEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 优惠券信息
 * 
 * @author luzuquan
 * @email lzq@atguigu.com
 * @date 2019-09-21 13:31:24
 */
@Mapper
public interface CouponDao extends BaseMapper<CouponEntity> {
	
}
