package com.example.test.controller;

import com.example.test.dto.itemDto;
import com.example.test.service.itemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class DetailController {

    @Autowired
    private itemService itemService;

    @GetMapping("/file/detail")
    @ResponseBody
    public List<itemDto> detail(int id) {
        List<itemDto> item = itemService.selectDetail(id);
//        model.addAttribute("item", item);
        return item;
    }


}
