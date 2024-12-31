package com.haydenshui.pricecomparison.item;

import com.haydenshui.pricecomparison.shared.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.haydenshui.pricecomparison.shared.util.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/items")
public class ItemController {

    @Autowired
    private ItemService itemService;

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

    @GetMapping("/test")
    public ResponseEntity<ApiResponse<String>> test() {
        String response = "Hello from Item Service!";
        System.out.println(response);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping("/spider")
    public ResponseEntity<ApiResponse<Boolean>> spiderItems(@RequestBody List<Map<String, String>> result) {
        System.out.println("Spider items");
        for(Map<String, String> item : result) {
            System.out.println("Spider item: " + item.get("name"));
            Item itemEntity = new Item();
            itemEntity.setSkuid(item.get("skuid"));

            itemEntity.setName(item.get("name"));
            itemEntity.setUrl(item.get("url"));
            System.out.println("Spider item url: " + item.get("url"));
            itemEntity.setImage(item.get("image"));
            System.out.println("Spider item image: " + item.get("image"));
            itemEntity.setPrice(Double.parseDouble(item.get("price")));
            itemEntity.setPlatform(item.get("platform"));
            itemEntity.setCategoryId(0);
            try {
                itemService.createItem(itemEntity);
            } catch (Exception e) {
                System.err.println("Error saving item: " + e.getMessage());
            }
        }
        System.out.println("Spider items done");
        return ResponseEntity.ok(ApiResponse.success(true));
    }

    /**
     * Get item by id
     * @param id Item ID
     * @return ResponseEntity with the found item
     */
    @GetMapping("/id")
    public ResponseEntity<Item> getItemById(@RequestParam int id) {
        Item item = itemService.getItemById(id);
        return ResponseEntity.ok(item);
    }

    /**
     * Get item by platform id
     * @param skuId Platform SKU ID
     * @return ResponseEntity with the found item
     */
    @GetMapping("/sku")
    public ResponseEntity<Item> getItemBySkuId(@RequestParam String skuId) {
        System.out.println("skuId: " + skuId);
        Item item = itemService.getItemBySkuId(skuId);
        return ResponseEntity.ok(item);
    }

    @GetMapping("/price")
    public ResponseEntity<Item> getItemByIdforPrice(@RequestParam int id) {
        Item item = itemService.getItemById(id);
        return ResponseEntity.ok(item);
    }



    /**
     * Query items by conditions
     * @param conditions Query conditions
     * @return ResponseEntity with the list of found items
     */
    /* @PostMapping("/query")
    public ResponseEntity<List<Item>> queryItems(@RequestBody ItemQueryConditions conditions) {
        List<Item> items = itemService.queryItems(conditions);
        return ResponseEntity.ok(items);
    } */

    /**
     * Update item by name
     * @param name Item name
     * @param item Item data to update
     * @return ResponseEntity with the updated item
     */
    @PutMapping("/name")
    public ResponseEntity<Item> updateItemByName(@RequestParam String name, @RequestBody Item item) {
        Item updatedItem = itemService.updateItemByName(name, item);
        return ResponseEntity.ok(updatedItem);
    }

    /**
     * Update item by platform id
     * @param skuId Platform SKU ID
     * @param item Item data to update
     * @return ResponseEntity with the updated item
     */
    @PutMapping("/sku")
    public ResponseEntity<Item> updateItemBySkuId(@RequestParam String skuId, @RequestBody Item item) {
        Item updatedItem = itemService.updateItemBySkuId(skuId, item);
        return ResponseEntity.ok(updatedItem);
    }

    /**
     * Delete item by name
     * @param name Item name
     * @return ResponseEntity indicating success
     */
    @DeleteMapping("/name")
    public ResponseEntity<Void> deleteItem(@RequestParam String name) {
        itemService.deleteItem(name);
        return ResponseEntity.noContent().build();
    }

    /**
     * Check if item exists by name
     * @param name Item name
     * @return ResponseEntity with existence status
     */
    @GetMapping("/exists/name")
    public ResponseEntity<Boolean> itemExistsByName(@RequestParam String name) {
        boolean exists = itemService.itemExistsByName(name);
        return ResponseEntity.ok(exists);
    }

    /**
     * Check if item exists by platform id
     * @param skuId Platform SKU ID
     * @return ResponseEntity with existence status
     */
    @GetMapping("/exists/sku")
    public ResponseEntity<Boolean> itemExistsBySkuId(@RequestParam String skuId) {
        boolean exists = itemService.itemExistsBySkuId(skuId);
        return ResponseEntity.ok(exists);
    }

    /**
     * Get price history by item id
     * @param itemId Item ID
     * @return ResponseEntity with the list of price history
     */
    @GetMapping("/pricehistory")
    public ResponseEntity<List<PriceHistory>> getPriceHistoryById(@RequestParam int itemId) {
        List<PriceHistory> priceHistory = itemService.getPriceHistoryById(itemId);
        return ResponseEntity.ok(priceHistory);
    }

    /**
     * Query price history by conditions
     * @param conditions Query conditions
     * @return ResponseEntity with the list of price history found
     */
    @PostMapping("/pricehistory/query")
    public ResponseEntity<List<PriceHistory>> queryPriceHistory(@RequestBody PriceHistoryQueryConditions conditions) {
        List<PriceHistory> priceHistory = itemService.queryPriceHistory(conditions);
        return ResponseEntity.ok(priceHistory);
    }

}
