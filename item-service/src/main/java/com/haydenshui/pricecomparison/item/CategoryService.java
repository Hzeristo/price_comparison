package com.haydenshui.pricecomparison.item;

import com.haydenshui.pricecomparison.shared.exception.custom.*;
import com.haydenshui.pricecomparison.shared.model.Category;
import com.haydenshui.pricecomparison.shared.model.CategoryDict;
import com.haydenshui.pricecomparison.shared.model.Platform;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Service
public class CategoryService {
    
    @Autowired
    private CategoryRepository categoryRepository;

    /**
     * Get a category by its id
     * @param id
     * @return Category
     * @throws ResourceNotFoundException if category does not exist
     */
    public Category getCategoryById(int id) {
        return Optional.ofNullable(categoryRepository.findById(id))
            .orElseThrow(() -> new ResourceNotFoundException("Category with id " + id + " not found", "category"));
    }

    /**
     * Get a category by its name
     * @param name 
     * @return Category
     */
    public Category getCategoryByName(String name) {
        return Optional.ofNullable(categoryRepository.findByName(name))
            .orElseThrow(() -> new ResourceNotFoundException("Category with name " + name + " not found", "category"));
    }

    /**
     * Get the parent category of a category
     * @param name
     * @return Category
     */
    public Category getCategoryParent(String name) {
        return Optional.ofNullable(getCategoryByName(name).getParent())
            .orElseThrow(() -> new ResourceNotFoundException("Parent of category with name " + name + " not found", "category"));
    }
    
    /**
     * Not implemented: Get the child category of a category
     * @param name
     * @return Category
     */
    public Category getCategoryChild(String name) {
        throw new NotImplementedException("not implemented: getCategoryChild()");
    }

    /**
     * Check if a category exists by its name
     * @param name
     * @return boolean
     */
    public boolean existsByName(String name) {
        return categoryRepository.existsByName(name);
    }

    /**
     * Print all categories
     */
    public void printAllCategories() {
        System.out.println("All categories:");
        categoryRepository.findAll().forEach(System.out::println);
    }
}
