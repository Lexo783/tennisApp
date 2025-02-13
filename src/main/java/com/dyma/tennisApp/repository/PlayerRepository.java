package com.dyma.tennisApp.repository;

import com.dyma.tennisApp.data.PlayerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlayerRepository extends JpaRepository<PlayerEntity, Long> {
    Optional<PlayerEntity> findOneByLastNameIgnoreCase(String lastName);
}
