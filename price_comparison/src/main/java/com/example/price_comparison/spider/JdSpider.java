package com.example.price_comparison.spider;

import com.example.price_comparison.model.Item;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.By;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.concurrent.TimeUnit;
import java.io.FileReader;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

public class JdSpider implements ProductSpider {

    private WebDriver driver;

    @Autowired
    private String edgeDriverLocalPath;

    @Autowired
    private String jdDictionaryPath;

    public JdSpider() { }
    
    private void setupDriver() {
        EdgeOptions options = new EdgeOptions();
        options.addArguments("--headless"); // 无头模式
        System.setProperty("webdriver.edge.driver", edgeDriverLocalPath); // 请替换为你的 msedgedriver 路径
        driver = new EdgeDriver(options);
    }

    public List<Item> startRequests(String query, String type) {
        setupDriver();
        String startUrl;

        if ("keyword".equalsIgnoreCase(type)) {
            startUrl = "https://search.jd.com/Search?keyword=" + query;
        } else if ("category".equalsIgnoreCase(type)) {
            startUrl = Optional.ofNullable(getStartUrl(jdDictionaryPath, query))
                .orElseThrow(() -> new IllegalArgumentException("未找到分类: " + query));
        } else {
            throw new IllegalArgumentException("不支持的爬虫类型: " + type);
        }

        try {
            driver.get(startUrl);
            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
            // 获取页面内容
            List<WebElement> items = driver.findElements(By.cssSelector("#J_goodsList ul li"));
            List<Item> res = new ArrayList<>();
            for (WebElement item : items) {
                String title = item.findElement(By.cssSelector("div div a em")).getText();
                String price = item.findElement(By.cssSelector("div div strong i")).getText();
                String skuid = item.getAttribute("data-sku");
                String detailsUrl = item.findElement(By.cssSelector("div div.p-img a")).getAttribute("href");
                Item product = Item.builder()
                    .name(title)
                    .price((double)Double.parseDouble(price))
                    .skuid(skuid)
                    .url(detailsUrl)
                    .platform("jd")
                    //.categoryId()
                    .build();
                res.add(product);
            }
            return res;
        } catch (Exception e) {
            System.err.println("访问京东时发生错误: " + e.getMessage());
            return null;
        } finally {
            close();
        }
    }

    public void close() {
        if (driver != null) {
            driver.quit();
        }
    }
}
