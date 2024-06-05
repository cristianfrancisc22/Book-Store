package com.banz.bookstore.bookstore.service;

import com.banz.bookstore.bookstore.exceptions.BookNotFoundException;
import com.banz.bookstore.bookstore.exceptions.CategoryNotFoundException;
import com.banz.bookstore.bookstore.model.Book;
import com.banz.bookstore.bookstore.model.Category;
import com.banz.bookstore.bookstore.payload.request.CreateBookDTO;
import com.banz.bookstore.bookstore.repository.BookRepository;

import com.banz.bookstore.bookstore.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class BookService {

    @Value("${upload.path}")
    private String uploadPath;

    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;

    public BookService(BookRepository bookRepository
            , CategoryRepository categoryRepository) {
        this.bookRepository = bookRepository;
        this.categoryRepository = categoryRepository;
    }

    public List<Book> getBooks(int count) {
        Pageable pageable = PageRequest.of(0, count);
        return bookRepository.findAll(pageable).getContent();
    }

    public Book findById(Long id) {
        return bookRepository.findById(id).orElseThrow(
                () -> new BookNotFoundException(
                        String.format("Book with id: %s not found", id)));
    }

    public Book createBook(CreateBookDTO createBookDTO, MultipartFile image) {
        try {
            String filePath = saveImageToFileSystem(image);
            List<Category> categories = validateCategories(createBookDTO.getCategories());

            Book book = new Book();
            book.setTitle(createBookDTO.getTitle());
            book.setAuthor(createBookDTO.getAuthor());
            book.setDescription(createBookDTO.getDescription());
            book.setPrice(createBookDTO.getPrice());
            book.setImageLink(filePath);
            book.setPublisher(createBookDTO.getPublisher());
            book.setCategories(categories);

            return bookRepository.save(book);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save image", e);
        } catch (RuntimeException e) {
            throw new RuntimeException("Category validation failed", e);
        }
    }

    private List<Category> validateCategories(List<String> categoryNames) {
        List<Category> categories = new ArrayList<>();
        for (String name : categoryNames) {
            Category category = categoryRepository.findByName(name);
            if (category == null) {
                throw new CategoryNotFoundException("Category not found: " + name);
            }
            categories.add(category);
        }
        return categories;
    }

    private String saveImageToFileSystem(MultipartFile image) throws IOException {
        String uniqueFilename = UUID.randomUUID() + ".webp";
        String filePath = uploadPath + File.separator + uniqueFilename;
        File dest = new File(filePath);
        System.out.println(dest);
        image.transferTo(dest);
        return uniqueFilename;
    }

    @Transactional
    public void deleteBook(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Book with id: " + id + " not found"));

        for (Category category : book.getCategories()) {
            category.getBooks().remove(book);
            categoryRepository.save(category);
        }

        bookRepository.delete(book);
    }
}
