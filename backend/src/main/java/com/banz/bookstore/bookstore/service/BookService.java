package com.banz.bookstore.bookstore.service;

import com.banz.bookstore.bookstore.exceptions.BookNotFoundException;
import com.banz.bookstore.bookstore.model.Book;
import com.banz.bookstore.bookstore.payload.request.CreateBookDTO;
import com.banz.bookstore.bookstore.repository.BookRepository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
public class BookService {

    @Value("${upload.path}")
    private String uploadPath;

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> getBooks(int count) {
        Pageable pageable = PageRequest.of(0, count);
        return bookRepository.findAll(pageable).getContent();
    }

    public Book findById(Long id) {
        return bookRepository.findById(id).orElseThrow(
                () -> new BookNotFoundException(String.format("Book with id: %s not found", id)));
    }

    public Book createBook(CreateBookDTO createBookDTO, MultipartFile image) {
        try {
            String filePath = saveImageToFileSystem(image);
            Book book = new Book();
            book.setTitle(createBookDTO.getTitle());
            book.setAuthor(createBookDTO.getAuthor());
            book.setDescription(createBookDTO.getDescription());
            book.setPrice(createBookDTO.getPrice());
            book.setImageLink(filePath);
            book.setPublisher(createBookDTO.getPublisher());
            book.setCategories(createBookDTO.getCategories());
            book = bookRepository.save(book);
            return book;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String saveImageToFileSystem(MultipartFile image) throws IOException {
        String uniqueFilename = UUID.randomUUID() + ".webp";
        String filePath = uploadPath + File.separator + uniqueFilename;
        File dest = new File(filePath);
        image.transferTo(dest);
        return filePath;
    }
}
