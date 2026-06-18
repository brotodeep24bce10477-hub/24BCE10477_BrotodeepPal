package com.example.bookmanagement.service;

import com.example.bookmanagement.exception.BookNotFoundException;
import com.example.bookmanagement.model.Book;
import com.example.bookmanagement.repository.BookRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository repository;

    public BookServiceImpl(BookRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Book> getAllBooks() {
        return repository.findAll();
    }

    @Override
    public Book getBookById(String id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new BookNotFoundException("Book not found"));
    }

    @Override
    public Book saveBook(Book book) {
        return repository.save(book);
    }

    @Override
    public Book updateBook(String id, Book book) {

        Book existing = getBookById(id);

        existing.setTitle(book.getTitle());
        existing.setAuthor(book.getAuthor());
        existing.setGenre(book.getGenre());
        existing.setPrice(book.getPrice());
        existing.setQuantity(book.getQuantity());
        existing.setPublicationYear(book.getPublicationYear());
        existing.setAvailabilityStatus(
                book.isAvailabilityStatus());

        return repository.save(existing);
    }

    @Override
    public void deleteBook(String id) {
        repository.deleteById(id);
    }
}