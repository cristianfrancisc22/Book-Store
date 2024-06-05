package com.banz.bookstore.bookstore.repository;

import com.banz.bookstore.bookstore.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category findByName(String drama);
}
