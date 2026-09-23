package org.example;

import org.example.library.Book;
import org.example.library.Reader;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
  public static void main(String[] args) {
    var context = new AnnotationConfigApplicationContext();

    context.registerBean("myBook", Book.class, () -> {
      Book book = new Book();
      book.setName("Чистый код");
      book.setAuthor("Роберт Мартин");
      book.setAvailable(true);
      book.setEdition(2019);
      return book;
    });

    context.registerBean("myReader", Reader.class, () -> {
      Reader reader = new Reader();
      reader.setFullname("Иван Иванов");
      reader.setPassportNumber("1234 567890");
      return reader;
    });

    context.refresh();

    Book book = context.getBean("myBook", Book.class);
    Reader reader = context.getBean("myReader", Reader.class);

    System.out.println("Извлеченная книга: " + book);
    System.out.println("Извлеченный читатель: " + reader);

    context.close();
  }
}