package com.haydenshui.pricecomparison.favorite;

import com.haydenshui.pricecomparison.shared.model.*;
import com.haydenshui.pricecomparison.favorite.FavoriteRepository;
import com.haydenshui.pricecomparison.shared.exception.custom.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import jakarta.validation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * Service class for managing user-related operations.
 */
@Service
public class FavoriteService {

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private AlertBoundRepository alertBoundRepository;

    @Autowired
    private AlertFrequencyRepository alertFrequencyRepository;

    @Autowired
    private AlertMethodRepository alertMethodRepository;

    @Transactional
    public Favorite createFavorite(Favorite favorite) {
        validateForDuplicatedFavorites(favorite);
        return favoriteRepository.save(favorite);
    }

    @Transactional
    public AlertBound createAlertBound(AlertBound alertBound) {
        return alertBoundRepository.save(alertBound);
    }

    public List<Favorite> getFavoritesByUserId(int userId) {
        return Optional.ofNullable(favoriteRepository.findByUser_Id(userId))
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId, "Favorite"));
    }

    public List<Favorite> getFavoritesByItemId(int itemId) {
        return Optional.ofNullable(favoriteRepository.findByItem_Id(itemId))
                .orElseThrow(() -> new ResourceNotFoundException("Item not found with ID: " + itemId, "Favorite"));
    }

    public Favorite getFavorite(int userId, int itemId) {
        return Optional.ofNullable(favoriteRepository.findByUser_IdAndItem_Id(userId, itemId))
                .orElseThrow(() -> new ResourceNotFoundException("Favorite not found for user and item: " + itemId + " not in " + userId + "\'s favorites", "Favorite"));
    }
    
    @Transactional
    public Favorite updateFavorite(int userId, int itemId, Map<String, Object> updates) {
        Favorite existingFavorite = getFavorite(userId, itemId);
        Map<String, Consumer<Object>> fieldUpdaters = new HashMap<>();
        fieldUpdaters.put("user", value -> existingFavorite.setUser((User) value));
        fieldUpdaters.put("item", value -> existingFavorite.setItem((Item) value));
        fieldUpdaters.put("bounds", value -> {
            if (value != null) {
                List<AlertBound> newBounds = (List<AlertBound>) value;
                existingFavorite.setBounds(newBounds);
                alertBoundRepository.saveAll(newBounds);  // 同步更新 AlertFrequency
            }
        });
        fieldUpdaters.put("frequencies", value -> {
            if (value != null) {
                List<AlertFrequency> newFrequencies = (List<AlertFrequency>) value;
                existingFavorite.setFrequencies(newFrequencies);
                alertFrequencyRepository.saveAll(newFrequencies);  // 同步更新 AlertFrequency
            }
        });
        fieldUpdaters.put("methods", value -> {
            if (value != null) {
                List<AlertMethod> newMethods = (List<AlertMethod>) value;
                existingFavorite.setMethods(newMethods);
                alertMethodRepository.saveAll(newMethods);  // 同步更新 AlertMethod
            }
        });
        updates.forEach((field, value) -> {
            Consumer<Object> updater = fieldUpdaters.get(field);
            if (updater != null) {
                updater.accept(value);  // 执行字段更新
            }
        });
        return favoriteRepository.save(existingFavorite);
    }

    public boolean favoriteExists(int userId, int itemId) {
        return favoriteRepository.existsByUser_IdAndItem_Id(userId, itemId);
    }
    
    @Transactional
    public void deleteFavorite(int userId, int itemId) {
        if(!favoriteRepository.existsByUser_IdAndItem_Id(userId, itemId))
            throw new ResourceNotFoundException("Favorite not found for user and item: " + itemId + " not in " + userId + "\'s favorites", "favorite");
        favoriteRepository.deleteByUser_IdAndItem_Id(userId, itemId);
    }
    
    private void validateForDuplicatedFavorites(Favorite favorite) {
        int userId = favorite.getUser().getId();
        int itemId = favorite.getItem().getId();
        if(favoriteRepository.existsByUser_IdAndItem_Id(userId, itemId))
            throw new DuplicateResourceException("Favorite already exists for user and item: " + itemId + " already in " + userId + "\'s favorites", "favorite");
    }

    private boolean validateForDuplicatedAlertBound(AlertBound alertBound) {
        final double EPSILON = 0.01;
        List<AlertBound> bounds = alertBoundRepository.findByFavorite_IdAndConditionTypeAndConditionValueAndConditionPercent(
            alertBound.getFavorite().getId(),
            alertBound.getConditionType(),
            alertBound.getConditionValue(),
            alertBound.getConditionPercent());
        return !bounds.isEmpty();
    }
}