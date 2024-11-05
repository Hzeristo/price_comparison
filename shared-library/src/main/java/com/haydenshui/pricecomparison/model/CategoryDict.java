package com.haydenshui.pricecomparison.shared.model;

import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import java.util.Map;

@Data
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDict {
    
    private Platform platform;
    private Map<String, String>dict;

    public void addMapping(String apiCategory, String dbCategory) {
        dict.put(apiCategory.trim(), dbCategory.trim());
    }

    public String getDbCategory(String apiCategory) {
        return dict.get(apiCategory);
    }

    public boolean containsApiCategory(String apiCategory) {
        return dict.containsKey(apiCategory);
    }
}
