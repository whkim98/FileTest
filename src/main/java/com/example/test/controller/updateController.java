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
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;

@Controller
public class updateController {

    @Autowired
    private updateService updateService;

    @Autowired
    private itemService itemService;

    //원래는 해당 정보(ex. 게시판)에 해당하는 첨부파일을 해서 pk, fk 관계를 통해 해당 정보에 해당하는 파일만 보이도록 하는
    //로직이 필요하지만 지금은 제외함. 어떤 식으로 진행될 지 모르기 때문
    @PostMapping("/file/update")
    public String update(@RequestParam("myfiles") MultipartFile[] myfiles,
                         HttpServletRequest request) {

        // 파일 업로드 경로 설정
        String uploadPath = request.getSession().getServletContext().getRealPath("/resources/file");
        System.out.println("업로드 경로: " + uploadPath);

        // 파일 저장 경로 생성
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdir();  // 디렉토리가 없으면 생성
        }

        System.out.println(Arrays.toString(myfiles));

        for (MultipartFile myfile : myfiles) {
            if (!myfile.isEmpty()) {
                // 파일 이름 가져오기
                String testFile = myfile.getOriginalFilename();
                try {
                    // 파일을 실제 경로에 저장
                    File saveFile = new File(uploadPath, testFile);
                    myfile.transferTo(saveFile);

                    System.out.println("파일 저장 완료: " + saveFile.getAbsolutePath());

                    updateService.insert(testFile);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

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

        // 업데이트 테이블 업데이트
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

        // 가장 최근 insert된 아이템 테이블의 PK 데이터
        int recentiid = itemService.recentiid();

        // 기존 JSON 데이터 읽기
        File jsonFile = new File(uploadPath + File.separator + "update_content_" + id + ".json");
        JSONObject updateContentJson = new JSONObject();
        if (jsonFile.exists()) {
            try {
                // JSON 파일에서 기존 데이터 읽어오기
                String content = new String(Files.readAllBytes(jsonFile.toPath()), StandardCharsets.UTF_8);
                updateContentJson = new JSONObject(content);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // 새 데이터 추가
        JSONArray valueArray = new JSONArray();
        valueArray.put(previousFilename);
        valueArray.put(newFileName);
        updateContentJson.append("title", valueArray); // 기존 데이터에 추가

        // update 테이블 content 업데이트
        Map<String, Object> map2 = new HashMap<>();
        map2.put("id", id);
        map2.put("recentiid", recentiid);
        map2.put("update_content", updateContentJson.toString());
        updateService.updateContent(map2);

        // JSON 파일로 저장
        try {
            if (!jsonFile.exists()) {
                jsonFile.createNewFile();
            }

            // JSON 데이터를 파일로 저장
            FileWriter fileWriter = new FileWriter(jsonFile);
            fileWriter.write(updateContentJson.toString(4));
            fileWriter.flush();
            fileWriter.close();

            System.out.println("JSON 파일 저장 성공: " + jsonFile.getAbsolutePath());

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("JSON 파일 저장 실패");
        }

        return "redirect:/";
    }



}
