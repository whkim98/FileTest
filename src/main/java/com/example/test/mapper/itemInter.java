package com.example.test.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

@Mapper
public interface itemInter {
    void insertItem(Map<String, Object> map1);
    int recentiid();
    void deleteItem(int id);
}
