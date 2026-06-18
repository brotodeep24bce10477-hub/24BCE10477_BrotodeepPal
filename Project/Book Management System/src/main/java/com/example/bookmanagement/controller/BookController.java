package com.example.bookmanagement.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.bookmanagement.model.Book;
import com.example.bookmanagement.service.BookService;

@Controller
@RequestMapping("/books")
public class BookController {

    private final BookService service;

    public BookController(BookService service) {
        this.service = service;
    }

    // View all books
    @GetMapping
    public String viewBooks(Model model) {

        model.addAttribute("books",
                service.getAllBooks());

        return "view-books";
    }

    // Show add book form
    @GetMapping("/add")
    public String addForm(Model model) {

        model.addAttribute("book",
                new Book());

        return "add-book";
    }

    // Save new book
    @PostMapping("/save")
    public String saveBook(@ModelAttribute Book book) {

        service.saveBook(book);

        return "redirect:/books";
    }

    // Delete book
    @GetMapping("/delete/{id}")
    public String deleteBook(@PathVariable String id) {

        service.deleteBook(id);

        return "redirect:/books";
    }

    // Show edit form
    @GetMapping("/edit/{id}")
    public String editBook(@PathVariable String id,
                           Model model) {

        Book book = service.getBookById(id);

        model.addAttribute("book", book);

        return "edit-book";
    }

    // Update book
    @PostMapping("/update/{id}")
    public String updateBook(@PathVariable String id,
                             @ModelAttribute Book book) {

        service.updateBook(id, book);

        return "redirect:/books";
    }
}