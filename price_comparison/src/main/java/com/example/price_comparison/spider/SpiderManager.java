package com.example.price_comparison.spider;

import com.example.price_comparison.model.Item;
import com.example.price_comparison.spider.ProductSpider;
import com.example.price_comparison.spider.SpiderFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.List;

public class SpiderManager {
    
    private final ExecutorService executor;

    private final SpiderFactory factory;

    public SpiderManager(int numThreads) {
        this.executor = Executors.newFixedThreadPool(numThreads);
        this.factory = new SpiderFactory();
    }

    public Future<List<Item>> runSpider(String category, String platform) {
        return executor.submit(() -> {
            ProductSpider spider = factory.createSpider(platform);  // 每个线程创建一个独立的爬虫实例
            return spider.startRequests(category);
        });
    }

    public void shutdown() {
        executor.shutdown();
    }
}
