package com.lanceraro.api.repository;

import com.lanceraro.api.model.TcgGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TcgGroupRepository extends JpaRepository<TcgGroup, Long> {
}
