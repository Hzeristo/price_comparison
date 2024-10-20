package com.example.price_comparison.service.factory;

import com.example.price_comparison.service.CategoryService;
import com.example.price_comparison.service.categories.JDCategoryService;
import com.example.price_comparison.service.categories.OtherPlatformCategoryService; // 其他平台服务的示例
import com.example.price_comparison.model.Platform;
import com.example.price_comparison.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class CategoryServiceFactory {

    @Autowired
    private CategoryRepository categoryRepository;

    private final Map<String, String> dictionaryPaths = new HashMap<>();

    public CategoryServiceFactory() {
        // 初始化字典路径映射
        dictionaryPaths.put("jd", "/dictionary/jdDict.csv");
        //
    }

    public CategoryService createCategoryService(String platform) {
        String dictionaryPath = getDictionaryPath(platform);
        return new CategoryService(categoryRepository, dictionaryPath);
    }

    private String getDictionaryPath(String platform) {
        String path = dictionaryPaths.get(platform.toLowerCase());
        if (path == null) {
            throw new IllegalArgumentException("Unsupported platform for creating dictionary: " + platform);
        }
        return path;
    }
}
