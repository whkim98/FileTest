package com.example.test.controller;

import com.example.test.service.itemService;
import com.example.test.service.updateService;
import jakarta.servlet.http.HttpServletRequest;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.sql.RowId;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;

@Controller
public class updateController {

    @Autowired
    private updateService updateService;

    @Autowired
    private itemService itemService;

    @PostMapping("/file/update")
    public String update(@RequestParam("myfile") MultipartFile myfile,
                         HttpServletRequest request) {

        // 파일 업로드 경로 설정
        String uploadPath = request.getSession().getServletContext().getRealPath("/resources/file");
        System.out.println("업로드 경로: " + uploadPath + myfile.getOriginalFilename());

        // 파일 이름 가져오기
        String testFile = myfile.getOriginalFilename();

        // 파일 저장 경로 생성
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();  // 디렉토리가 없으면 생성
        }

        try {
            // 파일을 실제 경로에 저장
            File saveFile = new File(uploadPath, testFile);
            myfile.transferTo(saveFile);  // 파일 저장

            System.out.println("파일 저장 완료: " + saveFile.getAbsolutePath());

        } catch (IOException e) {
            e.printStackTrace();
        }

        updateService.insert(testFile);

        return "redirect:/";
    }


    @PostMapping("/file/updatee")
    public String updatee(@RequestParam("myfile") MultipartFile myfile,
                          @RequestParam("previous_filename") String previousFilename,
                          HttpServletRequest request, @RequestParam("id") int id) {

        String uploadPath = request.getSession().getServletContext().getRealPath("/resources/file");
        String newFileName = myfile.getOriginalFilename();

        // 이전 파일 삭제
        File oldFile = new File(uploadPath + File.separator + previousFilename);
        if (oldFile.exists()) {
            if (oldFile.delete()) {
                System.out.println("이전 파일 삭제 성공: " + previousFilename);
            } else {
                System.out.println("이전 파일 삭제 실패: " + previousFilename);
            }
        }

        // 새 파일 저장
        try {
            File newFile = new File(uploadPath + File.separator + newFileName);
            myfile.transferTo(newFile);
            System.out.println("새 파일 저장 성공: " + newFileName);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("파일 저장 실패");
        }

        // 업데이트 테이블 인설트
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("table_name", newFileName);
        updateService.updateFile(map);

        // 아이템 인설트
        Map<String, Object> map1 = new HashMap<>();
        map1.put("id", id);
        map1.put("field_name", newFileName);
        map1.put("from_value", previousFilename);
        itemService.insert(map1);

        int recentiid = itemService.recentiid();

        // content 업데이트
        JSONObject updateContentJson = new JSONObject();
        JSONArray valueArray = new JSONArray();
        valueArray.put(previousFilename);
        valueArray.put(newFileName);
        updateContentJson.put("title", valueArray);

        Map<String, Object> map2 = new HashMap<>();
        map2.put("id", id);
        map2.put("recentiid", recentiid);
        map2.put("update_content", updateContentJson.toString());

        updateService.updateContent(map2);

        return "redirect:/";
    }


}
