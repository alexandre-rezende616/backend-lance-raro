package com.lanceraro.api.repository;

import com.lanceraro.api.model.Relic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RelicRepository extends JpaRepository<Relic, Long> {
    List<Relic> findByUserId(Long userId);
}
