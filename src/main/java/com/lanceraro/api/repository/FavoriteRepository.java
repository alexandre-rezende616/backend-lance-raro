package com.lanceraro.api.repository;

import com.lanceraro.api.model.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    List<Favorite> findByUserId(Long userId);
    Favorite findByUserIdAndRelicId(Long userId, Long relicId);
}
