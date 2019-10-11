package com.atguigu.gamll.index.service.impl;

import com.alibaba.fastjson.JSON;
import com.atguigu.core.bean.Resp;
import com.atguigu.gamll.index.annotation.GuliCache;
import com.atguigu.gamll.index.feign.GmallPmsClient;
import com.atguigu.gamll.index.service.IndexService;
import com.atguigu.gmall.pms.CategoryVO;
import com.atguigu.gmall.pms.entity.CategoryEntity;
import org.apache.commons.lang.StringUtils;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;


import java.util.List;
@Service
public class IndexServiceImpl implements IndexService {
    @Autowired
    private GmallPmsClient gmallPmsClient;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private static final String KEY_PREFIX="index:cate:";

    private static final Integer TIMEOUT =30;

    @Autowired
    private RedissonClient redissonClient;

    @Override
    public List<CategoryEntity> queryLvllCates() {
        Resp<List<CategoryEntity>> listResp = this.gmallPmsClient.queryCategoriesByCidOrLevel(1, null);
        return listResp.getData();
    }
    @GuliCache(prefix = "index:cats")
    @Override
    public List<CategoryVO> queryCatesbyPid(Long pid) {
        //1.先查询缓存，缓存中有直接返回
        //2.查询数据库
        Resp<List<CategoryVO>> listResp = this.gmallPmsClient.queryCategorysWithSub(pid);
        List<CategoryVO> categoryVOS = listResp.getData();
        //3.放入缓存
        return categoryVOS;
    }
}
