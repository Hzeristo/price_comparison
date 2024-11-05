package com.example.price_comparison.spider;

public class SpiderFactory {
    
    public SpiderFactory() {}
    
    public static ProductSpider createSpider(String platform) {
        switch (platform.toLowerCase()) {
            case "jd":
                return new JdSpider();
            case "taobao":
                return new TaobaoSpider();
            default:
                throw new IllegalArgumentException("不支持的平台: " + platform);
        }
    }
}
