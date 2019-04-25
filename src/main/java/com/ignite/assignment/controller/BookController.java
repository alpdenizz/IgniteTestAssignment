package com.ignite.assignment.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ignite.assignment.model.Book;
import com.ignite.assignment.model.Comment;
import com.ignite.assignment.service.BookService;

@Controller
public class BookController {
	
	@Autowired
	private BookService bookService;
	
	@PostMapping("/books")
	public String addBook(@Valid @ModelAttribute @RequestBody Book book, BindingResult bindingResult) {
		if(bindingResult.hasErrors()) {
			return "createBookPage";
		}
		else {
			bookService.addBook(book);
			return "redirect:/home";
		}
	}
	
	@PostMapping("/books/{bookId}")
	public String updateBook(@PathVariable String bookId, @Valid @ModelAttribute Book selectedBook, BindingResult bindingResult, Model model) {
		if(bindingResult.hasErrors()) {
			return "redirect:/home";
		}
		else {
			Optional<Book> opt = bookService.getBook(bookId);
			System.out.println("Received: "+bookId);
			if(opt.isPresent()) {
				Book b = opt.get();
				bookService.updateBook(bookId, selectedBook.getIsbn13(), selectedBook.getTitle(), selectedBook.getAuthor());
				//return "redirect:/books/"+selectedBook.getIsbn13();
			}
			return "redirect:/home";
		}
	}
	
	@GetMapping("/books/new")
	public String newBook(Model model) {
		model.addAttribute("book", new Book());
		return "createBookPage";
	}
	
	@GetMapping("/books/{bookId}/edit")
	public String editBook(@PathVariable String bookId, Model model) {
		System.out.println("Received isbn: "+bookId);
		Book book = bookService.getBook(bookId).get();
		model.addAttribute("selectedBook", book);
		model.addAttribute("comment", new Comment());
		return "editBookPage";
	}
	
	@GetMapping("/books/{bookId}/delete")
	public String deleteBook(@PathVariable String bookId, Model model) {
		System.out.println("Received isbn: "+bookId);
		bookService.deleteBook(bookId);
		return "redirect:/home";
	}
	
	@PostMapping("books/{bookId}/comment")
	public String addComment(@PathVariable String bookId, @Valid @ModelAttribute Comment comment, BindingResult bindingResult){
		if(bindingResult.hasErrors()) {
			return "redirect:/home";
		}
		bookService.addComment(bookId, comment.getContent());
		return "redirect:/home";
	}
	
	@GetMapping("/home")
	public String getAllBooks(Model model) {
		//System.out.println(bookService.getAllBooks());
		List<Book> books = bookService.getAllBooks();
		model.addAttribute("books", books);
		return "mainPage";
	}
	
	@GetMapping("/api/books")
	public String getBook(@RequestParam String isbn, Model model) {
		Optional<Book> book = bookService.getBook(isbn);
		if(book.isPresent()) {
			Book present = book.get();
			model.addAttribute("isbn", present.getIsbn13());
			model.addAttribute("title", present.getTitle());
			model.addAttribute("author", present.getAuthor());
			model.addAttribute("comments", present.getComments());
			return "/books/"+isbn;
		}
		return "/";
	}
	
	@PutMapping("/api/books")
	public String updateBook(@RequestParam String isbn, Model model) {
		Optional<Book> book = bookService.getBook(isbn);
		if(book.isPresent()) {
			Object o1 = model.getAttribute("isbn");
			Object o2 = model.getAttribute("title");
			Object o3 = model.getAttribute("author");
			String s1 = o1 != null ? o1.toString() : "";
			String s2 = o2 != null ? o2.toString() : "";
			String s3 = o3 != null ? o3.toString() : "";
			bookService.updateBook(isbn, s1, s2, s3);
			return "/books/"+isbn;
		}
		return "/";
	}
	

}
