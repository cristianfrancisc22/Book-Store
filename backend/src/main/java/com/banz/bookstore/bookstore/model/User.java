package com.banz.bookstore.bookstore.model;

import com.banz.bookstore.bookstore.enums.Authority;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Collections;
import java.util.Set;


@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    @ElementCollection(targetClass = Authority.class, fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "user_authorities", joinColumns = @JoinColumn(name = "user_id"))
    private Set<Authority> authorities = Collections.singleton(Authority.USER);

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User() {

    }
}
