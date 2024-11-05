package com.haydenshui.pricecomparison.shared.model;

import com.haydenshui.pricecomparison.shared.model.Platform;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemQueryConditions { 
    private String name;
    private String skuId;
    private int categoryId;
    private double maxPrice;
    private double minPrice;
    private String sortBy;             
    private boolean ascending;         
    private List<String> platforms; 
}
