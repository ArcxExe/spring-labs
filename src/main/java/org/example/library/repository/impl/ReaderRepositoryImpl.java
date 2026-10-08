package org.example.library.repository.impl;

import org.example.library.entity.Reader;
import org.example.library.repository.ReaderRepository;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.UUID;

@Repository
public class ReaderRepositoryImpl implements ReaderRepository {

  private static final int DEFAULT_CAPACITY = 10;
  private Reader[] readers;
  private int size;

  public ReaderRepositoryImpl() {
    this(DEFAULT_CAPACITY);
  }

  public ReaderRepositoryImpl(int capacity) {
    this.readers = new Reader[capacity > 0 ? capacity : DEFAULT_CAPACITY];
    this.size = 0;
  }

  @Override
  public void add(Reader reader) {
    if (reader == null) {
      return;
    }
    if (reader.getId() == null) {
      reader.setId(UUID.randomUUID());
    }
    if (size == readers.length) {
      grow();
    }
    readers[size++] = reader;
  }

  @Override
  public void update(Reader reader) {
    if (reader == null || reader.getId() == null) {
      return;
    }
    for (int i = 0; i < size; i++) {
      if (readers[i] != null && reader.getId().equals(readers[i].getId())) {
        readers[i] = reader;
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
      if (readers[i] != null && id.equals(readers[i].getId())) {
        readers[i].setDeleted(true);
        return;
      }
    }
  }

  public void hardDelete(UUID id) {
    if (id == null) {
      return;
    }
    for (int i = 0; i < size; i++) {
      if (readers[i] != null && id.equals(readers[i].getId())) {
        int numMoved = size - i - 1;
        if (numMoved > 0) {
          System.arraycopy(readers, i + 1, readers, i, numMoved);
        }
        readers[--size] = null;
        return;
      }
    }
  }

  @Override
  public Reader findById(UUID id) {
    if (id == null) {
      return null;
    }
    for (int i = 0; i < size; i++) {
      if (readers[i] != null && id.equals(readers[i].getId())) {
        return readers[i];
      }
    }
    return null;
  }

  @Override
  public Reader[] findAll() {
    return Arrays.copyOf(readers, size);
  }

  public Reader[] findAllActive() {
    int activeCount = 0;
    for (int i = 0; i < size; i++) {
      if (readers[i] != null && !readers[i].isDeleted()) {
        activeCount++;
      }
    }
    Reader[] activeReaders = new Reader[activeCount];
    int index = 0;
    for (int i = 0; i < size; i++) {
      if (readers[i] != null && !readers[i].isDeleted()) {
        activeReaders[index++] = readers[i];
      }
    }
    return activeReaders;
  }

  public int size() {
    return size;
  }

  private void grow() {
    readers = Arrays.copyOf(readers, readers.length * 2);
  }
}
