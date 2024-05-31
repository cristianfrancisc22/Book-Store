package com.banz.bookstore.bookstore.model;


import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String author;
    private String description;
    private String publisher;
    private String imageLink;
    private double price;
    private float rating;
    @ManyToMany
    private List<Category> category;
}
