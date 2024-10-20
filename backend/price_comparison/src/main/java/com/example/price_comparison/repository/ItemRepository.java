package com.example.price_comparison.repository;

import com.example.price_comparison.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {

    /**
     * Finds a user by their username.
     *
     * @return The user with the given username, or null if no such user exists.
     */
    List<Item> findAll();

    /**
     * Find an item by its name.
     * 
     * @param name The name of the item to find.
     * @return The item with the given name, or null if no such item exists.
     */
    List<Item> findByName(String name);

    /**
     * Find an item by its id.
     * 
     * @param id The id of the item to find.
     * @return The item with the given id, or null if no such item exists.
     */
    List<Item> findByNameContaining(String name);

    /**
     * Find an item by its platform.
     * @param platform The platform of the item to find.
     * @return The item with the given platform, or null if no such item exists.
     */
    List<Item> findByPlatform(String platform);

    /**
     * Find an item by its category id.
     * @param categoryId The category id of the item to find.
     * @return The item with the given category id, or null if no such item exists.
     */
    List<Item> findByCategoryId(Integer categoryId);
    
     /**
     * Find an item by query condition.
     * @param condition The query condition
     * @return The item with the given query condition, or null if no such item exists.
     */
    //List<Item> findByConditionedQuery(ItemQueryConditions condition);
}
