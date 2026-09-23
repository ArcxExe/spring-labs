package org.example;

import org.example.library.Book;
import org.example.library.ProjectConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
  public static void main(String[] args) {

    var context = new AnnotationConfigApplicationContext(ProjectConfig.class);
    Book book = context.getBean(Book.class);

    System.out.println(book);
  }
}
