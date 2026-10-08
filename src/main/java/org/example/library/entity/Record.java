package org.example.library.entity;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.UUID;


public class Record {

  private UUID id;
  private UUID bookId;
  private UUID readerId;
  private LocalDate issueDate;
  private LocalDate expectedReturnDate;
  private LocalDate actualReturnDate;
  private boolean isDeleted = false;

  public Record() {}

  public Record(UUID id, UUID bookId, UUID readerId, LocalDate issueDate, LocalDate expectedReturnDate, LocalDate actualReturnDate, boolean isDeleted) {
    this.id = id;
    this.bookId = bookId;
    this.readerId = readerId;
    this.issueDate = issueDate;
    this.expectedReturnDate = expectedReturnDate;
    this.actualReturnDate = actualReturnDate;
    this.isDeleted = isDeleted;
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public UUID getBookId() {
    return bookId;
  }

  public void setBookId(UUID bookId) {
    this.bookId = bookId;
  }

  public UUID getReaderId() {
    return readerId;
  }

  public void setReaderId(UUID readerId) {
    this.readerId = readerId;
  }

  public LocalDate getIssueDate() {
    return issueDate;
  }

  public void setIssueDate(LocalDate issueDate) {
    this.issueDate = issueDate;
  }

  public LocalDate getExpectedReturnDate() {
    return expectedReturnDate;
  }

  public void setExpectedReturnDate(LocalDate expectedReturnDate) {
    this.expectedReturnDate = expectedReturnDate;
  }

  public LocalDate getActualReturnDate() {
    return actualReturnDate;
  }

  public void setActualReturnDate(LocalDate actualReturnDate) {
    this.actualReturnDate = actualReturnDate;
  }

  public boolean isDeleted() {
    return isDeleted;
  }

  public void setDeleted(boolean deleted) {
    isDeleted = deleted;
  }
}
