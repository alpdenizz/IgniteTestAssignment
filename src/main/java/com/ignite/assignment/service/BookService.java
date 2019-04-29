package com.ignite.assignment.service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ignite.assignment.model.Book;
import com.ignite.assignment.model.Comment;
import com.ignite.assignment.repository.BookRepository;
/**
 * 
 * @author Denizalp KAPISIZ (denizalp1634@gmail.com)
 * <p>Operations of Book</p>
 *
 */
@Service
public class BookService {
	
	@Autowired
	private BookRepository bookRepository;
	
	public BookService() {}
	
	/**
	 * @param book Insert this into Book table
	 */
	public void addBook(Book book) {
		if(!bookRepository.findById(book.getIsbn13()).isPresent())
		bookRepository.save(book);
	}
	
	/**
	 * 
	 * @param isbn Find book with this value in Book table
	 * @return Book with this isbn otherwise Optional.empty
	 */
	public Optional<Book> getBook(String isbn) {
		return bookRepository.findById(isbn);
	}
	
	/**
	 * 
	 * @return All books in Book table
	 */
	public List<Book> getAllBooks() {
		return bookRepository.findAll();
	}
	
	/**
	 * 
	 * @param isbn13 delete Book with this value from Book table
	 * @return <b>true</b> if deletion successful 
	 * <p>otherwise <b>false</b></p>
	 */
	public boolean deleteBook(String isbn13) {
		Optional<Book> opt = getBook(isbn13);
		if(opt.isPresent()) {
			bookRepository.deleteById(isbn13);
			return true;
		}
		return false;
	}
	
	/**
	 * 
	 * @param isbn Book isbn to be updated
	 * @param isbn2 isbn value for update
	 * @param title title value for update
	 * @param author author value for update
	 * 
	 * <p>If isbn field has not changed, then existing book will be updated with
	 * <b>valid</b> fields</p>
	 * <p>If so, then existing book will be deleted and book with new isbn will
	 * be inserted in Book table</p>
	 */
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
	
	/**
	 * 
	 * @param isbn Book isbn to add comment into it
	 * @param content Content of the comment
	 * 
	 * <p>Inserts comment with this content to book with this isbn</p>
	 */
	public void addComment(String isbn, String content) {
		Optional<Book> book = getBook(isbn);
		if(book.isPresent()) {
			Book b = book.get();
			b.addComment(content);
			bookRepository.save(b);
		}
	}
	
	/**
	 * 
	 * @param isbn Book's isbn
	 * @return comments of Book with this isbn
	 */
	public List<Comment> getAllComments(String isbn) {
		Optional<Book> book = getBook(isbn);
		if(book.isPresent()) {
			return book.get().getComments();
		}
		return null;
	}

}
