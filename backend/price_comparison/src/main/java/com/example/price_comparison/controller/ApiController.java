package com.example.price_comparison.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${api.similarity.url}")
    private String similarityUrl;

    @Value("${hanlp.similarity.path}")
    private String hanlpSimilarityPath;

    @Getter
    @Setter
    public static class SimilarityRequest {
        private String text1;
        private String text2;
    }

    /**
     * send similarity post request to similarity api 
     * @param similarityRequest
     * @return ResponseEntity with similarity score
     */
    @PostMapping("/similarity/send")
    public ResponseEntity<String> sendSimilarityPost(@RequestBody SimilarityRequest similarityRequest) {
        // 将请求体转为 JSON 字符串
        String jsonPayload = String.format("{\"text1\": \"%s\", \"text2\": \"%s\"}", 
            similarityRequest.getText1(), 
            similarityRequest.getText2());
        String result = apiService.sendPostRequest(similarityUrl, jsonPayload);
        return ResponseEntity.ok(result);
    }

}

