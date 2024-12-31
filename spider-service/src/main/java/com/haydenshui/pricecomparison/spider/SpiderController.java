package com.haydenshui.pricecomparison.spider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import com.haydenshui.pricecomparison.shared.util.ApiResponse;
import com.haydenshui.pricecomparison.shared.model.Item;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.core.ParameterizedTypeReference;
import java.util.Map;
import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping("/spider")
public class SpiderController {

    @Autowired
    private RestTemplate restTemplate;

    private final JDSpiderService jdSpiderService;

    private final TaobaoSpiderService taobaoSpiderService;

    private final PriceHistoryService priceHistoryService;

    @Value("${jd.url.login}")
    String jdLoginUrl;

    @Value("${jd.url.search}")
    String jdSearchUrl;

    @Value("${jd.url.item}")
    String jdItemUrl;

    @Value("${taobao.url.login}")
    String taobaoLoginUrl;

    @Value("${taobao.url.search}")
    String taobaoSearchUrl;

    @Autowired
    public SpiderController(JDSpiderService jdSpiderService, PriceHistoryService priceHistoryService, TaobaoSpiderService taobaoSpiderService) {
        this.jdSpiderService = jdSpiderService;
        this.priceHistoryService = priceHistoryService;
        this.taobaoSpiderService = taobaoSpiderService;
    }

    @GetMapping("/favorite")
    public ResponseEntity<ApiResponse<String>> callService1() {
        String Url = "http://favorite-service/favorite/test"; // 使用服务名称访问
        System.out.println("wdefwwv gbnsofu gdoshfd");
        ResponseEntity<ApiResponse<String>> response = restTemplate.exchange(
            Url,
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<ApiResponse<String>>() {}
        );
        return response;
    }

    @GetMapping("/items")
    public ResponseEntity<ApiResponse<String>> callService2() {
        String Url = "http://item-service/items/test"; // 使用服务名称访问
        System.out.println("jidijowi wof wufowi dwxscjf");
        ResponseEntity<ApiResponse<String>> response = restTemplate.exchange(
            Url,
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<ApiResponse<String>>() {}
        );
        return response;
    }


    // 登录并保存 cookies
    @PostMapping("/jd/login")
    public ResponseEntity<ApiResponse<Boolean>> loginAndSaveJDCookies(@RequestParam String username, @RequestParam String password) {
        try {
            jdSpiderService.initDriver();
            jdSpiderService.loginAndSaveCookies(jdLoginUrl, username, password);
            return ResponseEntity.ok(ApiResponse.success(true));
        } catch (IOException e) {
            return ResponseEntity.ok(ApiResponse.failure("登录失败"));
        }
    }

    @GetMapping("/jd/search")
    public ResponseEntity<ApiResponse<List<Map<String, String>>>> fetchJDProducts(@RequestParam String searchKeyWord, @RequestParam String username, @RequestParam String password) {
        try{
            String url = "http://item-service/items/spider";
            List<Map<String, String>> products = jdSpiderService.fetchProductsWithLogin(jdLoginUrl, jdSearchUrl + searchKeyWord, username, password);
            System.out.println("Done");
            ResponseEntity<ApiResponse<Boolean>> response = restTemplate.exchange(
                url,                           // 请求 URL
                HttpMethod.POST,               // 请求方法
                new HttpEntity<>(products),    // 请求体
                new ParameterizedTypeReference<ApiResponse<Boolean>>() {}  // 响应体类型
            );
            System.out.println("Done");
            jdSpiderService.quit();
            return ResponseEntity.ok(ApiResponse.success(products));
        } catch (Exception e) {
            jdSpiderService.quit();
            return ResponseEntity.ok(ApiResponse.failure("搜索失败"));
        }
    }

    @GetMapping("/jd/sku")
    public ResponseEntity<ApiResponse<Map<String, String>>> fetchJDProductBySku(@RequestParam String skuId, @RequestParam String store, @RequestParam String username, @RequestParam String password) {
        try{
            String url = "http://item-service/items/sku?skuId=" + skuId;
            Map<String, String> product = jdSpiderService.fetchProductWithLogin(jdLoginUrl, jdItemUrl + skuId + ".html", username, password);
            System.out.println("Done");
            if(store.equals("y")){
                ResponseEntity<ApiResponse<Item>> response = restTemplate.exchange(
                    url,                           // 请求 URL
                    HttpMethod.POST,               // 请求方法
                    new HttpEntity<>(product),    // 请求体
                    new ParameterizedTypeReference<ApiResponse<Item>>() {}  // 响应体类型
                );
            }
            System.out.println("Done");
            jdSpiderService.quit();
            return ResponseEntity.ok(ApiResponse.success(product));
        } catch (Exception e) {
            jdSpiderService.quit();
            return ResponseEntity.ok(ApiResponse.failure("搜索失败"));
        }
    }

