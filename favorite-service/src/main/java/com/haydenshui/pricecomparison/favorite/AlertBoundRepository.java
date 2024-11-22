package com.haydenshui.pricecomparison.favorite;

import com.haydenshui.pricecomparison.shared.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlertBoundRepository extends JpaRepository<AlertBound, Integer> {
    
    List<AlertBound> findByFavorite_Id(int favoriteId);

    List<AlertBound> findByFavorite_IdAndConditionTypeAndConditionValueAndConditionPercent(int favorite_id, AlertType conditionType, double conditionValue, int conditionPercent);

    boolean existsByFavorite_Id(int favoriteId);

}