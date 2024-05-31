package com.banz.bookstore.bookstore.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Category {
    @Id
    private Long id;
    private String name;
    @ManyToMany
    private List<Book> book;
}
