package com.atguigu.gmall.search.service;

import com.atguigu.gmall.search.vo.SearchParamVO;
import com.atguigu.gmall.search.vo.SearchResponse;
import org.springframework.stereotype.Service;

import java.io.IOException;


public interface SearchService {
    SearchResponse search(SearchParamVO searchParamVO) throws IOException;
}
