package com.user.core.repository;

import com.user.core.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsernameAndEnabled(String userName, boolean enabled);


    User findByUsername(String userName);

    User findByEmail(String email);
}
