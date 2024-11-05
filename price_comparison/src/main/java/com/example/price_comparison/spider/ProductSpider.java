package com.example.price_comparison.spider;

import com.example.price_comparison.model.*;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.openqa.selenium.WebDriver;
import java.io.FileReader;
import java.util.List;

public interface ProductSpider {

    List<Item> startRequests(String query, String type); // 启动请求并抓取数据

    void close();

    default String getStartUrl(String path, String category) {
        JsonObject categoryDict = JsonParser.parseReader(new FileReader(path)).getAsJsonObject();
        return getUrlFromDict(categoryDict, category);
    }

    default String getUrlFromDict(JsonObject categoryDict, String category) {
        for (String bCategory : categoryDict.keySet()) {
            JsonObject bInfo = categoryDict.getAsJsonObject(bCategory);
            if (category.equals(bCategory)) {
                return bInfo.get("url").getAsString();
            }
            JsonObject subCategories = bInfo.getAsJsonObject("子分类");
            for (String mCategory : subCategories.keySet()) {
                JsonObject mInfo = subCategories.getAsJsonObject(mCategory);
                if (category.equals(mCategory)) {
                    return mInfo.get("url").getAsString();
                }
                JsonObject subSubCategories = mInfo.getAsJsonObject("子分类");
                for (String sCategory : subSubCategories.keySet()) {
                    JsonObject sInfo = subSubCategories.getAsJsonObject(sCategory);
                    if (category.equals(sCategory)) {
                        return sInfo.get("url").getAsString();
                    }
                }
            }
        }
        return null;
    }
}
