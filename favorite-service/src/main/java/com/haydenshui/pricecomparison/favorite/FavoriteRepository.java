package com.haydenshui.pricecomparison.favorite;

import com.haydenshui.pricecomparison.shared.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Integer> {
    
    /**
     * find favorite list by user id
     * @param userId
     * @return
     */
    List<Favorite> findByUser_Id(int userId);
        
    /**
     * find favorite list by item id
     * @param itemId
     * @return
     */
    List<Favorite> findByItem_Id(int itemId);

    /**
     * find favorite list by user id and item id
     * @param userId
     * @param itemId
     * @return
     */
    Favorite findByUser_IdAndItem_Id(int userId, int itemId);
    
    /**
     * find favorite list by user id and item id
     * @param userId
     * @param itemId
     * @return 
     */
    boolean existsByUser_IdAndItem_Id(int userId, int itemId);
    
    /**
     * delete favorite list by user id and item id
     * @param userId
     * @param itemId
     */
    void deleteByUser_IdAndItem_Id(int userId, int itemId);
}
