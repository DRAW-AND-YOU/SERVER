package com.drawandyou.drawandyou_server.user.domain.repository;

import com.drawandyou.drawandyou_server.user.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findOptionalByUsername(String username);

    User findByUsername(String username);

    Boolean existsByUsername(String username);
}
