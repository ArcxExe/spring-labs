package org.example;

import org.example.library.Book;
import org.example.library.ProjectConfig;
import org.example.library.Reader;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
  public static void main(String[] args) {

    var context = new AnnotationConfigApplicationContext(ProjectConfig.class);
    Reader reader = context.getBean(Reader.class);
    System.out.println(reader);
  }
}
