package org.example.library.entity;

import org.springframework.stereotype.Component;

import java.util.UUID;


public class Reader {
  private UUID id;
  private String name;
  private String firstName;
  private String lastName;
  private boolean isDeleted = false;
  private int pasport;

  public Reader() {}

  public Reader(int pasport, boolean isDeleted, String lastName, String firstName, String name, UUID id) {
    this.pasport = pasport;
    this.isDeleted = isDeleted;
    this.lastName = lastName;
    this.firstName = firstName;
    this.name = name;
    this.id = id;
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

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public boolean isDeleted() {
    return isDeleted;
  }

  public void setDeleted(boolean deleted) {
    isDeleted = deleted;
  }

  public int getPasport() {
    return pasport;
  }

  public void setPasport(int pasport) {
    this.pasport = pasport;
  }

  public String getFullName() {
    StringBuilder sb = new StringBuilder();
    if (lastName != null && !lastName.isBlank()) {
      sb.append(lastName).append(" ");
    }
    if (firstName != null && !firstName.isBlank()) {
      sb.append(firstName).append(" ");
    }
    if (name != null && !name.isBlank()) {
      sb.append(name);
    }
    String full = sb.toString().trim();
    return full.isEmpty() ? "Неизвестный читатель" : full;
  }
}