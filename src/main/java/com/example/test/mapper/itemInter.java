package com.example.test.mapper;

import com.example.test.dto.itemDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface itemInter {
    void insertItem(Map<String, Object> map1);
    int recentiid();
    void deleteItem(int id);
    List<itemDto> selectDetail(int id);
}
