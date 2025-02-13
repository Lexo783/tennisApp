package com.dyma.tennisApp.repository;

import com.dyma.tennisApp.data.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findOneWithRolesByLoginIgnoreCase(String login);
}
