package com.example.price_comparison.spider;

import com.example.price_comparison.model.Item;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

@Service
public class SpiderManager {

    @Getter
    @Setter
    @AllArgsConstructor
    public class SpiderTask {
        private final ProductSpider spider;
        private final Future<List<Item>> future;
    }
    
    private final ExecutorService executor;
    private final SpiderFactory factory;
    private final Map<String, SpiderTask> activeSpiders;

    public SpiderManager(int numThreads) {
        this.executor = Executors.newFixedThreadPool(numThreads);
        this.factory = new SpiderFactory();
        this.activeSpiders = new HashMap<>();
    }

    /**
     * create new spider, start request, store spider object and return the result
     *
     * @param query query keyword
     * @param type query type
     * @param platform platform
     * @return Future<List<Item>> Future object that will hold the result of the request
     */
    public Future<List<Item>> runSpider(String query, String type, String platform) {
        ProductSpider spider = factory.createSpider(platform);
        Future<List<Item>> future = executor.submit(() -> spider.startRequests(query, type));
        activeSpiders.put(platform + "_" + query + "_" + type, new SpiderTask(spider, future)); 
        return future;
    }

    /**
     * stop spider by query, type and platform and remove it from activeSpiders
     *
     * @param query query keyword
     * @param type spider type
     * @param platform platform to query
     */
    public void stopSpider(String query, String type, String platform) {
        SpiderTask task = activeSpiders.get(platform + "_" + query + "_" + type);
        if (task != null) {
            task.future.cancel(true);
            task.spider.close();
            activeSpiders.remove(platform + "_" + query + "_" + type);
        }
    }

    /**
     * get all active spiders
     * 
     * @return Map<String, Future<List<Item>>> map of active spiders
     */
    public Map<String, Future<List<Item>>> getActiveSpiders() {
        return activeSpiders;
    }

    /**
     * shutdown all active spiders
     */
    public void shutdown() {
        for (SpiderTask task : activeSpiders.values()) {
            task.future.cancel(true);
            task.spider.close();
        }
        executor.shutdown();
        activeSpiders.clear();
    }

}
