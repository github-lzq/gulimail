package com.atguigu.gamll.index.service;

import com.atguigu.gmall.pms.CategoryVO;
import com.atguigu.gmall.pms.entity.CategoryEntity;

import java.util.List;

public interface IndexService {
    List<CategoryEntity> queryLvllCates();

    List<CategoryVO> queryCatesbyPid(Long pid);
}
