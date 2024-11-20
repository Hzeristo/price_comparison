package com.example.price_comparison.service.factory;

import com.example.price_comparison.service.CategoryService;
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

    @Autowired
    private Map<String, String> dictionaryPaths;

    public CategoryService createCategoryService(Platform platform) {
        String dictionaryPath = getDictionaryPath(platform);
        return new CategoryService(categoryRepository, dictionaryPath);
    }

    private String getDictionaryPath(Platform platform) {
        String path = dictionaryPaths.get(platform.getName());
        if (path == null) {
            throw new IllegalArgumentException("Unsupported platform for creating dictionary: " + platform);
        }
        return path;
    }
}