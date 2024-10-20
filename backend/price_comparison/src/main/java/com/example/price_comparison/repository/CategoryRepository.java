package com.example.price_comparison.repository;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    
    /**
     * Find category by name
     * 
     * @param name Category name to search
     * @return Category object if found, null otherwise
     */
    Category findByName(String name);
    
    /**
     * Check if category with given name exists
     * 
     * @param name Category name to check
     * @return true if category exists, false otherwise
     */
    boolean existsByName(String name);
}
