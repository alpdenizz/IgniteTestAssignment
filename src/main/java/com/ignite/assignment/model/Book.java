package com.ignite.assignment.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * 
 * @author Denizalp KAPISIZ (denizalp1634@gmail.com)
 * <p>Book entity class</p>
 *
 */
@Entity
@Table(name = "book")
public class Book {

	/***
	 * ISBN-13 field for identification of books
	 */
	@Id
	@NotEmpty
	private String isbn13;
	
	/**
	 * Title of book
	 */
	@NotEmpty
	private String title;
	
	/**
	 * Author of book
	 */
	@NotEmpty
	private String author;
	
	/**
	 * Comments of book
	 */
	@OneToMany(mappedBy="book",cascade = CascadeType.ALL)
	private List<Comment> comments = new ArrayList<Comment>();
	
	private int numberOfComments;
	
	/**
	 * Constructor
	 * @param isbn13
	 * @param title
	 * @param author
	 */
	public Book(@Valid String isbn13, @Valid String title, @Valid String author) {
		this.isbn13 = isbn13;
		this.title = title;
		this.author = author;
	}
	
	public Book() {}
	
	public String getAuthor() {
		return author;
	}
	
	public List<Comment> getComments() {
		return comments.stream().sorted((c1,c2) -> {
			return c2.getCT().compareTo(c1.getCT());
		}).collect(Collectors.toList());
	}
	
	public String getIsbn13() {
		return isbn13;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setAuthor(String author) {
		this.author = author;
	}
	
	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}
	
	public void setIsbn13(String isbn13) {
		this.isbn13 = isbn13;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public void addComment(String content) {
		comments.add(new Comment(content,this));
		setNumberOfComments();
	}
	
	public int getNumberOfComments() {
		return this.numberOfComments;
	}
	
	public void setNumberOfComments() {
		this.numberOfComments = comments.size();
	}
	
	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		if(obj == null) return false;
		if(obj instanceof Book) {
			Book b2 = (Book) obj;
			return b2.getIsbn13().equals(this.isbn13);
		}
		return false;
	}
	
}
