package org.example.library.service.impl;

import org.example.library.dto.OverdueBookReportItem;
import org.example.library.dto.ReaderOverdueReportItem;
import org.example.library.entity.Book;
import org.example.library.entity.Reader;
import org.example.library.entity.Record;
import org.example.library.repository.BookRepository;
import org.example.library.repository.ReaderRepository;
import org.example.library.repository.RecordRepository;
import org.example.library.service.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class LibraryServiceImpl implements LibraryService {

  private final BookRepository bookRepository;
  private final ReaderRepository readerRepository;
  private final RecordRepository recordRepository;
  private final String logFilePath;

  private static final DateTimeFormatter LOG_TIME_FORMATTER =
      DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

  @Autowired
  public LibraryServiceImpl(
      BookRepository bookRepository,
      ReaderRepository readerRepository,
      RecordRepository recordRepository) {
    this(bookRepository, readerRepository, recordRepository, "library.log");
  }

  public LibraryServiceImpl(
      BookRepository bookRepository,
      ReaderRepository readerRepository,
      RecordRepository recordRepository,
      String logFilePath) {
    this.bookRepository = bookRepository;
    this.readerRepository = readerRepository;
    this.recordRepository = recordRepository;
    this.logFilePath = (logFilePath != null && !logFilePath.isBlank()) ? logFilePath : "library.txt";
  }

  @Override
  public Record issueBook(UUID bookId, UUID readerId, LocalDate expectedReturnDate) {
    if (bookId == null || readerId == null) {
      log("ERROR", "Ошибка выдачи: передан пустой идентификатор книги или читателя");
      throw new IllegalArgumentException("Идентификатор книги и читателя не могут быть null");
    }

    Book book = bookRepository.findById(bookId);
    if (book == null || book.isDeleted()) {
      log("ERROR", "Ошибка выдачи: книга не найдена или удалена (ID: " + bookId + ")");
      throw new IllegalArgumentException("Книга не найдена или удалена: " + bookId);
    }

    if (!book.isAvailable()) {
      log("ERROR", String.format("Ошибка выдачи: книга '%s' (ID: %s) уже выдана другому читателю", book.getName(), bookId));
      throw new IllegalStateException("Книга в данный момент недоступна: " + book.getName());
    }

    Reader reader = readerRepository.findById(readerId);
    if (reader == null || reader.isDeleted()) {
      log("ERROR", "Ошибка выдачи: читатель не найден или удален (ID: " + readerId + ")");
      throw new IllegalArgumentException("Читатель не найден или удален: " + readerId);
    }

    LocalDate finalExpectedDate = expectedReturnDate != null ? expectedReturnDate : LocalDate.now().plusWeeks(2);

    book.setAvailable(false);
    bookRepository.update(book);

    Record record = new Record(
        UUID.randomUUID(),
        bookId,
        readerId,
        LocalDate.now(),
        finalExpectedDate,
        null,
        false
    );
    recordRepository.add(record);

    log("INFO", String.format(
        "Выдача книги успешна: '%s' (автор: %s, ID: %s) выдана читателю %s (ID: %s). Срок возврата: %s. Запись ID: %s",
        book.getName(),
        book.getAuthor() != null ? book.getAuthor() : "не указан",
        book.getId(),
        reader.getFullName(),
        reader.getId(),
        finalExpectedDate,
        record.getId()
    ));

    return record;
  }

  @Override
  public Record returnBook(UUID recordId) {
    if (recordId == null) {
      log("ERROR", "Ошибка возврата: передан пустой идентификатор записи");
      throw new IllegalArgumentException("Идентификатор записи не может быть null");
    }

    Record record = recordRepository.findById(recordId);
    if (record == null || record.isDeleted()) {
      log("ERROR", "Ошибка возврата: запись не найдена или удалена (ID: " + recordId + ")");
      throw new IllegalArgumentException("Запись не найдена или удалена: " + recordId);
    }

    if (record.getActualReturnDate() != null) {
      log("ERROR", "Ошибка возврата: книга по записи " + recordId + " уже была возвращена " + record.getActualReturnDate());
      throw new IllegalStateException("Книга уже была возвращена " + record.getActualReturnDate());
    }

    record.setActualReturnDate(LocalDate.now());
    recordRepository.update(record);

    Book book = bookRepository.findById(record.getBookId());
    if (book != null) {
      book.setAvailable(true);
      bookRepository.update(book);
    }

    Reader reader = readerRepository.findById(record.getReaderId());

    log("INFO", String.format(
        "Возврат книги успешен: '%s' возвращена читателем %s (Запись ID: %s, дата возврата: %s)",
        book != null ? book.getName() : record.getBookId(),
        reader != null ? reader.getFullName() : record.getReaderId(),
        record.getId(),
        record.getActualReturnDate()
    ));

    return record;
  }

  @Override
  public Record returnBook(UUID bookId, UUID readerId) {
    if (bookId == null || readerId == null) {
      log("ERROR", "Ошибка возврата: передан пустой идентификатор книги или читателя");
      throw new IllegalArgumentException("Идентификатор книги и читателя не могут быть null");
    }

    Record[] records = recordRepository.findAll();
    for (Record record : records) {
      if (record != null
          && !record.isDeleted()
          && record.getActualReturnDate() == null
          && bookId.equals(record.getBookId())
          && readerId.equals(record.getReaderId())) {
        return returnBook(record.getId());
      }
    }

    log("ERROR", String.format("Ошибка возврата: активная запись выдачи книги %s читателю %s не найдена", bookId, readerId));
    throw new IllegalArgumentException("Активная запись выдачи для указанной книги и читателя не найдена");
  }

  @Override
  public List<ReaderOverdueReportItem> getReadersWithOverdueReturns() {
    List<ReaderOverdueReportItem> report = new ArrayList<>();
    Reader[] readers = readerRepository.findAll();
    Record[] records = recordRepository.findAll();
    LocalDate now = LocalDate.now();

    for (Reader reader : readers) {
      if (reader == null || reader.isDeleted()) {
        continue;
      }

      int overdueCount = 0;
      for (Record record : records) {
        if (record != null
            && !record.isDeleted()
            && reader.getId().equals(record.getReaderId())) {

          LocalDate expectedDate = record.getExpectedReturnDate();
          if (expectedDate == null) {
            continue;
          }

          if (record.getActualReturnDate() == null) {
            if (now.isAfter(expectedDate)) {
              overdueCount++;
            }
          } else {
            if (record.getActualReturnDate().isAfter(expectedDate)) {
              overdueCount++;
            }
          }
        }
      }

      report.add(new ReaderOverdueReportItem(reader, overdueCount));
    }

    return report;
  }

  @Override
  public String generateReadersWithOverdueReturnsReport() {
    List<ReaderOverdueReportItem> items = getReadersWithOverdueReturns();
    StringBuilder sb = new StringBuilder();
    sb.append("========================================================================================\n");
    sb.append("ОТЧЕТ: СПИСОК НЕ УДАЛЕННЫХ ЧИТАТЕЛЕЙ С КОЛИЧЕСТВОМ ПРОСРОЧЕННЫХ ВОЗВРАТОВ\n");
    sb.append("========================================================================================\n");

    if (items.isEmpty()) {
      sb.append("Читатели отсутствуют.\n");
    } else {
      int index = 1;
      for (ReaderOverdueReportItem item : items) {
        sb.append(String.format("[%d] %s\n", index++, item));
      }
    }
    sb.append("========================================================================================\n");

    String result = sb.toString();
    log("INFO", "Сгенерирован отчет: список читателей с количеством просроченных возвратов (всего читателей: " + items.size() + ")");
    return result;
  }

  @Override
  public List<OverdueBookReportItem> getOverdueUnreturnedBooks() {
    List<OverdueBookReportItem> report = new ArrayList<>();
    Record[] records = recordRepository.findAll();
    LocalDate now = LocalDate.now();

    for (Record record : records) {
      if (record == null || record.isDeleted()) {
        continue;
      }

      if (record.getActualReturnDate() == null && record.getExpectedReturnDate() != null) {
        if (now.isAfter(record.getExpectedReturnDate())) {
          long overdueDays = ChronoUnit.DAYS.between(record.getExpectedReturnDate(), now);

          Book book = bookRepository.findById(record.getBookId());
          Reader reader = readerRepository.findById(record.getReaderId());

          String author = (book != null && book.getAuthor() != null) ? book.getAuthor() : "Не указан";
          String bookTitle = (book != null && book.getName() != null) ? book.getName() : "Неизвестная книга";
          String readerFullName = (reader != null) ? reader.getFullName() : "Неизвестный читатель";

          report.add(new OverdueBookReportItem(
              author,
              bookTitle,
              readerFullName,
              record.getExpectedReturnDate(),
              overdueDays
          ));
        }
      }
    }

    return report;
  }

  @Override
  public String generateOverdueUnreturnedBooksReport() {
    List<OverdueBookReportItem> items = getOverdueUnreturnedBooks();
    StringBuilder sb = new StringBuilder();
    sb.append("========================================================================================\n");
    sb.append("ДЕТАЛИЗИРОВАННЫЙ ОТЧЕТ О ПРОСРОЧЕННЫХ НЕВОЗВРАЩЕННЫХ КНИГАХ\n");
    sb.append("========================================================================================\n");

    if (items.isEmpty()) {
      sb.append("Просроченные невозвращенные книги отсутствуют.\n");
    } else {
      int index = 1;
      for (OverdueBookReportItem item : items) {
        sb.append(String.format(
            "[%d] Автор: %s\n"
                + "    Наименование: '%s'\n"
                + "    ФИО читателя: %s\n"
                + "    Дата предполагаемого возврата: %s\n"
                + "    Число дней просрочки: %d дн.\n",
            index++,
            item.getAuthor(),
            item.getBookTitle(),
            item.getReaderFullName(),
            item.getExpectedReturnDate(),
            item.getOverdueDays()
        ));
      }
    }
    sb.append("========================================================================================\n");

    String result = sb.toString();
    log("INFO", "Сгенерирован детализированный отчет о просроченных невозвращенных книгах (всего просрочено: " + items.size() + ")");
    return result;
  }

  public BookRepository getBookRepository() {
    return bookRepository;
  }

  public ReaderRepository getReaderRepository() {
    return readerRepository;
  }

  public RecordRepository getRecordRepository() {
    return recordRepository;
  }

  private synchronized void log(String level, String message) {
    String timestamp = LocalDateTime.now().format(LOG_TIME_FORMATTER);
    String line = String.format("[%s] [%s] %s%n", timestamp, level, message);
    System.out.print(line);

    try {
      Files.writeString(
          Paths.get(logFilePath),
          line,
          StandardCharsets.UTF_8,
          StandardOpenOption.CREATE,
          StandardOpenOption.APPEND
      );
    } catch (IOException e) {
      System.err.println("Не удалось записать в лог-файл: " + e.getMessage());
    }
  }
}
