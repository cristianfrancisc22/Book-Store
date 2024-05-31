package com.banz.bookstore.bookstore.repository;

import com.banz.bookstore.bookstore.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}
