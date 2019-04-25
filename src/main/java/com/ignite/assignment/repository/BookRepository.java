package com.ignite.assignment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ignite.assignment.model.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, String> {

}
