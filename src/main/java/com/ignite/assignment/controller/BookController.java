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

import com.ignite.assignment.model.Book;
import com.ignite.assignment.model.Comment;
import com.ignite.assignment.service.BookService;

/**
 * 
 * @author Denizalp KAPISIZ (denizalp1634@gmail.com)
 * <p>Book controller to operate Book management system among views</p>
 */

@Controller
public class BookController {
	
	@Autowired
	private BookService bookService;
	
	/**
	 * 
	 * @param book built by entered input from form in createBookPage.html
	 * @param bindingResult to obtain validation result from form
	 * @return home page if operation is successful<br>
	 * Otherwise form page will be open with errors
	 */
	@PostMapping("/books")
	public String addBook(@Valid @ModelAttribute Book book, BindingResult bindingResult) {
		if(bindingResult.hasErrors()) {
			return "createBookPage";
		}
		else {
			bookService.addBook(book);
			return "redirect:/home";
		}
	}
	
	/**
	 * 
	 * @param bookId isbn of Book to be updated
	 * @param selectedBook selected book in home page
	 * @param bindingResult validation results of new values in editing
	 * @param model
	 * @return If no validation error then Updates the book and returns to home page<br>
	 * If validation error then still returns to home page but without change in selected book
	 */
	@PostMapping("/books/{bookId}")
	public String updateBook(@PathVariable String bookId, @Valid @ModelAttribute Book selectedBook, BindingResult bindingResult, Model model) {
		if(bindingResult.hasErrors()) {
			return "redirect:/home";
		}
		else {
			Optional<Book> opt = bookService.getBook(bookId);
			if(opt.isPresent()) {
				bookService.updateBook(bookId, selectedBook.getIsbn13(), selectedBook.getTitle(), selectedBook.getAuthor());
				//return "redirect:/books/"+selectedBook.getIsbn13();
			}
			return "redirect:/home";
		}
	}
	
	/**
	 * 
	 * @param model holds object to fill with form values
	 * @return opens form page for book creation
	 */
	@GetMapping("/books/new")
	public String newBook(Model model) {
		model.addAttribute("book", new Book());
		return "createBookPage";
	}
	
	/**
	 * 
	 * @param bookId Book's isbn
	 * @param model holds objects for operation in editPage
	 * @return Edit page of selected book
	 */
	@GetMapping("/books/{bookId}/edit")
	public String editBook(@PathVariable String bookId, Model model) {
		
		if(bookService.getBook(bookId).isPresent()) {
			Book book = bookService.getBook(bookId).get();
			model.addAttribute("selectedBook", book);
			model.addAttribute("comment", new Comment());
			return "editBookPage";
		}
		return "redirect:/home";
	}
	
	/**
	 * 
	 * @param bookId Book isbn to delete
	 * @param model
	 * @return Deletes the selected book and refreshes home page
	 */
	@GetMapping("/books/{bookId}/delete")
	public String deleteBook(@PathVariable String bookId, Model model) {
		
		bookService.deleteBook(bookId);
		return "redirect:/home";
	}
	
	/**
	 * 
	 * @param bookId Book isbn to insert comment in it
	 * @param comment Comment object for operation
	 * @param bindingResult validation results from content textarea
	 * @return Inserts comment with NotEmpty comment into book and returns home page<br>
	 * If with empty comment, still returns home page but without adding comment into book
	 */
	@PostMapping("books/{bookId}/comment")
	public String addComment(@PathVariable String bookId, @Valid @ModelAttribute Comment comment, BindingResult bindingResult){
		if(bindingResult.hasErrors()) {
			return "redirect:/home";
		}
		bookService.addComment(bookId, comment.getContent());
		return "redirect:/home";
	}
	
	/**
	 * 
	 * @param model holds all books from Book table
	 * @return All existing books in the system
	 */
	@GetMapping("/home")
	public String getAllBooks(Model model) {
		List<Book> books = bookService.getAllBooks();
		model.addAttribute("books", books);
		return "mainPage";
	}
	

}