    @GetMapping("/jd/name")
    public ResponseEntity<ApiResponse<Map<String, String>>> fetchJDProductByName(@RequestParam String name, @RequestParam String username, @RequestParam String password) {
        try{
            Map<String, String> product = jdSpiderService.fetchProductWithLogin(jdLoginUrl, jdSearchUrl + name, username, password);
            System.out.println("Done");
            jdSpiderService.quit();
            return ResponseEntity.ok(ApiResponse.success(product));
        } catch (Exception e) {
            jdSpiderService.quit();
            return ResponseEntity.ok(ApiResponse.failure("搜索失败"));
        }
    }

    @GetMapping("/jd/priceHistory")
    public ResponseEntity<byte[]> getJDPriceHistory(@RequestParam String skuId) {
        try {
            // 初始化 WebDriver
            priceHistoryService.initDriver();
            // 获取价格历史截图
            byte[] screenshot = priceHistoryService.getJDPriceHistory(skuId);
            priceHistoryService.quit();
            // 返回图像数据并设置 Content-Type 为 image/png
            return ResponseEntity.ok()
                    .header("Content-Type", "image/png")
                    .body(screenshot);
        } catch (Exception e) {
            e.printStackTrace(); // 记录异常日志
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null); // 返回空响应，前端根据状态码处理
        } finally {
            priceHistoryService.quit();
        }
    }

    @PostMapping("/taobao/login")
    public ResponseEntity<ApiResponse<Boolean>> loginAndSaveTaobaoCookies(@RequestParam String username, @RequestParam String password) {
        try {
            taobaoSpiderService.initDriver();
            taobaoSpiderService.loginAndSaveCookies(taobaoLoginUrl, username, password);
            return ResponseEntity.ok(ApiResponse.success(true));
        } catch (IOException e) {
            return ResponseEntity.ok(ApiResponse.failure("登录失败"));
        }
    }

    @GetMapping("/taobao/search")
    public ResponseEntity<ApiResponse<List<Map<String, String>>>> fetchTaobaoProducts(@RequestParam String searchKeyWord, @RequestParam String username, @RequestParam String password) {
        try{
            String url = "http://item-service/items/spider";
            List<Map<String, String>> products = taobaoSpiderService.fetchProductsWithLogin(taobaoLoginUrl, taobaoSearchUrl + searchKeyWord, username, password);
            System.out.println("Done");
            ResponseEntity<ApiResponse<Boolean>> response = restTemplate.exchange(
                url,                           // 请求 URL
                HttpMethod.POST,               // 请求方法
                new HttpEntity<>(products),    // 请求体
                new ParameterizedTypeReference<ApiResponse<Boolean>>() {}  // 响应体类型
            );
            System.out.println("Done");
            taobaoSpiderService.quit();
            return ResponseEntity.ok(ApiResponse.success(products));
        } catch (Exception e) {
            taobaoSpiderService.quit();
            return ResponseEntity.ok(ApiResponse.failure("搜索失败"));
        }
    }

    @GetMapping("/taobao/name")
    public ResponseEntity<ApiResponse<Map<String, String>>> fetchTaobaoProductByName(@RequestParam String name, @RequestParam String username, @RequestParam String password) {
        try{
            System.out.println("Name");
            Map<String, String> product = taobaoSpiderService.fetchProductWithLogin(taobaoLoginUrl, taobaoSearchUrl + name, username, password);
            System.out.println("Done");
            taobaoSpiderService.quit();
            return ResponseEntity.ok(ApiResponse.success(product));
        } catch (Exception e) {
            taobaoSpiderService.quit();
            return ResponseEntity.ok(ApiResponse.failure("搜索失败"));
        }
    }

    @GetMapping("/taobao/priceHistory")
    public ResponseEntity<byte[]> getTaobaoPriceHistory(@RequestParam String url) {
        try {
            // 初始化 WebDriver
            priceHistoryService.initDriver();
            // 获取价格历史截图
            byte[] screenshot = priceHistoryService.getTaobaoPriceHistory(url);
            priceHistoryService.quit();
            // 返回图像数据并设置 Content-Type 为 image/png
            return ResponseEntity.ok()
                    .header("Content-Type", "image/png")
                    .body(screenshot);
        } catch (Exception e) {
            e.printStackTrace(); // 记录异常日志
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null); // 返回空响应，前端根据状态码处理
        } finally {
            priceHistoryService.quit();
        }
    }

}
