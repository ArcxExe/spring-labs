package org.example.library.dto;

import java.time.LocalDate;

public class OverdueBookReportItem {

  private String author;
  private String bookTitle;
  private String readerFullName;
  private LocalDate expectedReturnDate;
  private long overdueDays;

  public OverdueBookReportItem(
      String author,
      String bookTitle,
      String readerFullName,
      LocalDate expectedReturnDate,
      long overdueDays) {
    this.author = author;
    this.bookTitle = bookTitle;
    this.readerFullName = readerFullName;
    this.expectedReturnDate = expectedReturnDate;
    this.overdueDays = overdueDays;
  }

  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  public String getBookTitle() {
    return bookTitle;
  }

  public void setBookTitle(String bookTitle) {
    this.bookTitle = bookTitle;
  }

  public String getReaderFullName() {
    return readerFullName;
  }

  public void setReaderFullName(String readerFullName) {
    this.readerFullName = readerFullName;
  }

  public LocalDate getExpectedReturnDate() {
    return expectedReturnDate;
  }

  public void setExpectedReturnDate(LocalDate expectedReturnDate) {
    this.expectedReturnDate = expectedReturnDate;
  }

  public long getOverdueDays() {
    return overdueDays;
  }

  public void setOverdueDays(long overdueDays) {
    this.overdueDays = overdueDays;
  }

  @Override
  public String toString() {
    return String.format(
        "Автор: %s | Книга: '%s' | Читатель: %s | Ожидаемая дата возврата: %s | Просрочено дней: %d",
        author != null ? author : "Не указан",
        bookTitle,
        readerFullName,
        expectedReturnDate,
        overdueDays
    );
  }
}
