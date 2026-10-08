package org.example.library.dto;

import org.example.library.entity.Reader;

public class ReaderOverdueReportItem {

  private Reader reader;
  private int overdueCount;

  public ReaderOverdueReportItem(Reader reader, int overdueCount) {
    this.reader = reader;
    this.overdueCount = overdueCount;
  }

  public Reader getReader() {
    return reader;
  }

  public void setReader(Reader reader) {
    this.reader = reader;
  }

  public int getOverdueCount() {
    return overdueCount;
  }

  public void setOverdueCount(int overdueCount) {
    this.overdueCount = overdueCount;
  }

  @Override
  public String toString() {
    return String.format(
        "Читатель: %s (ID: %s, Паспорт: %d) | Количество просроченных возвратов: %d",
        reader.getFullName(),
        reader.getId(),
        reader.getPasport(),
        overdueCount
    );
  }
}
