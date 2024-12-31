package com.haydenshui.pricecomparison.spider;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.openqa.selenium.JavascriptExecutor;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.time.Duration;
import javax.imageio.ImageIO;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.coordinates.WebDriverCoordsProvider;

@Service
public class PriceHistoryService {

    private WebDriver driver;

    public PriceHistoryService() {
    }

    public void initDriver() {
        String edgeDriverPath = getClass().getClassLoader().getResource("drivers/msedgedriver.exe").getPath();
        System.setProperty("webdriver.edge.driver", edgeDriverPath);
        EdgeOptions options = new EdgeOptions();
        options.addArguments("--disable-gpu");
        options.addArguments("--headless");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/115.0.0.0 Safari/537.36");
        options.addArguments("--disable-blink-features=AutomationControlled");
        this.driver = new EdgeDriver(options);
    }

    public byte[] getJDPriceHistory(String skuId) throws IOException, InterruptedException {
        // 打开目标页面
        driver.get("http://item.jdvvv.com/" + skuId + ".html");
    
        // 等待 canvas 元素加载
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement canvas = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("container")));
    
        Thread.sleep(5000);
    
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", canvas);
    
        Thread.sleep(5000);

        JavascriptExecutor js = (JavascriptExecutor) driver;
        Number devicePixelRatio = (Number) js.executeScript("return window.devicePixelRatio");
        double scale = devicePixelRatio.doubleValue();
        System.out.println("Device Pixel Ratio: " + scale);

    
        // 截取整个页面
        Screenshot fullScreenshot = new AShot()
                .coordsProvider(new WebDriverCoordsProvider())
                .takeScreenshot(driver);
    
        BufferedImage fullImage = fullScreenshot.getImage();
        System.out.println("完整截图大小：宽=" + fullImage.getWidth() + ", 高=" + fullImage.getHeight());

        /* ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(fullImage, "png", baos);
        byte[] imageBytes = baos.toByteArray();

        return imageBytes; */

        Point location = canvas.getLocation();
        Dimension size = canvas.getSize();

    // 元素的裁剪区域坐标 (调整为实际像素)
        int x1 = (int) (location.getX() * scale);

        // 定义裁剪区域的坐标 (x1, y1) 到 (x2, y2)
        int y1 = 546;
    
        // 裁剪指定区域
        BufferedImage croppedImg = fullImage.getSubimage(x1, y1, 1920, 800);
    
        // 将裁剪后的图片转换为字节数组
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(croppedImg, "png", baos);
        byte[] imageBytes = baos.toByteArray();
    
        return imageBytes;
    }

    public byte[] getTaobaoPriceHistory(String url) throws IOException, InterruptedException {

        // 打开目标页面
        driver.get(url.replace("detail.tmall.com", "detail.tmallvvv.com"));
      
        // 等待 canvas 元素加载
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement canvas = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("container")));
    
        Thread.sleep(5000);
    
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", canvas);
    
        Thread.sleep(5000);

        JavascriptExecutor js = (JavascriptExecutor) driver;
        Number devicePixelRatio = (Number) js.executeScript("return window.devicePixelRatio");
        double scale = devicePixelRatio.doubleValue();
        System.out.println("Device Pixel Ratio: " + scale);

    
        // 截取整个页面
        Screenshot fullScreenshot = new AShot()
                .coordsProvider(new WebDriverCoordsProvider())
                .takeScreenshot(driver);
    
        BufferedImage fullImage = fullScreenshot.getImage();
        System.out.println("完整截图大小：宽=" + fullImage.getWidth() + ", 高=" + fullImage.getHeight());

        /* ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(fullImage, "png", baos);
        byte[] imageBytes = baos.toByteArray();

        return imageBytes; */

        Point location = canvas.getLocation();
        Dimension size = canvas.getSize();

    // 元素的裁剪区域坐标 (调整为实际像素)
        int x1 = (int) (location.getX() * scale);

        // 定义裁剪区域的坐标 (x1, y1) 到 (x2, y2)
        int y1 = 546;
    
        // 裁剪指定区域
        BufferedImage croppedImg = fullImage.getSubimage(x1, y1, 1920, 800);
    
        // 将裁剪后的图片转换为字节数组
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(croppedImg, "png", baos);
        byte[] imageBytes = baos.toByteArray();
    
        return imageBytes;
    }

    public void quit() {
        if (driver != null) {
            driver.quit();
        }
    }
}
