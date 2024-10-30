package com.example.price_comparison.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableAsync
@EnableScheduling
public class AppConfig {

    @Value("${dictionary.paths.jd}")
    private String jdDictionaryPath;

    @Value("${dictionary.paths.tmall}")
    private String tmallDictionaryPath;

    @Value("${dictionary.paths.taobao}")
    private String taobaoDictionaryPath;

    @Value("${dictionary.paths.vip}")
    private String vipDictionaryPath;

    @Value("${hanlp.api.url}")
    private String apiUrl;

    @Value("${hanlp.auth}")
    private String apiKey;

    @Value("${edge.driver.local.path}")
    private String edgeDriverLocalPath;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
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

    @Bean
    public String apiUrl() {
        return apiUrl;
    }

    @Bean
    public String apiKey() {
        return apiKey;
    }

    @Bean
    public String edgeDriverLocalPath() {
        return edgeDriverLocalPath;
    }
}
