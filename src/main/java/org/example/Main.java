package org.example;

import org.example.library.entity.Book;
import org.example.library.entity.Reader;
import org.example.library.entity.Record;
import org.example.library.repository.BookRepository;
import org.example.library.repository.ReaderRepository;
import org.example.library.service.LibraryService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.time.LocalDate;
import java.util.UUID;

public class Main {
  public static void main(String[] args) {
    // 1. Инициализация Spring контекста (Constructor Injection для LibraryService)
    var context = new AnnotationConfigApplicationContext(ProjectConfig.class);

    LibraryService libraryService = context.getBean(LibraryService.class);
    BookRepository bookRepository = context.getBean(BookRepository.class);
    ReaderRepository readerRepository = context.getBean(ReaderRepository.class);

    System.out.println("=== Spring Context успешно инициализирован ===\n");

    // 2. Создание читателей
    Reader reader1 = new Reader(123456, false, "Иванов", "Иван", "Иванович", UUID.randomUUID());
    Reader reader2 = new Reader(654321, false, "Петров", "Петр", "Сергеевич", UUID.randomUUID());
    Reader readerDeleted = new Reader(999999, true, "Сидоров", "Сидор", "Сидорович", UUID.randomUUID());

    readerRepository.add(reader1);
    readerRepository.add(reader2);
    readerRepository.add(readerDeleted);

    // 3. Создание книг
    Book book1 = new Book(UUID.randomUUID(), "Война и мир", "Лев Толстой", 1, true, false);
    Book book2 = new Book(UUID.randomUUID(), "Преступление и наказание", "Федор Достоевский", 2, true, false);
    Book book3 = new Book(UUID.randomUUID(), "Мастер и Маргарита", "Михаил Булгаков", 1, true, false);
    Book book4 = new Book(UUID.randomUUID(), "Евгений Онегин", "Александр Пушкин", 3, true, false);

    bookRepository.add(book1);
    bookRepository.add(book2);
    bookRepository.add(book3);
    bookRepository.add(book4);

    // 4. Выдача книг
    // Выдача 1: обычная выдача в будущем
    libraryService.issueBook(book1.getId(), reader1.getId(), LocalDate.now().plusWeeks(2));

    // Выдача 2: просроченная невозвращенная книга (дата в прошлом)
    libraryService.issueBook(book2.getId(), reader1.getId(), LocalDate.now().minusDays(10));

    // Выдача 3: просроченная книга для второго читателя
    libraryService.issueBook(book3.getId(), reader2.getId(), LocalDate.now().minusDays(5));

    // Выдача 4: книга, которая будет возвращена с просрочкой
    Record record4 = libraryService.issueBook(book4.getId(), reader2.getId(), LocalDate.now().minusDays(15));

    // 5. Возврат книги (книга 4 возвращается)
    libraryService.returnBook(record4.getId());

    System.out.println("\n");

    // 6. Генерация отчета 1: список не удаленных читателей с количеством просроченных возвратов
    String report1 = libraryService.generateReadersWithOverdueReturnsReport();
    System.out.println(report1);

    // 7. Генерация отчета 2: детализированный отчет о просроченных невозвращенных книгах
    String report2 = libraryService.generateOverdueUnreturnedBooksReport();
    System.out.println(report2);

    context.close();
  }
}
