package com.example.price_comparison.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class AppConfig {

    @Value("${api.similarity.url}")
    private String similarityUrl;

    @Value("${dictionary.paths.jd}")
    private String jdDictionaryPath;

    @Value("${dictionary.paths.tmall}")
    private String tmallDictionaryPath;

    @Value("${dictionary.paths.taobao}")
    private String taobaoDictionaryPath;

    @Value("${dictionary.paths.vip}")
    private String vipDictionaryPath;

    @Value("${hanlp.similarity.path}")
    private String hanlpSimilarityPath;
    
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public String similarityUrl() {
        return similarityUrl; 
    }

    @Bean
    public String hanlpSimilarityPath() {
        return hanlpSimilarityPath;
    }

    @Bean
    public Map<String, String> dictionaryPaths() {
        Map<String, String> paths = new HashMap<>();
        paths.put("jd", jdDictionaryPath);
        paths.put("tmall", tmallDictionaryPath);
        paths.put("taobao", taobaoDictionaryPath);
        paths.put("vip", vipDictionaryPath);
        return paths;
    }
}
