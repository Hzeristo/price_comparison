package com.example.price_comparison.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ApiService {
    @Autowired
    private RestTemplate restTemplate;

    public String sendPostRequest(String url, String jsonPayload) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        HttpEntity<String> entity = new HttpEntity<>(jsonPayload, headers);
        
        String response = restTemplate.postForObject(url, entity, String.class);
        return response;
    }

    public String sendGetRequest(String url) {
        return restTemplate.getForObject(url, String.class);
    }
}
