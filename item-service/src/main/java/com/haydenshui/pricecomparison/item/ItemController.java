package com.haydenshui.pricecomparison.item;

import com.haydenshui.pricecomparison.shared.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/items")
public class ItemController {

    @Autowired
    private ItemService itemService;

    @Autowired
    private ItemMessageProvider itemMessageProvider;

    @Autowired
    private ItemMessageConsumer itemMessageConsumer;

    /**
     * Create a new item
     * @param item Item to be created
     * @return ResponseEntity with the created item
     */
    @PostMapping("/new")
    public ResponseEntity<Item> createItem(@RequestBody Item item) {
        Item createdItem = itemService.createItem(item);
        return ResponseEntity.ok(createdItem);
    }

    /**
     * Get item by id
     * @param id Item ID
     * @return ResponseEntity with the found item
     */
    @GetMapping("/{id}")
    public ResponseEntity<Item> getItemById(@PathVariable int id) {
        Item item = itemService.getItemById(id);
        return ResponseEntity.ok(item);
    }

    /**
     * Get item by platform id
     * @param skuId Platform SKU ID
     * @return ResponseEntity with the found item
     */
    @GetMapping("/sku/{skuId}")
    public ResponseEntity<Item> getItemBySkuId(@PathVariable String skuId) {
        Item item = itemService.getItemBySkuId(skuId);
        return ResponseEntity.ok(item);
    }

    /**
     * Query items by conditions
     * @param conditions Query conditions
     * @return ResponseEntity with the list of found items
     */
    @PostMapping("/query")
    public ResponseEntity<List<Item>> queryItems(@RequestBody ItemQueryConditions conditions) {
        List<Item> items = itemService.queryItems(conditions);
        itemMessageProvider.sendItems(items);
        return ResponseEntity.ok(items);
    }

    /**
     * Update item by name
     * @param name Item name
     * @param item Item data to update
     * @return ResponseEntity with the updated item
     */
    @PutMapping("/name/{name}")
    public ResponseEntity<Item> updateItemByName(@PathVariable String name, @RequestBody Item item) {
        Item updatedItem = itemService.updateItemByName(name, item);
        return ResponseEntity.ok(updatedItem);
    }

    /**
     * Update item by platform id
     * @param skuId Platform SKU ID
     * @param item Item data to update
     * @return ResponseEntity with the updated item
     */
    @PutMapping("/sku/{skuId}")
    public ResponseEntity<Item> updateItemBySkuId(@PathVariable String skuId, @RequestBody Item item) {
        Item updatedItem = itemService.updateItemBySkuId(skuId, item);
        return ResponseEntity.ok(updatedItem);
    }

    /**
     * Delete item by name
     * @param name Item name
     * @return ResponseEntity indicating success
     */
    @DeleteMapping("/name/{name}")
    public ResponseEntity<Void> deleteItem(@PathVariable String name) {
        itemService.deleteItem(name);
        return ResponseEntity.noContent().build();
    }

    /**
     * Check if item exists by name
     * @param name Item name
     * @return ResponseEntity with existence status
     */
    @GetMapping("/exists/name/{name}")
    public ResponseEntity<Boolean> itemExistsByName(@PathVariable String name) {
        boolean exists = itemService.itemExistsByName(name);
        return ResponseEntity.ok(exists);
    }

    /**
     * Check if item exists by platform id
     * @param skuId Platform SKU ID
     * @return ResponseEntity with existence status
     */
    @GetMapping("/exists/sku/{skuId}")
    public ResponseEntity<Boolean> itemExistsBySkuId(@PathVariable String skuId) {
        boolean exists = itemService.itemExistsBySkuId(skuId);
        return ResponseEntity.ok(exists);
    }

    /**
     * Get price history by item id
     * @param itemId Item ID
     * @return ResponseEntity with the list of price history
     */
    @GetMapping("/{itemId}/price-history")
    public ResponseEntity<List<PriceHistory>> getPriceHistoryById(@PathVariable int itemId) {
        List<PriceHistory> priceHistory = itemService.getPriceHistoryById(itemId);
        return ResponseEntity.ok(priceHistory);
    }

    /**
     * Query price history by conditions
     * @param conditions Query conditions
     * @return ResponseEntity with the list of price history found
     */
    @PostMapping("/price-history/query")
    public ResponseEntity<List<PriceHistory>> queryPriceHistory(@RequestBody PriceHistoryQueryConditions conditions) {
        List<PriceHistory> priceHistory = itemService.queryPriceHistory(conditions);
        return ResponseEntity.ok(priceHistory);
    }
}
