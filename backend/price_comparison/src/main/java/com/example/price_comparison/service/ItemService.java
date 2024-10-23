package com.example.price_comparison.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.price_comparison.service.factory.CategoryServiceFactory;
import com.example.price_comparison.model.*;
import com.example.price_comparison.repository.ItemRepository;
import com.example.price_comparison.repository.PriceHistoryRepository;
import com.example.price_comparison.exception.custom.*;

@Service
public class ItemService {
    
    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private CategoryServiceFactory categoryServiceFactory;

    @Autowired
    private PriceHistoryRepository priceHistoryRepository;

    /**
     * Create a new item
     * @param item
     * @return Item created
     * 
     */
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @Transactional
    public Item createItem(Item item) {
        return itemRepository.save(item);
    }

    /**
     * Get item by id
     * @param id
     * @return Item found
     * @throws ItemNotFoundException if item not found
     */
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public Item getItemById(int id) {
        return Optional.ofNullable(itemRepository.findById(id))
                .orElseThrow(() -> new ItemNotFoundException("Item not found"));
    }
    
    /**
     * Query items by conditions
     * @param conditions
     * @return List of items found
     * @throws ItemNotFoundException if no item found
     */
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public List<Item> queryItems(ItemQueryConditions conditions) {
        return Optional.ofNullable(itemRepository.findByConditionedQuery(conditions))
                .orElseThrow(() -> new ItemNotFoundException("Items not found under such query"));
    }
    
    /**
     * Update item by name
     * @param name
     * @param item 
     * @return Item updated
     * @throws ItemNotFoundException if item not found
     */
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public Item updateItem(String name, Item item) {
        Item foundItem = getItemByName(name);
        if (foundItem.getPrice() != item.getPrice()) {
            PriceHistory priceHistory = new PriceHistory();
            priceHistory.setItemId(foundItem.getId());
            priceHistory.setPrice(foundItem.getPrice());
            priceHistory.setTimestamp(LocalDateTime.now());
            priceHistoryRepository.save(priceHistory);
        }
        foundItem.setName(item.getName());
        foundItem.setCategoryId(item.getCategoryId());
        foundItem.setPlatform(item.getPlatform());
        foundItem.setImage(item.getImage());
        foundItem.setUrl(item.getUrl());
        foundItem.setPrice(item.getPrice());
        return itemRepository.save(foundItem);
    }
    
    /**
     * Delete item by name
     * @param name
     * @return Item deleted
     * @throws ItemNotFoundException if item not found
     */
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public Item deleteItem(String name) {
        Item item = getItemByName(name);
        return itemRepository.delete(item);
    }
    
    /**
     * Check if item exists by name
     * @param name
     * @return boolean true if item exists
     */
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public Item itemExistsByName(String name) {
        return itemRepository.itemExistsByName(name);
    }
    
    /**
     * Query items by conditions
     * @param conditions
     * @return List of items found
     */
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public List<Item> queryItems(ItemQueryConditions conditions) {
        return itemRepository.findByConditionedQuery(
            conditions.getName(),
            conditions.getCategoryId(),
            conditions.getMaxPrice(),
            conditions.getMinPrice(),
            conditions.getPlatforms(),
            conditions.getSortBy(),
            conditions.isAscending()
        );
    }

    
}