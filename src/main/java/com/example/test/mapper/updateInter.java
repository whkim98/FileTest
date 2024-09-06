package com.example.test.mapper;

import com.example.test.dto.updateDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface updateInter {
    void insert(String table_name);
    List<updateDto> fileList();
    void updateFile(Map<String, Object> map);
    void updateContent(Map<String, Object> map);
}
