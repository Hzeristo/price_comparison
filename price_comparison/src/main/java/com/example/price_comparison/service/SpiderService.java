package com.example.price_comparison.service;

import com.example.price_comparison.spider.*;
import java.util.List;
import java.util.concurrent.Future;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;


@Service
public class SpiderService {
    
    @Autowired
    private SpiderManager spiderManager;

    @Scheduled(fixedRate = 3600000) // 每小时执行一次
    public void scheduleFavoriteItemsSpider() {
        List<String> favoriteItems = getFavoriteItems(); // 从数据库或其他来源获取收藏夹商品信息
        for (String item : favoriteItems) {
            spiderManager.runSpider(item, "keyword", "jd"); // 关键字爬取
        }
    }

}
