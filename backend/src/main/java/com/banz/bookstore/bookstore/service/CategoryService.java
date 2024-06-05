package com.banz.bookstore.bookstore.service;

import com.banz.bookstore.bookstore.exceptions.CategoryNotFoundException;
import com.banz.bookstore.bookstore.model.Book;
import com.banz.bookstore.bookstore.model.Category;
import com.banz.bookstore.bookstore.payload.request.CreateCategoryDTO;
import com.banz.bookstore.bookstore.repository.BookRepository;
import com.banz.bookstore.bookstore.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {

    private final Logger logger = LoggerFactory.getLogger(CategoryService.class);
    private final CategoryRepository categoryRepository;
    private final BookRepository bookRepository;

    public CategoryService(CategoryRepository categoryRepository, BookRepository bookRepository) {
        this.categoryRepository = categoryRepository;
        this.bookRepository = bookRepository;
    }

    @Transactional
    public void addCategory(String name) {
        if (categoryRepository.findByName(name) != null) {
            throw new RuntimeException("Category already exists");
        }
        Category category = new Category();
        category.setName(name);
        categoryRepository.save(category);

    }

    public void deleteCategory(Long id) {
        if (categoryRepository.existsById(id)) {
            Category category = categoryRepository.findById(id).orElseThrow(() ->
                    new CategoryNotFoundException("Category with id " + id + " not found"));

            for (Book book : category.getBooks()) {
                book.getCategories().remove(category);
                bookRepository.save(book);
            }

            categoryRepository.deleteById(id);
            logger.info("Deleted category with id: {}", id);
        } else {
            logger.warn("Attempted to delete non-existent category with id: {}", id);
            throw new CategoryNotFoundException("Category with id " + id + " not found");
        }
    }

    @Transactional
    public void updateCategory(Long id, CreateCategoryDTO categoryDTO) {
        Category category = categoryRepository.findById(id).orElseThrow(
                () -> new CategoryNotFoundException("Category with id " + id + " not found")
        );

        category.setName(categoryDTO.getName());
        categoryRepository.save(category);
    }

    public Category getCategory(Long id) {
        return categoryRepository.findById(id).orElseThrow(
                () -> new CategoryNotFoundException("Category with id " + id + " not found")
        );
    }
}
