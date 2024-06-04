package com.banz.bookstore.bookstore.payload.request;

import com.banz.bookstore.bookstore.model.Category;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.UniqueElements;


import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateBookDTO {

    @NotBlank(message = "Title cannot be blank")
    private String title;
    @NotBlank(message = "Author cannot be blank")
    private String author;
    @NotBlank(message = "Description cannot be blank")
    private String description;
    @NotBlank(message = "Publisher cannot be blank")
    private String publisher;
    @NotBlank(message = "ImageLink cannot be blank")
    private String imageLink;
    @NotBlank(message = "Price cannot be blank")
    private double price;
    @UniqueElements(message = "Categories must be unique")
    private List<Category> categories;
}
