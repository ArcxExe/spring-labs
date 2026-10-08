package org.example.library.repository;

import org.example.library.entity.Reader;

import java.util.UUID;

public interface ReaderRepository {

  void add(Reader reader);

  void update(Reader reader);

  void delete(UUID id);

  Reader findById(UUID id);

  Reader[] findAll();
}
