package com.example.price_comparison.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PriceHistoryQueryConditions {
    private int itemId;         
    private List<String> platform;
    private double maxPrice;        
    private double minPrice;        
    private LocalDateTime startDate; // 查询开始时间
    private LocalDateTime endDate;   // 查询结束时间
}
