package com.wid.elevenmarket.persistence;

import com.wid.elevenmarket.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<Users, Long> {
}
