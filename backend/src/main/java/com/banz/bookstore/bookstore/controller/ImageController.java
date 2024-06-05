package com.banz.bookstore.bookstore.controller;


import com.banz.bookstore.bookstore.exceptions.ImageNotFoundException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/imagelink")
public class ImageController {

    @GetMapping("/{imageName}")
    public ResponseEntity<Resource> getImage(@PathVariable String imageName) {
        // Construct the path to the image file
        Path imagePath = Paths.get("src/main/resources/static/images/" + imageName);

        try {
            // Load the image as a Resource
            Resource resource = new UrlResource(imagePath.toUri());

            // Check if the resource exists
            if (!resource.exists()) {
                throw new ImageNotFoundException("Image not found");
            }

            // Return the image with appropriate headers
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType("image/webp"))
                    .body(resource);
        } catch (MalformedURLException e) {
            throw new ImageNotFoundException("Failed to load image");
        }
    }
}
