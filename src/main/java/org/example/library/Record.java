package org.example.library;

import java.time.LocalDate;

/** Record */
public class Record {

  private LocalDate returnDate;
  private LocalDate borrowDate;
  private Book book;
  private Reader reader;

  public Record() {}

  public Record(LocalDate returnDate, Book book, Reader reader) {
    this.borrowDate = LocalDate.now();
    this.returnDate = returnDate;
    this.book = book;
    this.reader = reader;
  }

  public LocalDate getReturnDate() {
    return returnDate;
  }

  public void setReturnDate(LocalDate returnDate) {
    this.returnDate = returnDate;
  }

  public LocalDate getBorrowDate() {
    return borrowDate;
  }

  public void setBorrowDate(LocalDate borrowDate) {
    this.borrowDate = borrowDate;
  }

  public Book getBook() {
    return book;
  }

  public void setBook(Book book) {
    this.book = book;
  }

  public Reader getReader() {
    return reader;
  }

  public void setReader(Reader reader) {
    this.reader = reader;
  }

  @Override
  public String toString() {
    return "Record{returnDate="
        + returnDate
        + ", borrowDate="
        + borrowDate
        + ", book="
        + book
        + ", reader="
        + reader
        + "}";
  }
}
