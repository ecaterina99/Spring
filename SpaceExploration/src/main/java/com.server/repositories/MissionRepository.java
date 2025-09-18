package com.server.repositories;
import com.server.models.Mission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MissionRepository extends JpaRepository <Mission, Integer> {
        boolean existsByCode(String code);
        List <Mission> findByDestinationId(Integer destinationId);



    @Query("SELECT DISTINCT m FROM missions m " +
            "LEFT JOIN FETCH m.missionParticipants mp " +
            "LEFT JOIN FETCH m.destination d " +
            "LEFT JOIN FETCH m.missionRequiredSpecializations mrs " +
            "WHERE m.id = :id")
    Optional<Mission> findByIdWithDetails(@Param("id") int id);

    @Query("SELECT DISTINCT m FROM missions m " +
            "LEFT JOIN FETCH m.missionParticipants mp " +
            "LEFT JOIN FETCH m.destination d " +
            "LEFT JOIN FETCH m.missionRequiredSpecializations mrs")
    List<Mission> findAllWithDetails();

    @Query("SELECT DISTINCT m FROM missions m " +
            "LEFT JOIN FETCH m.destination d " +
            "LEFT JOIN FETCH m.missionRequiredSpecializations mrs " +
            "WHERE d.id = :destinationId")
    List<Mission> findByDestinationIdWithDetails(@Param("destinationId") int destinationId);
}
