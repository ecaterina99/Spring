package com.server.repositories;

import com.server.models.Astronaut;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AstronautRepository extends JpaRepository<Astronaut, Integer> {
 /*   @Query("SELECT DISTINCT a FROM astronauts a " +
            "LEFT JOIN FETCH a.missionParticipants mp " +
            "LEFT JOIN FETCH mp.mission m " +
            "LEFT JOIN FETCH m.destination d " +
            "LEFT JOIN FETCH m.missionSpecializations mrs " +
            "LEFT JOIN FETCH m.missionReport mr " +
            "WHERE a.id = :id")
    Optional<Astronaut> findByIdWithMissions(@Param("id") int id);

    @Query("SELECT DISTINCT a FROM astronauts a " +
            "LEFT JOIN FETCH a.missionParticipants mp " +
            "LEFT JOIN FETCH mp.mission m " +
            "LEFT JOIN FETCH m.destination d " +
            "LEFT JOIN FETCH m.missionSpecializations mrs " +
            "LEFT JOIN FETCH m.missionReport mr")
    List<Astronaut> findAllWithMissions();

  */
    Optional <Astronaut> findAstronautById(Integer id);

    List<Astronaut> findBySpecialization(Astronaut.Specialization specialization);

    List<Astronaut> findByHealthStatus(Astronaut.HealthStatus healthStatus);
}