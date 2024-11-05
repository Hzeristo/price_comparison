package com.haydenshui.pricecomparison.item;

import com.haydenshui.pricecomparison.shared.model.Item;
import com.haydenshui.pricecomparison.shared.model.Platform;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {

    /**
     * Get an item by its ID.
     * 
     * @param id The ID of the item to find.
     * @return The item with the given ID, or null if no such item exists.
     */
    Item findById(int id);

    /**
     * Get an item by its platform ID.
     * 
     * @param skuId The platform ID of the item to find.
     * @return The item with the given platform ID, or null if no such item exists.
     */
    Item findBySkuId(String skuId);
    
    /**
     * Get an item by its name.
     * 
     * @param name The name of the item to find.
     * @return The item with the given name, or null if no such item exists.
     */
    Item findByName(String name);
    
    /**
     * Get all items.
     * 
     * @return A list of all items.
     */
    @Override
    List<Item> findAll();

    /**
     * Find items whose names contain the specified string.
     * 
     * @param name The string to search for in item names.
     * @return A list of items whose names contain the specified string.
     */
    List<Item> findByNameContaining(String name);

    /**
     * Find items by platform.
     * 
     * @param platform The platform of the items to find.
     * @return A list of items associated with the specified platform.
     */
    List<Item> findByPlatform(String platform);

    /**
     * Find items by category ID.
     * 
     * @param categoryId The category ID of the items to find.
     * @return A list of items belonging to the specified category.
     */
    List<Item> findByCategoryId(int categoryId);

    /**
     * Find items based on multiple query conditions.
     * 
     * @param name The name of the item to find.
     * @param categoryId The category ID of the items to find.
     * @param maxPrice The maximum price of the items to find.
     * @param minPrice The minimum price of the items to find.
     * @param platforms The platforms of the items to find.
     * @param sortBy The field to sort by.
     * @param ascending Whether to sort in ascending order.
     * @return A list of items that match the given query conditions.
     */
    @Query("SELECT i FROM Item i WHERE " +
        "(:skuId IS NULL OR i.sku_id = :skuId)" + 
        "(:name IS NULL OR i.name LIKE %:name%) AND " +
        "(:categoryId IS NULL OR i.category_id = :categoryId) AND " +
        "(:maxPrice IS NULL OR i.price <= :maxPrice) AND " +
        "(:minPrice IS NULL OR i.price >= :minPrice) AND " +
        "(:platforms IS NULL OR i.platform IN :platforms) " +
        "ORDER BY " +
        "CASE WHEN :sortBy = 'price' THEN i.price END " +
        "ASC :ascending")
    List<Item> findByConditionedQuery(
        @Param("name") String name,
        @Param("skuId") String skuId,
        @Param("categoryId") Integer categoryId,
        @Param("maxPrice") double maxPrice,
        @Param("minPrice") double minPrice,
        @Param("platforms") List<String> platforms,
        @Param("sortBy") String sortBy,
        @Param("ascending") boolean ascending
    );

    /**
     * Check if an item exists by its name.
     * 
     * @param name The name of the item to check.
     * @return True if an item with the given name exists, false otherwise.
     */
    boolean existsByName(String name);

    /**
     * Check if an item exists by its platform ID.
     * @param skuId The platform ID of the item to check.
     * @return True if an item with the given platform ID exists, false otherwise.
     */
    boolean existsBySkuId(String skuId);
}
