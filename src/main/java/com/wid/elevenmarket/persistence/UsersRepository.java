package com.wid.elevenmarket.persistence;

import com.wid.elevenmarket.model.Users;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<Users, Long> {

    // 현재 유효한 계정인지 확인
    @Query("SELECT u from Users u where u.Id = :userId AND u.validation = 1")
    Optional<Users> findByUserId(@Param("userId") Long userId);
}
