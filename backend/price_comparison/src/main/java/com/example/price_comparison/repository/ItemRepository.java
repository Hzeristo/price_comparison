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
     * @param name The name of the item to find.
     * @param categoryId The category id of the item to find.
     * @param maxPrice The maximum price of the item to find.
     * @param minPrice The minimum price of the item to find.
     * @param platforms The platforms of the item to find.
     * @param sortBy The field to sort by.
     * @param ascending Whether to sort in ascending order or not.
     * @return The item with the given query condition, or null if no such item exists.
     */
    @Query("SELECT i FROM Item i WHERE " +
        "(:name IS NULL OR i.name LIKE %:name%) AND " +
        "(:categoryId IS NULL OR i.categoryId = :categoryId) AND " +
        "(:maxPrice IS NULL OR i.price <= :maxPrice) AND " +
        "(:minPrice IS NULL OR i.price >= :minPrice) AND " +
        "(:platforms IS NULL OR i.platform IN :platforms) " +
        "ORDER BY CASE WHEN :sortBy = 'price' THEN i.price END " +
        "ORDER BY CASE WHEN :ascending THEN 1 ELSE 0 END")
    List<Item> findByConditionedQuery(@Param("name") String name, 
                                    @Param("categoryId") Integer categoryId, 
                                    @Param("maxPrice") Double maxPrice, 
                                    @Param("minPrice") Double minPrice, 
                                    @Param("platforms") List<Platform> platforms, 
                                    @Param("sortBy") String sortBy, 
                                    @Param("ascending") boolean ascending);
    List<Item> itemExistsByName(String name);
}
