package com.playdata.userservice.user.repository;

import com.playdata.userservice.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByemail(String email);



    List<User> id(Long id);
    boolean existsByEmail(String email);

    Optional<User> findByUsername(String username);

    Optional<User> findBySocialProviderAndSocialId(String socialId, String socialProvider);
}
