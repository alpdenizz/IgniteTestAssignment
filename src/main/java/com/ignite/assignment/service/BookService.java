package com.ignite.assignment.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ignite.assignment.model.Book;
import com.ignite.assignment.model.Comment;
import com.ignite.assignment.repository.BookRepository;

@Service
public class BookService {
	
	@Autowired
	private BookRepository bookRepository;
	
	@Autowired
	private EntityManager em;
	
	public BookService() {}
	
	public void addBook(Book book) {
		bookRepository.save(book);
	}
	
	public Optional<Book> getBook(String isbn) {
		return bookRepository.findById(isbn);
	}
	
	public List<Book> getAllBooks() {
		return bookRepository.findAll();
	}
	
	public boolean deleteBook(String isbn13) {
		Optional<Book> opt = getBook(isbn13);
		if(opt.isPresent()) {
			bookRepository.deleteById(isbn13);
			return true;
		}
		return false;
	}
	
	public void updateBook(String isbn, String isbn2, String title, String author) {
		Optional<Book> book = getBook(isbn);
		if(book.isPresent()) {
			Book present = book.get();
			if(isbn.equals(isbn2)) {
				present.setTitle(title);
				present.setAuthor(author);
				bookRepository.save(present);
			}
			else {
				Book updated = new Book(isbn2, title, author);
				if(deleteBook(isbn)) {
					bookRepository.save(updated);
				}
			}
		}
	}
	
	public void addComment(String isbn, String content) {
		Optional<Book> book = getBook(isbn);
		if(book.isPresent()) {
			Book b = book.get();
			b.addComment(content);
			bookRepository.save(b);
		}
	}
	
	public List<Comment> getAllComments(String isbn) {
		Optional<Book> book = getBook(isbn);
		if(book.isPresent()) {
			return book.get().getComments();
		}
		return null;
	}

}
