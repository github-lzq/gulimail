package com.atguigu.gmall.ums.dao;

import com.atguigu.gmall.ums.entity.MemberEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员
 * 
 * @author luzuquan
 * @email lzq@atguigu.com
 * @date 2019-09-21 13:32:26
 */
@Mapper
public interface MemberDao extends BaseMapper<MemberEntity> {
	
}
