package org.example.library.service;

import org.example.library.dto.OverdueBookReportItem;
import org.example.library.dto.ReaderOverdueReportItem;
import org.example.library.entity.Record;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface LibraryService {

  /**
   * Выдача книги читателю.
   *
   * @param bookId             идентификатор книги
   * @param readerId           идентификатор читателя
   * @param expectedReturnDate предполагаемая дата возврата
   * @return созданная учетная запись выдачи
   */
  Record issueBook(UUID bookId, UUID readerId, LocalDate expectedReturnDate);

  /**
   * Возврат книги по идентификатору записи.
   *
   * @param recordId идентификатор учетной записи
   * @return обновленная учетная запись
   */
  Record returnBook(UUID recordId);

  /**
   * Возврат книги по идентификаторам книги и читателя.
   *
   * @param bookId   идентификатор книги
   * @param readerId идентификатор читателя
   * @return обновленная учетная запись
   */
  Record returnBook(UUID bookId, UUID readerId);

  /**
   * Получение списка неудаленных читателей с количеством просроченных возвратов.
   */
  List<ReaderOverdueReportItem> getReadersWithOverdueReturns();

  /**
   * Генерация текстового отчета: список неудаленных читателей с количеством просроченных возвратов.
   */
  String generateReadersWithOverdueReturnsReport();

  /**
   * Получение детализированного списка просроченных невозвращенных книг.
   */
  List<OverdueBookReportItem> getOverdueUnreturnedBooks();

  /**
   * Генерация детализированного текстового отчета о просроченных невозвращенных книгах.
   */
  String generateOverdueUnreturnedBooksReport();
}
