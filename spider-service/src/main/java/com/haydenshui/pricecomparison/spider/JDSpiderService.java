package com.haydenshui.pricecomparison.spider;

import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.JavascriptExecutor;
import java.time.Duration;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class JDSpiderService {

    private WebDriver driver;

    public JDSpiderService() {
        
    }

    public void initDriver() {
        String edgeDriverPath = getClass().getClassLoader().getResource("drivers/msedgedriver.exe").getPath();
        System.setProperty("webdriver.edge.driver", edgeDriverPath); 
        EdgeOptions options = new EdgeOptions();
        options.addArguments("--disable-gpu"); // 禁用 GPU 加速
        options.addArguments("--window-size=1920,1080"); // 设置窗口大小
        options.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/115.0.0.0 Safari/537.36");
        options.addArguments("--disable-blink-features=AutomationControlled");
        this.driver = new EdgeDriver(options);
    }

    // 登录并保存 Cookies
    public void loginAndSaveCookies(String loginUrl, String username, String password) throws IOException {
        driver.get(loginUrl);

        // 填写用户名和密码
        driver.findElement(By.id("loginname")).sendKeys(username);
        driver.findElement(By.id("nloginpwd")).sendKeys(password);
        driver.findElement(By.id("loginsubmit")).click();

        // 等待页面跳转或登录成功标识元素出现
        new WebDriverWait(driver, Duration.ofSeconds(60)).until(ExpectedConditions.presenceOfElementLocated(By.id("treasure")));

        // 保存登录后的 Cookies
        saveCookies();
    }

    // 保存 Cookies 到文件
    private void saveCookies() throws IOException {
        File file = new File("src/main/resources/jdcookies.data");
        if (!file.exists()) {
            file.createNewFile();  // 如果文件不存在则创建
        }
        
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file))) {
            Set<Cookie> cookies = driver.manage().getCookies();
            for (Cookie cookie : cookies) {
                bufferedWriter.write(cookie.getName() + ";" + cookie.getValue() + ";" + cookie.getDomain() + ";" +
                        cookie.getPath() + ";" + cookie.getExpiry() + ";" + cookie.isSecure());
                bufferedWriter.newLine();
            }
            System.out.println("Cookies 保存完成！");
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException("保存 Cookies 失败！");
        }
    }

    // 加载 Cookies 并恢复会话
    public void loadCookies(String loginUrl) throws IOException {
        driver.get(loginUrl); // 打开目标站点以初始化会话

        File file = new File("src/main/resources/cookies.data");
        if (!file.exists()) {
            throw new IOException("Cookies 文件不存在，请先登录并保存 Cookies");
        }

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length >= 6) {
                    String name = parts[0];
                    String value = parts[1];
                    String domain = parts[2];
                    String path = parts[3];
                    String expiry = parts[4];
                    boolean isSecure = Boolean.parseBoolean(parts[5]);

                    Cookie cookie = new Cookie.Builder(name, value)
                            .domain(domain)
                            .path(path)
                            .isSecure(isSecure)
                            .build();
                    driver.manage().addCookie(cookie);
                }
            }
            System.out.println("Cookies 加载完成！");
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException("加载 Cookies 失败！");
        }

        // 刷新页面以应用 Cookies
        driver.navigate().refresh();
    }


    public boolean autoLogin(String loginUrl) {
        try {
            loadCookies(loginUrl); // 加载 Cookies
            driver.get(loginUrl); // 打开登录页面，测试是否已登录
    
            // 检查是否存在已登录标志（如用户头像或欢迎文本）
            new WebDriverWait(driver, Duration.ofSeconds(30)).until(
                ExpectedConditions.presenceOfElementLocated(By.id("treasure"))
            );
            System.out.println("自动登录成功！");
            return true;
        } catch (Exception e) {
            System.out.println("自动登录失败，需要重新登录：" + e.getMessage());
            return false;
        }
    }

    public void ensureLoggedIn(String loginUrl, String username, String password) throws IOException {
        if (!autoLogin(loginUrl)) {
            System.out.println("Cookies 无效，尝试重新登录...");
            loginAndSaveCookies(loginUrl, username, password); // 手动登录并保存 Cookies
        }
    }    

    public List<Map<String, String>> fetchProducts(String searchUrl) {
        List<Map<String, String>> products = new ArrayList<>();
        System.out.println("开始抓取商品列表...");
        try {
            driver.get(searchUrl);

            // 等待商品列表加载完成
            new WebDriverWait(driver, Duration.ofSeconds(30)).until(
                ExpectedConditions.presenceOfElementLocated(By.id("J_goodsList"))
            );
            System.out.println("商品列表加载完成！");
            // 获取商品列表
            List<WebElement> items = driver.findElements(By.cssSelector("li.gl-item"));
            System.out.println("商品列表数量：" + items.size());

            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
            
            //skuid, name, url, image, price, platform, category 
            for (WebElement item : items) {
                try {
                    String skuid = item.getAttribute("data-sku");
                    System.out.println("SKU ID：" + skuid);

                    WebElement nameElement = item.findElement(By.cssSelector("div div.p-name.p-name-type-2 a em"));
                    String name = nameElement.getText();
                    System.out.println("商品名称：" + name);

                    //button
                    WebElement urlElement = item.findElement(By.cssSelector("div div.p-img a"));
                    String url = urlElement.getAttribute("href");
                    System.out.println("商品链接：" + url);

                    WebElement imgElement = item.findElement(By.cssSelector("div div.p-img a img"));
                    String image = imgElement.getAttribute("src");
                    System.out.println("商品图片：" + image);

                    // 提取价格
                    String price = item.findElement(By.cssSelector("div div.p-price strong i")).getText();
                    System.out.println("商品价格：" + price);

                    /* urlElement.click();

                    WebElement categoryElement = item.findElement(By.cssSelector("div.crumb.fl.clearfix"));
                    System.out.println("商品分类：" + categoryElement.getText());
                    WebElement output= categoryElement.findElement(By.cssSelector("div.item > a[clstag='shangpin|keycount|product|mbNav-2']"));
                    String categoryName = categoryElement.getText();
                    System.out.println("商品分类：" + categoryName);

                    driver.navigate().back(); */

                    // 将数据保存到 Map 中
                    Map<String, String> product = new HashMap<>();
                    product.put("skuid", skuid);
                    product.put("name", name);
                    product.put("url", url);
                    product.put("image", image);
                    product.put("price", price);
                    product.put("platform", "京东");
                    product.put("category", "placeholder");
                    products.add(product);
                } catch (Exception e) {
                    // 忽略单个商品解析失败的情况
                    System.out.println("商品解析失败：" + e.getMessage());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return products;
    }

    public Map<String, String> fetchProduct(String searchUrl) {
        Map<String, String> product = new HashMap<>();
        System.out.println("开始获取单个商品价格...");
        try {
            driver.get(searchUrl);
            new WebDriverWait(driver, Duration.ofSeconds(30)).until(
                ExpectedConditions.presenceOfElementLocated(By.id("J_goodsList"))
            );
            System.out.println("商品列表加载完成！");
            // 获取商品列表
            List<WebElement> items = driver.findElements(By.cssSelector("li.gl-item"));
            System.out.println("商品列表数量：" + items.size());
            
            if (!items.isEmpty()) {
                WebElement firstItem = items.get(0); // 获取第一个商品
                try {
                    // 相对定位获取价格
                    String skuid = firstItem.getAttribute("data-sku");
                    System.out.println("SKU ID：" + skuid);

                    WebElement nameElement = firstItem.findElement(By.cssSelector("div div.p-name.p-name-type-2 a em"));
                    String name = nameElement.getText();
                    System.out.println("商品名称：" + name);

                    //button
                    WebElement urlElement = firstItem.findElement(By.cssSelector("div div.p-img a"));
                    String url = urlElement.getAttribute("href");
                    System.out.println("商品链接：" + url);

                    WebElement imgElement = firstItem.findElement(By.cssSelector("div div.p-img a img"));
                    String image = imgElement.getAttribute("src");
                    System.out.println("商品图片：" + image);

                    // 提取价格
                    String price = firstItem.findElement(By.cssSelector("div div.p-price strong i")).getText();
                    System.out.println("商品价格：" + price);

                    /* urlElement.click();

                    WebElement categoryElement = item.findElement(By.cssSelector("div.crumb.fl.clearfix"));
                    System.out.println("商品分类：" + categoryElement.getText());
                    WebElement output= categoryElement.findElement(By.cssSelector("div.item > a[clstag='shangpin|keycount|product|mbNav-2']"));
                    String categoryName = categoryElement.getText();
                    System.out.println("商品分类：" + categoryName);

                    driver.navigate().back(); */

                    // 将数据保存到 Map 中
                    product.put("skuid", skuid);
                    product.put("name", name);
                    product.put("url", url);
                    product.put("image", image);
                    product.put("price", price);
                    product.put("platform", "京东");
                    product.put("category", "placeholder");
                    return product;
                } catch (Exception e) {
                    System.out.println("无法找到价格元素：" + e.getMessage());
                    return product; // 未找到价格时返回默认值
                }
            } else {
                System.out.println("未找到商品！");
                return product;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return product;
        } 
    }

    public List<Map<String, String>> fetchProductsWithLogin(String loginUrl, String searchUrl, String username, String password) {
        try {
            initDriver();
            ensureLoggedIn(loginUrl, username, password); // 检查登录状态
            return fetchProducts(searchUrl); // 开始抓取商品信息
        } catch (IOException e) {
            System.out.println("登录失败：" + e.getMessage());
            return new ArrayList<>();
        }
    }

    public Map<String, String> fetchProductWithLogin(String loginUrl, String searchUrl, String username, String password) {
        try {
            initDriver();
            ensureLoggedIn(loginUrl, username, password); // 检查登录状态
            return fetchProduct(searchUrl); // 开始抓取商品信息
        } catch (IOException e) {
            System.out.println("登录失败：" + e.getMessage());
            return new HashMap<>();
        }
    }

    // 退出 WebDriver，释放资源
    public void quit() {
        if (driver != null) {
            driver.quit();
        }
    }
}
