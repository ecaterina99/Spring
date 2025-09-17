package com.server.repositories;

import com.server.models.Destination;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DestinationRepository  extends JpaRepository <Destination, Integer> {
}
