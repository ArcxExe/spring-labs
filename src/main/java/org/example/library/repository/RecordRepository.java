package org.example.library.repository;

import org.example.library.entity.Record;

import java.util.UUID;

public interface RecordRepository {

  void add(Record record);

  void update(Record record);

  void delete(UUID id);

  Record findById(UUID id);

  Record[] findAll();
}
