package com.ignite.assignment.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

/**
 * 
 * @author Denizalp KAPISIZ (denizalp1634@gmail.com)
 * <p>Comment entity class</p>
 *
 */
@Entity
@Table(name="book_comment")
public class Comment{
	
	@Id
	private String id;
	/**
	 * Content of the comment
	 */
	@NotBlank
	private String content;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="isbn13", nullable=false)
	private Book book;
	
	/**
	 * Creation time of comment
	 */
	private LocalDateTime creationTime = LocalDateTime.now();
	
	public Comment(String content,Book book) {
		this.content = content.trim();
		this.id = book.getIsbn13()+"_"+creationTime.toString();
		this.book = book;
	}
	
	public Comment() {
		
	}
	
	public String getCreationTime() {
		return creationTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
	}
	
	public LocalDateTime getCT() {
		return creationTime;
	}
	
	public String getContent() {
		return content;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * Two comments are equal iff their ids are equal
	 */
	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		if(obj == null) return false;
		if(obj instanceof Comment) {
			Comment c2 = (Comment)obj;
			return c2.getId().equals(this.getId());
		}
		return false;
	}
	
	public Comment trimFields() {
		this.content = this.content.trim();
		return this;
	}

}
