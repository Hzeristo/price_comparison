package com.haydenshui.pricecomparison.favorite;

import com.haydenshui.pricecomparison.shared.model.*;
import com.haydenshui.pricecomparison.shared.util.ApiResponse;
import com.haydenshui.pricecomparison.shared.util.validation.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/favorite")
public class FavoriteController {
    
    private final FavoriteService favoriteService;
    
    @Autowired
    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }
    
    @PostMapping("/new")
    public ResponseEntity<ApiResponse<Favorite>> createFavorite(@Valid @RequestBody Favorite favorite) {
        Favorite createdFavorite = favoriteService.createFavorite(favorite);
        return ResponseEntity.ok(ApiResponse.success(createdFavorite));
    }

    @GetMapping("/user")
    public ResponseEntity<ApiResponse<List<Favorite>>> getUserFavorite(@RequestParam @NotNull int userId) {
        List<Favorite> userFavorites = favoriteService.getFavoritesByUserId(userId);
        return ResponseEntity.ok(ApiResponse.success(userFavorites));
    }

    @GetMapping("/item")
    public ResponseEntity<ApiResponse<List<Favorite>>> getItemFavorite(@RequestParam @NotNull int itemId) {
        List<Favorite> itemFavorites = favoriteService.getFavoritesByItemId(itemId);
        return ResponseEntity.ok(ApiResponse.success(itemFavorites));
    }
    
    @PutMapping("/update")
    public ResponseEntity<ApiResponse<Favorite>> updateFavorite(
            @RequestParam @NotNull int userId,
            @RequestParam @NotNull int itemId,
            @RequestBody Map<String, Object> updates) {
        Favorite updatedFavorite = favoriteService.updateFavorite(userId, itemId, updates);
        return ResponseEntity.ok(ApiResponse.success(updatedFavorite));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse<Void>> deleteFavorite(
            @RequestParam @NotNull int userId,
            @RequestParam @NotNull int itemId) {
        favoriteService.deleteFavorite(userId, itemId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
