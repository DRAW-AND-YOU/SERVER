package com.drawandyou.drawandyou_server.domain.user.domain.repository;

import com.drawandyou.drawandyou_server.domain.user.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findOptionalByUsername(String username);

    User findByUsername(String username);

    Boolean existsByUsername(String username);

    // email 관련 메소드 추가
    User findByEmail(String email);

    Boolean existsByEmail(String email);
}
