package com.example.test;

import com.example.test.dto.updateDto;
import com.example.test.service.updateService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private updateService update;

    @GetMapping("/")
    public String home(Model model) {
        List<updateDto> list = update.fileList();
        model.addAttribute("list", list);
        return "home/index";
    }

//    @PostMapping("/file/update")
//    public String update(@RequestParam("myfile") MultipartFile file, HttpServletRequest request) {
//        String uploadPath = request.getSession().getServletContext().getRealPath("/resources");
//        System.out.println(uploadPath);
//        return "update";
//    }
}