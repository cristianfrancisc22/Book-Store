package com.banz.bookstore.bookstore.repository;

import com.banz.bookstore.bookstore.model.User;
import org.springframework.data.jpa.repository.JpaRepository;





public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);

    boolean existsByUsername(String username);
}
