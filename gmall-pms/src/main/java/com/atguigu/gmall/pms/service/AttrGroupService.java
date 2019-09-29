package com.atguigu.gmall.pms.service;

import com.atguigu.gmall.pms.vo.AttrGroupVO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.atguigu.gmall.pms.entity.AttrGroupEntity;
import com.atguigu.core.bean.PageVo;
import com.atguigu.core.bean.QueryCondition;

import java.util.List;


/**
 * 属性分组
 *
 * @author luzuquan
 * @email lzq@atguigu.com
 * @date 2019-09-22 13:41:37
 */
public interface AttrGroupService extends IService<AttrGroupEntity> {

    PageVo queryPage(QueryCondition params);

    PageVo queryAttrGroupByCid(Long catId, QueryCondition queryCondition);

    AttrGroupVO queryGroupWithAttrByGid(Long gid);

    List<AttrGroupVO> queryGroupWithAttrByCid(Long catId);
}

