package com.example.test.service;

import com.example.test.dto.updateDto;
import com.example.test.mapper.updateInter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class updateService {
    @Autowired
    private updateInter updateInter;

    public void insert(String table_name) {
        updateInter.insert(table_name);
    }

    public List<updateDto> fileList(){
        return updateInter.fileList();
    }

    public void updateFile(Map<String, Object> map) {
        updateInter.updateFile(map);
    }

    public void updateContent(Map<String, Object> map){
        updateInter.updateContent(map);
    }
}
