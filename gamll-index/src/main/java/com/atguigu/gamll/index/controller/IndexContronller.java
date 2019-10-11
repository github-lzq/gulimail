package com.atguigu.gamll.index.controller;

import com.atguigu.core.bean.Resp;
import com.atguigu.gamll.index.service.IndexService;
import com.atguigu.gmall.pms.CategoryVO;
import com.atguigu.gmall.pms.entity.CategoryEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("index")
public class IndexContronller {
    @Autowired
    private IndexService indexService;
    @GetMapping("cates")
    public Resp<List<CategoryEntity>> queryLvllCates(){
        List<CategoryEntity> lvllCates = this.indexService.queryLvllCates();
        return Resp.ok(lvllCates);
    }
    @GetMapping("cates/{pid}")
    public Resp<List<CategoryVO>> queryCatesByPid(@PathVariable("pid")Long pid){
        List<CategoryVO> cates =indexService.queryCatesbyPid(pid);
        return Resp.ok(cates);
    }
}
