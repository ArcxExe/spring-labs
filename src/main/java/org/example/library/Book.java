package org.example.library;

public class Book {
  private String name;
  private boolean isAvailable;
  private int edition;
  private long id;
  private String author;

  public Book(String name, boolean isAvailable, int edition, String author) {
    this.name = name;
    this.isAvailable = isAvailable;
    this.edition = edition;
    this.author = author;
  }

  public Book() {}

  public boolean isAvailable() {
    return isAvailable;
  }

  public void setAvailable(boolean isAvailable) {
    this.isAvailable = isAvailable;
  }

  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getEdition() {
    return edition;
  }

  public void setEdition(int edition) {
    this.edition = edition;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  @Override
  public String toString() {
    return "Book{name=" + name + ", isAvailable=" + isAvailable + ", edition=" + edition + "}";
  }
}
