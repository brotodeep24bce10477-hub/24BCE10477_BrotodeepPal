package com.example.bookmanagement.service;

import java.util.List;

import com.example.bookmanagement.model.Book;

public interface BookService {

    List<Book> getAllBooks();

    Book getBookById(String id);

    Book saveBook(Book book);

    Book updateBook(String id, Book book);

    void deleteBook(String id);
}