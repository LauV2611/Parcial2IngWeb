package com.example.parcial.Controller;

import com.example.parcial.Services.BookService;
import com.example.parcial.Entities.BookEntity;


import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/books")
@Validated
public class BookController {

        private final BookService bookService;

        public BookController(BookService bookService) {
            this.bookService = bookService;
        }

        @GetMapping
        public ResponseEntity<?> getAllBooks(
                @RequestParam(defaultValue = "0") int page,
                @RequestParam(defaultValue = "5") int size,
                @RequestParam(defaultValue = "title,asc") String[] sort) {
            try {
                Pageable pageable = PageRequest.of(page, size, Sort.by(parseSort(sort)));
                return bookService.getAllBooks(pageable);
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().body("Invalid sorting direction. Use 'asc' or 'desc'.");
            }
        }

        private Sort.Order parseSort(String[] sort) {
            if (sort.length < 2) {
                throw new IllegalArgumentException("Sort parameter must have both field and direction (e.g., 'title,desc').");
            }

            String property = sort[0];
            String direction = sort[1].toLowerCase();

            List<String> validDirections = Arrays.asList("asc", "desc");
            if (!validDirections.contains(direction)) {
                throw new IllegalArgumentException("Invalid sort direction. Use 'asc' or 'desc'.");
            }

            return new Sort.Order(Sort.Direction.fromString(direction), property);
        }

        @GetMapping("/{id}")
        public ResponseEntity<?> getBookById(@PathVariable UUID id) {
            return bookService.getBookById(id);
        }

        @GetMapping("/search")
        public ResponseEntity<?> getBooksByTitle(
                @RequestParam String title,
                @RequestParam(defaultValue = "0") int page,
                @RequestParam(defaultValue = "5") int size,
                @RequestParam(defaultValue = "title,asc") String[] sort) {

            Pageable pageable = PageRequest.of(page, size, Sort.by(parseSort(sort)));
            return bookService.getBooksByTitle(title, pageable);
        }

        @PostMapping
        public ResponseEntity<?> insertBook(@Valid @RequestBody BookEntity bookEntity) {
            return bookService.addBook(bookEntity);
        }

        @PutMapping("/{id}")
        public ResponseEntity<?> updateBook(@PathVariable UUID id, @Valid @RequestBody BookEntity bookEntity) {
            return bookService.updateBook(id, bookEntity);
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<?> deleteBook(@PathVariable UUID id) {
            return bookService.deleteBook(id);
        }

        @GetMapping("/hello")
        public String hello() {
            return "Hello World from Book Inventory";
        }

    }







