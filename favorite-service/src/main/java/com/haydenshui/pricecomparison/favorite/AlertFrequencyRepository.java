package com.haydenshui.pricecomparison.favorite;

import com.haydenshui.pricecomparison.shared.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlertFrequencyRepository extends JpaRepository<AlertFrequency, Integer> {
    
}