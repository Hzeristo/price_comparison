package com.example.price_comparison.service;

import com.example.price_comparison.exception.custom.*;
import com.example.price_comparison.repository.CategoryRepository;
import com.example.price_comparison.model.Category;
import com.example.price_comparison.model.CategoryDict;
import com.example.price_comparison.model.Platform;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.access.prepost.PreAuthorize;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Optional;


@Service
public class CategoryService {
    
    private String pathname;
    
    @Autowired
    private final CategoryRepository categoryRepository;
    
    private final CategoryDict categoryDict;
    
    public CategoryService(CategoryRepository categoryRepository, String pathname) {
        this.categoryRepository = categoryRepository;
        this.categoryDict = loadCategoryDict(pathname);
    }

    /**
     * load dictionary from local dictionary file
     * @param filePath
     * @return CategoryDict from file
     */
    private CategoryDict loadCategoryDict(String filePath) {
        CategoryDict dict = new CategoryDict();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(filePath)))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    dict.addMapping(parts[0].trim(), parts[1].trim());
                }
            }
        } catch (IOException e) {
            throw new DictionaryNotInitializedException("Failed to initialize dictionary for platform: " + categoryDict.getPlatform().getName(), categoryDict.getPlatform());
        }
        return dict;
    }

    /**
     * Get the category dictionary
     * @return CategoryDict
     */
    public CategoryDict getCategoryDict() {
        return categoryDict;
    }
    
    /**
     * Create a new category
     * deprecated for static use of category dictionary
     * @param category
     * @return Category
     * @throws CategoryAlreadyExistsException if category already exists
     */
    @Deprecated
    @PreAuthorize("hasRole('ADMIN')")
    public Category createCategory(Category category) {
        if (categoryRepository.existsByName(category.getName())) {
            throw new DuplicateResourceException("Category with name " + category.getName() + " already exists", "category");
        }
        return categoryRepository.save(category);
    }

    /**
     * Get a category by its id
     * @param id
     * @return Category
     * @throws CategoryNotFoundException if category does not exist
     */
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public Category getCategoryById(int id) {
        return Optional.ofNullable(categoryRepository.findById(id))
            .orElseThrow(() -> new CategoryNotFoundException("Category with id " + id + " not found"));
    }

    /**
     * Get a category by its name
     * @param name 
     * @return Category
     */
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public Category getCategoryByName(String name) {
        return Optional.ofNullable(categoryRepository.findByName(name))
            .orElseThrow(() -> new CategoryNotFoundException("Category with name " + name + " not found"));
    }

    /**
     * Get the parent category of a category
     * @param name
     * @return Category
     */
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public Category getCategoryParent(String name) {
        return Optional.ofNullable(getCategoryByName(name).getParent())
            .orElseThrow(() -> new CategoryNotFoundException("Parent of category with name " + name + " not found"));
    }
    
    /**
     * Not implemented: Get the child category of a category
     * @param name
     * @return Category
     */
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public Category getCategoryChild(String name) {
        throw new NotImplementedException("not implemented: getCategoryChild()");
    }

    /**
     * Check if a category exists by its name
     * @param name
     * @return boolean
     */
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public boolean existsByName(String name) {
        return categoryRepository.existsByName(name);
    }

    /**
     * Print all categories
     */
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public void printAllCategories() {
        System.out.println("All categories:");
        categoryRepository.findAll().forEach(System.out::println);
    }
}
