package com.ignite.assignment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ignite.assignment.model.Book;

/**
 * 
 * @author Denizalp KAPISIZ (denizalp1634@gmail.com)
 * <p>Access methods to operate book database via Spring Management</p>
 * 
 */
@Repository
public interface BookRepository extends JpaRepository<Book, String> {

}
