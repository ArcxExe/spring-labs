package org.example.library.repository.impl;

import org.example.library.entity.Book;
import org.example.library.repository.BookRepository;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.UUID;

@Repository
public class BookRepositoryImpl implements BookRepository {

  private static final int DEFAULT_CAPACITY = 10;
  private Book[] books;
  private int size;

  public BookRepositoryImpl() {
    this(DEFAULT_CAPACITY);
  }

  public BookRepositoryImpl(int capacity) {
    this.books = new Book[capacity > 0 ? capacity : DEFAULT_CAPACITY];
    this.size = 0;
  }

  @Override
  public void add(Book book) {
    if (book == null) {
      return;
    }
    if (book.getId() == null) {
      book.setId(UUID.randomUUID());
    }
    if (size == books.length) {
      grow();
    }
    books[size++] = book;
  }

  @Override
  public void update(Book book) {
    if (book == null || book.getId() == null) {
      return;
    }
    for (int i = 0; i < size; i++) {
      if (books[i] != null && book.getId().equals(books[i].getId())) {
        books[i] = book;
        return;
      }
    }
  }

  @Override
  public void delete(UUID id) {
    if (id == null) {
      return;
    }
    for (int i = 0; i < size; i++) {
      if (books[i] != null && id.equals(books[i].getId())) {
        books[i].setDeleted(true);
        return;
      }
    }
  }

  public void hardDelete(UUID id) {
    if (id == null) {
      return;
    }
    for (int i = 0; i < size; i++) {
      if (books[i] != null && id.equals(books[i].getId())) {
        int numMoved = size - i - 1;
        if (numMoved > 0) {
          System.arraycopy(books, i + 1, books, i, numMoved);
        }
        books[--size] = null;
        return;
      }
    }
  }

  @Override
  public Book findById(UUID id) {
    if (id == null) {
      return null;
    }
    for (int i = 0; i < size; i++) {
      if (books[i] != null && id.equals(books[i].getId())) {
        return books[i];
      }
    }
    return null;
  }

  @Override
  public Book[] findAll() {
    return Arrays.copyOf(books, size);
  }

  public Book[] findAllActive() {
    int activeCount = 0;
    for (int i = 0; i < size; i++) {
      if (books[i] != null && !books[i].isDeleted()) {
        activeCount++;
      }
    }
    Book[] activeBooks = new Book[activeCount];
    int index = 0;
    for (int i = 0; i < size; i++) {
      if (books[i] != null && !books[i].isDeleted()) {
        activeBooks[index++] = books[i];
      }
    }
    return activeBooks;
  }

  public int size() {
    return size;
  }

  private void grow() {
    books = Arrays.copyOf(books, books.length * 2);
  }
}
