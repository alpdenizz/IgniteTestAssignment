package com.ignite.assignment.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;

import java.util.List;
import com.ignite.assignment.model.Book;
import com.ignite.assignment.model.Comment;
import com.ignite.assignment.repository.BookRepository;
import com.ignite.assignment.service.BookService;

/**
 * 
 * @author Denizalp KAPISIZ(denizalp1634@gmail.com)
 * <p>Tests of BookService</p>
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@TestPropertySource("classpath:/application-test.properties")
public class BookServiceTest {
	
	@Autowired
	private BookRepository bookRepository;
	
	@Autowired
	private BookService bookService;
	
	/**
	 * Assert autowired beans are not null
	 */
	@Test
	public void test_BeansNotNullAndLiquibaseWorking() {
		assertNotNull(bookRepository);
		assertNotNull(bookService);
		
		List<Book> books = bookService.getAllBooks();
		assertThat(books).hasSize(2);
		assertThat(books.get(0).getIsbn13()).isEqualTo("isbn999");
		assertThat(books.get(1).getIsbn13()).isEqualTo("isbn998");
	}
	
	/**
	 * Assert book is inserted successfully
	 * <ul>
	 * <li>Size of books list must be increased by 1</li>
	 * <li>New book must exist in Book table</li>
	 * </ul>
	 */
	@Test
	public void test_addBookSuccessful() {
		int before = bookService.getAllBooks().size();
		Book b = new Book("isbn","title","author");
		bookService.addBook(b);
		
		int after = bookService.getAllBooks().size();
		assertThat(after-before).isEqualTo(1);
		assertThat(bookService.getBook("isbn")).isPresent();
	}
	
	/**
	 * Assert insertion is not successful because Isbn field is empty
	 * <ul>
	 * <li>There must be no change in Book table</li>
	 * </ul>
	 */
	@Test
	public void test_addBookNotSuccessful_IsbnFieldEmpty() {
		int before = bookService.getAllBooks().size();
		Book b = new Book("","title","author");
		boolean error = false;
		try {
			bookService.addBook(b);
		}
		catch(Exception e) {
			assertNotNull(e);
			error = true;
		}
		assertThat(error).isTrue();
		int after = bookService.getAllBooks().size();
		assertThat(after-before).isEqualTo(0);
	}
	
	/**
	 * Assert insertion is not successful because Title field is empty
	 * <ul>
	 * <li>There must be no change in Book table</li>
	 * </ul>
	 */
	@Test
	public void test_addBookNotSuccessful_TitleFieldEmpty() {
		int before = bookService.getAllBooks().size();
		Book b = new Book("isbn","","author");
		boolean error = false;
		try {
			bookService.addBook(b);
		}
		catch(Exception e) {
			assertNotNull(e);
			error = true;
		}
		assertThat(error).isTrue();
		int after = bookService.getAllBooks().size();
		assertThat(after-before).isEqualTo(0);
	}
	
	/**
	 * Assert insertion is not successful because Author field is empty
	 * <ul>
	 * <li>There must be no change in Book table</li>
	 * </ul>
	 */
	@Test
	public void test_addBookNotSuccessful_AuthorFieldEmpty() {
		int before = bookService.getAllBooks().size();
		Book b = new Book("isbn","title","");
		boolean error = false;
		try {
			bookService.addBook(b);
		}
		catch(Exception e) {
			assertNotNull(e);
			error = true;
		}
		assertThat(error).isTrue();
		int after = bookService.getAllBooks().size();
		assertThat(after-before).isEqualTo(0);
	}
	
	/**
	 * Assert all books in the table will be retrieved
	 * <ul>
	 * <li>Book table size must be increased by 2</li>
	 * <li>Old book table must be a subset of new book table</li>
	 * </ul>
	 */
	@Test
	public void test_getAllBooks() {
		List<Book> beforeBooks = bookService.getAllBooks();
		Book b1 = new Book("isbn2","title2","author2");
		Book b2 = new Book("isbn3", "title3", "author3");
		
		bookService.addBook(b1);
		bookService.addBook(b2);
		
		List<Book> afterBooks = bookService.getAllBooks();
		assertThat(afterBooks).hasSize(beforeBooks.size()+2);
		assertThat(beforeBooks).isSubsetOf(afterBooks);
	}
	
	/**
	 * Assert deletion is successful
	 * <ul>
	 * <li>Deleted book must not exist in Book table</li>
	 * <li>Size of the table must be decreased by 1</li>
	 * </ul>
	 */
	@Test
	public void test_deleteBookSuccessful() {
		Book b1 = new Book("isbn4","title4","author4");
		bookService.addBook(b1);
		int before = bookService.getAllBooks().size();
		
		boolean b = bookService.deleteBook("isbn4");
		int after = bookRepository.findAll().size();
		assertThat(b).isTrue();
		assertThat(bookService.getBook("isbn4")).isEmpty();
		assertThat(before-after).isEqualTo(1);
	}
	
	/**
	 * Assert deletion is not successful because there is no such book in Book table
	 * <ul>
	 * <li>Book must not be found in the table</li>
	 * <li>Operation must return false</li>
	 * </ul>
	 */
	@Test
	public void test_deleteBookNotSuccessful() {
		assertThat(bookService.getBook("isbn5")).isEmpty();
		boolean b = bookService.deleteBook("isbn5");
		assertThat(b).isFalse();
	}
	
	/**
	 * Assert update without changing isbn field is successful
	 * <ul>
	 * <li>Book table size must not be changed</li>
	 * <li>Book must have the new values</li>
	 * </ul>
	 */
	@Test
	public void test_updateBookSuccessful_IsbnNotChanged() {
		Book b1 = new Book("isbn6","title6","author6");
		bookService.addBook(b1);
		bookService.addComment("isbn6", "good book");
		
		b1 = bookService.getBook("isbn6").get();
		
		int before = bookService.getAllBooks().size();
		
		bookService.updateBook("isbn6", "isbn6", "title7", "author7");
		
		int after = bookService.getAllBooks().size();
		
		assertThat(before).isEqualTo(after);
		assertThat(bookService.getBook("isbn6")).isPresent();
		Book b2 = bookService.getBook("isbn6").get();
		
		assertThat(b2.getIsbn13()).isEqualTo(b1.getIsbn13());
		assertThat(b2.getAuthor()).isEqualTo("author7");
		assertThat(b2.getTitle()).isEqualTo("title7");
		assertThat(b2.getComments()).isEqualTo(b1.getComments());
	}
	
	/**
	 * Assert update with changing isbn field is successful
	 * <ul>
	 * <li>Book with old isbn value must not be in book table</li>
	 * <li>Book with new isbn value must be in book table</li>
	 * <li>Book table size must not be changed</li>
	 * <li>Updated book must hold the new vales</li>
	 * <li>Updated book must have no comments</li>
	 * </ul>
	 */
	@Test
	public void test_updateBookSuccessful_IsbnChanged() {
		Book b1 = new Book("isbn8","title8","author8");
		bookService.addBook(b1);
		bookService.addComment("isbn8", "good book");
		
		b1 = bookService.getBook("isbn8").get();
		
		int before = bookService.getAllBooks().size();
		
		bookService.updateBook("isbn8", "isbn9", "title9", "author9");
		
		int after = bookService.getAllBooks().size();
		
		assertThat(before).isEqualTo(after);
		assertThat(bookService.getBook("isbn8")).isEmpty();
		Book b2 = bookService.getBook("isbn9").get();
		
		assertThat(b2.getIsbn13()).isNotEqualTo(b1.getIsbn13());
		assertThat(b2.getAuthor()).isEqualTo("author9");
		assertThat(b2.getTitle()).isEqualTo("title9");
		assertThat(b2.getComments()).isEmpty();
	}
	
	/**
	 * Assert adding comment in book is successful
	 * <ul>
	 * 	<li>Comment size of the book must be increased by 1</li>
	 * 	<li>Added comment must have the same entered content</li>
	 * </ul>
 	 */
	@Test
	public void test_addComment() {
		Book b1 = new Book("isbn10","title10","author10");
		bookService.addBook(b1);
		bookService.addComment("isbn10", "good book");
		
		b1 = bookService.getBook("isbn10").get();
		assertThat(b1.getComments()).isNotEmpty();
		assertThat(b1.getComments()).hasSize(1);
		
		Comment c1 = b1.getComments().get(0);
		assertThat(c1.getContent()).isEqualTo("good book");
	}
	
	/**
	 * Assert comments of book returned with true order
	 * <ul>
	 * 	<li>Comment size of the book must be increased by 2</li>
	 * 	<li>Second inserted comment must be the first element in comments list</li>
	 * 	<li>First inserted comment must be the second element in comments list</li>
	 * </ul>
	 */
	@Test
	public void test_getAllCommentsOrderedWithCreationTime() {
		Book b1 = new Book("isbn11","title11","author11");
		bookService.addBook(b1);
		bookService.addComment("isbn11", "first");
		bookService.addComment("isbn11", "second");
		
		List<Comment> commentsOfb1 = bookService.getAllComments("isbn11");
		assertThat(commentsOfb1).isNotEmpty();
		assertThat(commentsOfb1).hasSize(2);
		
		Comment c1 = commentsOfb1.get(0);
		Comment c2 = commentsOfb1.get(1);
		assertThat(c1.getContent()).isEqualTo("second");
		assertThat(c2.getContent()).isEqualTo("first");
	}

}
