package com.example.price_comparison.model;

import com.example.price_comparison.model.Platform;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemQueryConditions {
    private String name;
    private int categoryId;
    private double maxPrice;
    private double minPrice;
    private String sortBy;             
    private boolean ascending;         
    private List<Platform> platforms; 
}
