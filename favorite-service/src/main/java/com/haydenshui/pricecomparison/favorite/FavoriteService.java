package com.haydenshui.pricecomparison.favorite;

import com.haydenshui.pricecomparison.shared.model.*;
import com.haydenshui.pricecomparison.favorite.FavoriteRepository;
import com.haydenshui.pricecomparison.shared.exception.custom.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import jakarta.validation.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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
    private JavaMailSender mailSender;

    @Transactional
    public Favorite createFavorite(Favorite favorite) {
        validateForDuplicatedFavorites(favorite);
        return favoriteRepository.save(favorite);
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
        fieldUpdaters.put("reminderFrequency", value -> existingFavorite.setReminderFrequency((int) value));
        fieldUpdaters.put("lastReminderTime", value -> existingFavorite.setLastReminderTime((LocalDateTime) value));
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

    @Scheduled(fixedRate = 3600000) // 每小时执行一次
    public void checkAndSendReminders() {
        List<Favorite> favorites = favoriteRepository.findAll();

        for (Favorite favorite : favorites) {
            if (timeout(favorite)) {
                sendReminder(favorite);
                updateLastReminderTime(favorite);
            }
        }
    }

    private boolean timeout(Favorite favorite) {
        long currentTime = System.currentTimeMillis();
        long lastReminderTime = favorite.getLastReminderTime().atZone(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli();
        long reminderFrequencyInMillis = favorite.getReminderFrequency() * 1000L; // 秒转毫秒

        return (currentTime - lastReminderTime) >= reminderFrequencyInMillis;
    }

    private boolean priceHasChanged(Favorite favorite) {
        Item item = favorite.getItem();
        double currentPrice = getPriceFromExternalSource(item); // 假设这个方法获取到商品的最新价格
        
        // 如果价格发生变化，则返回true
        return currentPrice != item.getPrice();
    }

    private double getPriceFromExternalSource(Item item) {
        try {
            String url = "http://item-service/items/price?id=" + item.getId();
            ResponseEntity<Item> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Map<String, String>>>() {}
            );
            return Double.parseDouble(response.getBody().getPrice());
        } catch (Exception e) {
            e.printStackTrace();
            return 0.00;
        }
    }

    // 发送提醒邮件
    private void sendReminder(Favorite favorite) {
        // 获取用户邮箱等信息
        String userEmail = favorite.getUser().getEmail();

        // 使用Spring邮件发送功能
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(userEmail);
        message.setSubject("Price Alert: " + favorite.getItem().getName());
        message.setText("The price of your favorite item " + favorite.getItem().getName() + " has been updated. Check it out now!");

        mailSender.send(message);
    }

    // 更新lastReminderTime
    private void updateLastReminderTime(Favorite favorite) {
        favorite.setLastReminderTime(
            LocalDateTime.now()
                .withNano((LocalDateTime.now().getNano() / 1000) * 1000) // 将纳秒调整为微秒
        );
        favoriteRepository.save(favorite);
    }

}