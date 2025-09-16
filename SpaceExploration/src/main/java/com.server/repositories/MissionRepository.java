package com.server.repositories;
import com.server.models.Mission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MissionRepository extends JpaRepository <Mission, Integer> {
        boolean existsByCode(String code);
    }

