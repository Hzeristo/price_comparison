package com.example.price_comparison.repository;

import com.example.price_comparison.model.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Integer> {
    
    boolean existsById(int id);

    boolean existsByUser(int user_id);

    Favorite findById(int id);
    
    List<Favorite> findByUserId(int user_id);

    void deleteById(int id);
}
