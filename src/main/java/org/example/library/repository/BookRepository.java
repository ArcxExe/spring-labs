package org.example.library.repository;

import org.example.library.entity.Book;

import java.util.UUID;

public interface BookRepository {

  void add(Book book);

  void update(Book book);

  void delete(UUID id);

  Book findById(UUID id);

  Book[] findAll();
}
