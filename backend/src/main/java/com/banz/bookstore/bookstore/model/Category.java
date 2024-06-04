package com.banz.bookstore.bookstore.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "categories")
@Data
public class Category {
    @Id
    private Long id;
    private String name;
    @ManyToMany(mappedBy = "categories")
    private List<Book> books;
}
