package com.purwafest.purwafest.auth.infrastructure.repository;

import com.purwafest.purwafest.auth.domain.entities.User;
import com.purwafest.purwafest.auth.domain.enums.UserType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Integer>, JpaSpecificationExecutor<User> {
    Optional<User> findUserByEmail(String email);
    long countByEmail(String email);
}
