package com.example.test.service;

import com.example.test.dto.itemDto;
import com.example.test.mapper.itemInter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class itemService {

    @Autowired
    private itemInter itemInter;

//    public void insert(Map<String, Object> map1){
//        itemInter.insertItem(map1);
//    }

    public void insert(Map<String, Object> itemData){
        itemInter.insertItem(itemData);
    }

    public int recentiid(){
        return itemInter.recentiid();
    }

    public void deleteItem(int id){
        itemInter.deleteItem(id);
    }

    public List<itemDto> selectDetail(int id){
        return itemInter.selectDetail(id);
    }
}
