package com.example.parcial.Services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.parcial.Repositories.BookRepository;
import com.example.parcial.Entities.BookEntity;

import java.util.*;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public ResponseEntity<?> getAllBooks(Pageable pageable) {
        Page<BookEntity> books = bookRepository.findAll(pageable);
        return buildResponseEntity(books);
    }

    public ResponseEntity<?> getBookById(UUID id) {
        Optional<BookEntity> book = bookRepository.findById(id);
        if (book.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("Status", String.format("Book with ID %s not found.", id)));
        }
        return ResponseEntity.ok(Collections.singletonMap("Book", book.get()));
    }

    public ResponseEntity<?> getBooksByTitle(String title, Pageable pageable) {
        Page<BookEntity> books = bookRepository.findAllByTitleContaining(title, pageable);
        return buildResponseEntity(books);
    }

    private ResponseEntity<?> buildResponseEntity(Page<BookEntity> books) {
        Map<String, Object> response = new HashMap<>();
        response.put("TotalElements", books.getTotalElements());
        response.put("TotalPages", books.getTotalPages());
        response.put("CurrentPage", books.getNumber());
        response.put("NumberOfElements", books.getNumberOfElements());
        response.put("Books", books.getContent());
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<?> addBook(BookEntity bookToAdd) {
        Page<BookEntity> existingBooks = bookRepository.findAllByTitleContaining(bookToAdd.getTitle(), Pageable.unpaged());
        if (existingBooks.getTotalElements() > 0) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Collections.singletonMap("Status", String.format("Book already exists with %d coincidences.", existingBooks.getTotalElements())));
        } else {
            BookEntity savedBook = bookRepository.save(bookToAdd);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Collections.singletonMap("Status", String.format("Added Book with ID %s", savedBook.getId())));
        }
    }

    public ResponseEntity<?> updateBook(UUID id, BookEntity bookToUpdate) {
        Optional<BookEntity> optionalBook = bookRepository.findById(id);
        if (optionalBook.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("Status", String.format("Book with ID %s not found.", id)));
        }

        BookEntity existingBook = optionalBook.get();
        existingBook.setTitle(bookToUpdate.getTitle());
        existingBook.setAuthor(bookToUpdate.getAuthor());
        existingBook.setPublicationYear(bookToUpdate.getPublicationYear());
        existingBook.setPages(bookToUpdate.getPages());

        bookRepository.save(existingBook);

        return ResponseEntity.ok(Collections.singletonMap("Status", String.format("Updated Book with ID %s", existingBook.getId())));
    }

    public ResponseEntity<?> deleteBook(UUID id) {
        Optional<BookEntity> optionalBook = bookRepository.findById(id);
        if (optionalBook.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("Status", String.format("Book with ID %s doesn't exist.", id)));
        }

        bookRepository.deleteById(id);
        return ResponseEntity.ok(Collections.singletonMap("Status", String.format("Deleted Book with ID %s", id)));
    }
}
