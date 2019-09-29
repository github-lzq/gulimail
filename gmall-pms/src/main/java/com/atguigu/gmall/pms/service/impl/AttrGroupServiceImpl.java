package com.atguigu.gmall.pms.service.impl;

import com.atguigu.gmall.pms.dao.AttrAttrgroupRelationDao;
import com.atguigu.gmall.pms.dao.AttrDao;
import com.atguigu.gmall.pms.entity.AttrAttrgroupRelationEntity;
import com.atguigu.gmall.pms.entity.AttrEntity;
import com.atguigu.gmall.pms.vo.AttrGroupVO;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.core.bean.PageVo;
import com.atguigu.core.bean.Query;
import com.atguigu.core.bean.QueryCondition;

import com.atguigu.gmall.pms.dao.AttrGroupDao;
import com.atguigu.gmall.pms.entity.AttrGroupEntity;
import com.atguigu.gmall.pms.service.AttrGroupService;
import org.springframework.util.CollectionUtils;


@Service("attrGroupService")
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupDao, AttrGroupEntity> implements AttrGroupService {
    @Autowired
    AttrGroupDao attrGroupDao;
    @Autowired
    AttrAttrgroupRelationDao relationDao;
    @Autowired
    private AttrDao attrDao;

    @Override
    public PageVo queryPage(QueryCondition params) {
        IPage<AttrGroupEntity> page = this.page(
                new Query<AttrGroupEntity>().getPage(params),
                new QueryWrapper<AttrGroupEntity>()
        );

        return new PageVo(page);
    }

    @Override
    public PageVo queryAttrGroupByCid(Long catId, QueryCondition queryCondition) {
        //构建查询条件
        QueryWrapper<AttrGroupEntity> queryWrapper = new QueryWrapper<>();
        //拼接查询条件
        if (catId != null) {
            queryWrapper.eq("catelog_id", catId);
        }
        IPage<AttrGroupEntity> page = this.page(
                new Query<AttrGroupEntity>().getPage(queryCondition),
                queryWrapper
        );
        return new PageVo(page);
    }

    @Override
    public AttrGroupVO queryGroupWithAttrByGid(Long gid) {
        AttrGroupVO attrGroupVO = new AttrGroupVO();
        //1.先查询attrGroup
        AttrGroupEntity attrGroupEntity = this.attrGroupDao.selectById(gid);
        //拷贝参数
        BeanUtils.copyProperties(attrGroupEntity, attrGroupVO);
        //2.再去查询AttrAttrGroup
        List<AttrAttrgroupRelationEntity> relationEntities = this.relationDao.selectList(new
                QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_group_id", gid));

        if (CollectionUtils.isEmpty(relationEntities)){
            return attrGroupVO;
        }
        //将查询到的relationEntities插入到AttrGroupVO对象中
        attrGroupVO.setRelations(relationEntities);
        //3.最后查询Attr规格参数
        List<Long> idList = relationEntities.stream().map(relationEntity ->
                relationEntity.getAttrId()).collect(Collectors.toList());
        List<AttrEntity> attrEntities = this.attrDao.selectBatchIds(idList);
        attrGroupVO.setAttrEntities(attrEntities);
        return attrGroupVO;
    }

    @Override
    public List<AttrGroupVO> queryGroupWithAttrByCid(Long catId) {
        //根据分类id查询所有组
        List<AttrGroupEntity> attrGroupEntities = this.list(new QueryWrapper<AttrGroupEntity>().eq("catelog_id",catId));
       //判空
        if (CollectionUtils.isEmpty(attrGroupEntities)){
            return null;
        }
    return attrGroupEntities.stream().map(attrGroupEntity -> {
         return  this.queryGroupWithAttrByGid(attrGroupEntity.getAttrGroupId());
     }).collect(Collectors.toList());



    }

}