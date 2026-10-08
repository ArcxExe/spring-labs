package org.example.library.repository.impl;

import org.example.library.entity.Record;
import org.example.library.repository.RecordRepository;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.UUID;

@Repository
public class RecordRepositoryImpl implements RecordRepository {

  private static final int DEFAULT_CAPACITY = 10;
  private Record[] records;
  private int size;

  public RecordRepositoryImpl() {
    this(DEFAULT_CAPACITY);
  }

  public RecordRepositoryImpl(int capacity) {
    this.records = new Record[capacity > 0 ? capacity : DEFAULT_CAPACITY];
    this.size = 0;
  }

  @Override
  public void add(Record record) {
    if (record == null) {
      return;
    }
    if (record.getId() == null) {
      record.setId(UUID.randomUUID());
    }
    if (size == records.length) {
      grow();
    }
    records[size++] = record;
  }

  @Override
  public void update(Record record) {
    if (record == null || record.getId() == null) {
      return;
    }
    for (int i = 0; i < size; i++) {
      if (records[i] != null && record.getId().equals(records[i].getId())) {
        records[i] = record;
        return;
      }
    }
  }

  @Override
  public void delete(UUID id) {
    if (id == null) {
      return;
    }
    for (int i = 0; i < size; i++) {
      if (records[i] != null && id.equals(records[i].getId())) {
        records[i].setDeleted(true);
        return;
      }
    }
  }

  public void hardDelete(UUID id) {
    if (id == null) {
      return;
    }
    for (int i = 0; i < size; i++) {
      if (records[i] != null && id.equals(records[i].getId())) {
        int numMoved = size - i - 1;
        if (numMoved > 0) {
          System.arraycopy(records, i + 1, records, i, numMoved);
        }
        records[--size] = null;
        return;
      }
    }
  }

  @Override
  public Record findById(UUID id) {
    if (id == null) {
      return null;
    }
    for (int i = 0; i < size; i++) {
      if (records[i] != null && id.equals(records[i].getId())) {
        return records[i];
      }
    }
    return null;
  }

  @Override
  public Record[] findAll() {
    return Arrays.copyOf(records, size);
  }

  public Record[] findAllActive() {
    int activeCount = 0;
    for (int i = 0; i < size; i++) {
      if (records[i] != null && !records[i].isDeleted()) {
        activeCount++;
      }
    }
    Record[] activeRecords = new Record[activeCount];
    int index = 0;
    for (int i = 0; i < size; i++) {
      if (records[i] != null && !records[i].isDeleted()) {
        activeRecords[index++] = records[i];
      }
    }
    return activeRecords;
  }

  public int size() {
    return size;
  }

  private void grow() {
    records = Arrays.copyOf(records, records.length * 2);
  }
}
