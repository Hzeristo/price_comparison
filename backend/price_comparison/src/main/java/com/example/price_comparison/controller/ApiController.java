package com.example.price_comparison.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import com.example.price_comparison.service.ApiService;


@RestController
@RequestMapping("/api")
public class ApiController {

    @Autowired
    private ApiService apiService;

    @Getter
    @Setter
    public static class SimilarityRequest {
        private String text1;
        private String text2;
    }

    /**
     * 处理 POST 请求，发送 JSON 数据到外部 API 并返回结果。
     * 
     * @param jsonPayload 要发送的 JSON 数据
     * @return 外部 API 返回的结果
     */
    @PostMapping("/send")
    public ResponseEntity<String> sendPost(@RequestBody SimilarityRequest similarityRequest) {
        String url = "http://localhost:5000/similarity"; // 替换为你的 URL
        // 将请求体转为 JSON 字符串
        String jsonPayload = String.format("{\"text1\": \"%s\", \"text2\": \"%s\"}", 
            similarityRequest.getText1(), 
            similarityRequest.getText2());
        String result = apiService.sendPostRequest(url, jsonPayload);
        return ResponseEntity.ok(result);
    }

    /**
     * 处理 GET 请求，发送请求到外部 API 并返回结果。
     * 
     * @return 外部 API 返回的结果
     */
    @GetMapping("/get")
    public ResponseEntity<String> sendGet() {
        String url = "http://localhost:5000/similarity"; // 替换为你的 URL
        String result = apiService.sendGetRequest(url);
        return ResponseEntity.ok(result);
    }
}

