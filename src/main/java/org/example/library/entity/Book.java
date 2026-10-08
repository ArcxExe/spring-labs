package org.example.library.entity;

import org.springframework.stereotype.Component;
import java.util.UUID;


public class Book {

  private UUID id;
  private String name;
  private String author;
  private int izdanie;
  private boolean isAvailable;
  private boolean isDeleted;

  public Book() {}

  public Book(UUID id, String name, int izdanie, boolean isAvailable, boolean isDeleted) {
    this(id, name, null, izdanie, isAvailable, isDeleted);
  }

  public Book(UUID id, String name, String author, int izdanie, boolean isAvailable, boolean isDeleted) {
    this.id = id;
    this.name = name;
    this.author = author;
    this.izdanie = izdanie;
    this.isAvailable = isAvailable;
    this.isDeleted = isDeleted;
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  public int getIzdanie() {
    return izdanie;
  }

  public void setIzdanie(int izdanie) {
    this.izdanie = izdanie;
  }

  public boolean isAvailable() {
    return isAvailable;
  }

  public void setAvailable(boolean available) {
    isAvailable = available;
  }

  public boolean isDeleted() {
    return isDeleted;
  }

  public void setDeleted(boolean deleted) {
    isDeleted = deleted;
  }
}