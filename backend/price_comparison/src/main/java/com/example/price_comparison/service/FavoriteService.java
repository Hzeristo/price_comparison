package com.example.price_comparison.service;

import com.example.price_comparison.model.Favorite;
import com.example.price_comparison.repository.FavoriteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavoriteService {

    @Autowired
    private FavoriteRepository favoriteRepository;

    /**
     * Add a new favorite item
     * @param favorite
     * @return Favorite object added
     */
    public Favorite addFavorite(Favorite favorite) {
        return favoriteRepository.save(favorite);
    }

    /**
     * Remove a favorite item
     * @param favorite
     */
    public void removeFavorite(Favorite favorite) {
        favoriteRepository.delete(favorite);
    }
    
    /**
     * Remove a favorite item by id
     * @param id
     */
    public void removeFavoriteById(int id) {
        favoriteRepository.deleteById(id);
    }
    
    /**
     * check whether an item is in a user's favorite list
     * @param itemId
     * @param userId
     * @return 
     */
    public boolean existsInUserFavorites(int itemId, int userId) {
        List<Favorite> favors = favoriteRepository.findByUserId(userId);
        return favors.stream()
                      .anyMatch(favorite -> favorite.getItem_id() == itemId);
    }

    /**
     * Get all favorite items of a user by user_id
     * @param user_id
     * @return List of Favorite objects
     */
    public List<Favorite> findByUserId(int userId) {
        return favoriteRepository.findByUserId(userId);
    }
}
