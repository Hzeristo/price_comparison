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
     * Create a new item.
     * 
     * @param item The item to create.
     * @return The created item.
     */
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @Transactional
    public Item createItem(Item item) {
        return itemRepository.save(item);
    }

    /**
     * Get item by id.
     * 
     * @param id The id of the item to retrieve.
     * @return The found item.
     * @throws ItemNotFoundException if the item is not found.
     */
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @Transactional(readOnly = true)
    public Item getItemById(int id) {
        return Optional.ofNullable(itemRepository.findById(id))
                .orElseThrow(() -> new ItemNotFoundException("Item not found"));
    }

    /**
     * Get item by platform id (SKU ID).
     * 
     * @param skuId The SKU ID of the item to retrieve.
     * @return The found item.
     * @throws ItemNotFoundException if the item is not found.
     */
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @Transactional(readOnly = true)
    public Item getItemBySkuId(String skuId) {
        return Optional.ofNullable(itemRepository.findBySkuId(skuId))
                .orElseThrow(() -> new ItemNotFoundException("Item not found"));
    }

    /**
     * Query items by specified conditions.
     * 
     * @param conditions The user-defined query conditions.
     * @return A list of items found matching the conditions.
     * @throws ItemNotFoundException if no items are found.
     */
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @Transactional(readOnly = true)
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
     * Update item by name.
     * 
     * @param name The name of the item to update.
     * @param item The new item details.
     * @return The updated item.
     * @throws ItemNotFoundException if the item is not found.
     */
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @Transactional
    public Item updateItemByName(String name, Item item) {
        Item foundItem = Optional.ofNullable(itemRepository.findByName(name))
            .orElseThrow(() -> new ItemNotFoundException("Item not found with name: " + name));
        updatePriceHistoryIfChanged(foundItem, item);
        updateItemFields(foundItem, item);
        return itemRepository.save(foundItem);
    }

    /**
     * Update item by platform id (SKU ID).
     * 
     * @param skuId The SKU ID of the item to update.
     * @param item The new item details.
     * @return The updated item.
     * @throws ItemNotFoundException if the item is not found.
     */
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @Transactional
    public Item updateItemBySkuId(String skuId, Item item) {
        Item foundItem = Optional.ofNullable(itemRepository.findBySkuId(skuId))
            .orElseThrow(() -> new ItemNotFoundException("Item not found with SKU ID: " + skuId));
        updatePriceHistoryIfChanged(foundItem, item);
        updateItemFields(foundItem, item);
        return itemRepository.save(foundItem);
    }
    
    /**
     * Delete item by name.
     * 
     * @param name The name of the item to delete.
     * @throws ItemNotFoundException if the item is not found.
     */
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @Transactional
    public void deleteItem(String name) {
        Item item = itemRepository.findByName(name);
        itemRepository.delete(item);
    }
    
    /**
     * Check if an item exists by name.
     * 
     * @param name The name of the item to check.
     * @return true if the item exists, false otherwise.
     */
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @Transactional(readOnly = true)
    public boolean itemExistsByName(String name) {
        return itemRepository.existsByName(name);
    }

    /**
     * Check if an item exists by platform id (SKU ID).
     * 
     * @param skuId The SKU ID of the item to check.
     * @return true if the item exists, false otherwise.
     */
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @Transactional(readOnly = true)
    public boolean itemExistsBySkuId(String skuId) {
        return itemRepository.existsBySkuId(skuId);
    }

    /**
     * Get price history by item id.
     * 
     * @param itemId The id of the item.
     * @return A list of price history found.
     * @throws PriceHistoryNotFoundException if no price history is found.
     */
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @Transactional(readOnly = true)
    public List<PriceHistory> getPriceHistoryById(int itemId) {
        return Optional.ofNullable(priceHistoryRepository.findByItemId(itemId))
                .orElseThrow(() -> new PriceHistoryNotFoundException("Price History not found for: " + itemId));
    }
    
    /**
     * Get price history by item.
     * 
     * @param item The item to retrieve price history for.
     * @return A list of price history found.
     * @throws PriceHistoryNotFoundException if no price history is found.
     */
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @Transactional(readOnly = true)
    public List<PriceHistory> getPriceHistoryById(Item item) {
        return Optional.ofNullable(priceHistoryRepository.findByItemId(item.getId()))
                .orElseThrow(() -> new PriceHistoryNotFoundException("Price History not found for: " + item.getName()));
    }
    
    /**
     * Query price history by specified conditions.
     * 
     * @param condition The conditions to query price history.
     * @return A list of price history found matching the conditions.
     * @throws PriceHistoryNotFoundException if no price history is found.
     */
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @Transactional(readOnly = true)
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
     * Save a list of items in batch.
     * 
     * @param items The list of items to save.
     * @return The list of saved items.
     */
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @Transactional
    public List<Item> saveItems(List<Item> items) {
        return itemRepository.saveAll(items);
    }
    
    /**
     * Update price history if the price has changed.
     * 
     * @param foundItem The item found in the repository.
     * @param newItem The new item data to check against.
     */
    private void updatePriceHistoryIfChanged(Item foundItem, Item newItem) {
        if (newItem.getPrice() != foundItem.getPrice()) {
            PriceHistory priceHistory = new PriceHistory();
            priceHistory.setItemId(foundItem.getId());
            priceHistory.setPrice(newItem.getPrice());
            priceHistory.setTimestamp(LocalDateTime.now().truncatedTo(ChronoUnit.MICROS));
            priceHistoryRepository.save(priceHistory);
        }
    }
    
    /**
     * Update the fields of the found item with new item data.
     * 
     * @param foundItem The item found in the repository.
     * @param newItem The new item data to update.
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