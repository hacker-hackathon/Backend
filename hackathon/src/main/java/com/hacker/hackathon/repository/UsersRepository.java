package com.hacker.hackathon.repository;

import com.hacker.hackathon.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource
public interface UsersRepository extends JpaRepository<Users,Long> {
    Optional<Users> findByPassword(String password);

    Optional<Users> findByNickname(String nickname);

    Optional<Users> findByEmail(String email);
}
