package com.banz.bookstore.bookstore.controller;

import com.banz.bookstore.bookstore.exceptions.BookNotFoundException;
import com.banz.bookstore.bookstore.model.Book;
import com.banz.bookstore.bookstore.payload.request.CreateBookDTO;
import com.banz.bookstore.bookstore.service.BookService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("api/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public ResponseEntity<List<Book>> getNumberOfBooks(
            @RequestParam(defaultValue = "20") int count) {
        List<Book> books = bookService.getBooks(count);
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @GetMapping("id/{id}")
    public ResponseEntity<Book> getBookById(
            @PathVariable Long id) {
        try {
            Book book = bookService.findById(id);
            return new ResponseEntity<>(book, HttpStatus.OK);
        } catch (BookNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/add")
    public ResponseEntity<?> createBook(
            @Valid @ModelAttribute CreateBookDTO createBookDTO,
            @ModelAttribute MultipartFile image) {
        try {
            Book book = bookService.createBook(createBookDTO, image);
            return new ResponseEntity<>(book, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable Long id) {
        try {
            bookService.deleteBook(id);
            return new ResponseEntity<>("Book deleted with id: " + id + " deleted", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
