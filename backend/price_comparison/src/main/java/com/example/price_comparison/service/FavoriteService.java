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

    // 添加收藏
    public Favorite addFavorite(Favorite favorite) {
        return favoriteRepository.save(favorite);
    }

    // 删除收藏
    public void removeFavorite(Favorite favorite) {
        favoriteRepository.delete(favorite);
    }

    public void removeFavoriteById(int id) {
        favoriteRepository.deleteById(id);
    }

    public boolean existsById(Favorite favorite) {
        return favoriteRepository.existsById(favorite.getId());
    }

    public boolean existsById(int id) {
        return favoriteRepository.existsById(id);
    }

    public boolean existsByUser(Favorite favorite) {
        return favoriteRepository.existsByUser(favorite.getUser_id());
    }

    public List<Favorite> findByUserId(int user_id) {
        return favoriteRepository.findByUserId(user_id);
    }
}
