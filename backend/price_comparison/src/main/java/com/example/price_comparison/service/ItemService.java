package com.example.price_comparison.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.access.prepost.PreAuthorize;
import com.example.price_comparison.service.factory.CategoryServiceFactory;
import com.example.price_comparison.model.*;
import com.example.price_comparison.repository.ItemRepository;
import com.example.price_comparison.repository.PriceHistoryRepository;
import com.example.price_comparison.exception.custom.*;
import java.time.LocalDateTime;
import java.util.List;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

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
     * Get item by platform id
     * @param skuId platform id
     * @return Item found 
     * @throws ItemNotFoundException if item not found
     */
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public Item getItemBySkuId(String skuId) {
        return Optional.ofNullable(itemRepository.findBySkuId(skuId))
                .orElseThrow(() -> new ItemNotFoundException("Item not found"));
    }
    
    /**
     * Query items by conditions
     * @param conditions user defined query conditions
     * @return List of items found
     * @throws ItemNotFoundException if no item found
     */
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public List<Item> queryItems(ItemQueryConditions conditions) {
        return Optional.ofNullable(itemRepository.findByConditionedQuery(
                    conditions.getName(),
                    conditions.getSkuId(), 
                    conditions.getCategoryId(),
                    conditions.getMaxPrice(),
                    conditions.getMinPrice(),
                    conditions.getPlatforms(),
                    conditions.getSortBy(),
                    conditions.isAscending()
                ))
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
    public Item updateItemByName(String name, Item item) {
        Item foundItem = Optional.ofNullable(itemRepository.findByName(name))
            .orElseThrow(() -> new ItemNotFoundException("Item not found with name: " + name));
        updatePriceHistoryIfChanged(foundItem, item);
        updateItemFields(foundItem, item);
        return itemRepository.save(foundItem);
    }

    /**
     * Update item by platform id
     * @param skuId
     * @param item 
     * @return Item updated
     * @throws ItemNotFoundException if item not found
     */
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public Item updateItemBySkuId(String skuId, Item item) {
        Item foundItem = Optional.ofNullable(itemRepository.findBySkuId(skuId))
            .orElseThrow(() -> new ItemNotFoundException("Item not found with SKU ID: " + skuId));
        updatePriceHistoryIfChanged(foundItem, item);
        updateItemFields(foundItem, item);
        return itemRepository.save(foundItem);
    }
    
    /**
     * Delete item by name
     * @param name
     * @return Item deleted
     * @throws ItemNotFoundException if item not found
     */
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public void deleteItem(String name) {
        Item item = itemRepository.findByName(name);
        itemRepository.delete(item);
    }
    
    /**
     * Check if item exists by name
     * @param name
     * @return boolean true if item exists
     */
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public boolean itemExistsByName(String name) {
        return itemRepository.existsByName(name);
    }

    /**
     * Check if item exists by platform id
     * @param skuId
     * @return boolean true if item exists
     */
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public boolean itemExistsBySkuId(String skuId) {
        return itemRepository.existsBySkuId(skuId);
    }

    /**
     * Get price history by item id
     * @param itemId 
     * @return List of price history found
     * @throws PriceHistoryNotFoundException if no price history found
     */
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public List<PriceHistory> getPriceHistoryById(int itemId) {
        return Optional.ofNullable(priceHistoryRepository.findByItemId(itemId))
                .orElseThrow(() -> new PriceHistoryNotFoundException("Price History not found for: " + itemId));
    }
    
    /**
     * Get price history by item
     * @param item
     * @return List of price history found
     * @throws PriceHistoryNotFoundException if no price history found
     */
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public List<PriceHistory> getPriceHistoryById(Item item) {
        return Optional.ofNullable(priceHistoryRepository.findByItemId(item.getId()))
                .orElseThrow(() -> new PriceHistoryNotFoundException("Price History not found for: " + item.getName()));
    }
    
    /**
     * Query price history by conditions
     * @param conditions
     * @return List of price history found
     * @throws PriceHistoryNotFoundException if no price history found
     */
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public List<PriceHistory> queryPriceHistory(PriceHistoryQueryConditions condition) {
        return Optional.ofNullable(priceHistoryRepository.findByConditionedQuery(
                    condition.getItemId(),
                    condition.getPlatform(),
                    condition.getMaxPrice(),
                    condition.getMinPrice(),
                    condition.getStartDate(),
                    condition.getEndDate()
                ))
                .orElseThrow(() -> new PriceHistoryNotFoundException("Price History not found under such query"));
    }
    
    /**
     * Update price history if price changed
     * @param foundItem item found
     * @param newItem item to be updated
     */
    private void updatePriceHistoryIfChanged(Item foundItem, Item newItem) {
        if (newItem.getPrice() != (foundItem.getPrice())) {
            PriceHistory priceHistory = new PriceHistory();
            priceHistory.setItemId(foundItem.getId());
            priceHistory.setPrice(newItem.getPrice());
            priceHistory.setTimestamp(LocalDateTime.now().truncatedTo(ChronoUnit.MICROS));
            priceHistoryRepository.save(priceHistory);
        }
    }
    
    /**
     * Update item fields
     * @param foundItem item found
     * @param newItem item to be updated
     */
    private void updateItemFields(Item foundItem, Item newItem) {
        if (newItem.getName() != null) {
            foundItem.setName(newItem.getName());
        }
        if (newItem.getCategoryId() != 0) {
            foundItem.setCategoryId(newItem.getCategoryId());
        }
        if (newItem.getPlatform() != null) {
            foundItem.setPlatform(newItem.getPlatform());
        }
        if (newItem.getImage() != null) {
            foundItem.setImage(newItem.getImage());
        }
        if (newItem.getUrl() != null) {
            foundItem.setUrl(newItem.getUrl());
        }
        if (newItem.getPrice() != 0) {
            foundItem.setPrice(newItem.getPrice());
        }
    }
}