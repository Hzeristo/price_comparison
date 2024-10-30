package com.example.price_comparison.repository;

import com.example.price_comparison.model.PriceHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PriceHistoryRepository extends JpaRepository<PriceHistory, Integer> {
    List<PriceHistory> findByItemId(int itemId);

    @Query("SELECT ph FROM PriceHistory ph WHERE " +
    "(:itemId IS NULL OR ph.itemId = :itemId) AND " +
    "(:platform IS NULL OR ph.platform IN :platforms) AND " +
    "(:maxPrice IS NULL OR ph.price <= :maxPrice) AND " +
    "(:minPrice IS NULL OR ph.price >= :minPrice) AND " +
    "(:startDate IS NULL OR ph.timestamp >= :startDate) AND " +
    "(:endDate IS NULL OR ph.timestamp <= :endDate)")
    List<PriceHistory> findByConditionedQuery(
        @Param("itemId") int itemId,
        @Param("platforms")  List<String> platforms,
        @Param("maxPrice") double maxPrice,
        @Param("minPrice") double minPrice,
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate
    );
    
}
