package com.example.bookmanagement.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.bookmanagement.model.Book;

public interface BookRepository extends MongoRepository<Book, String> {

}