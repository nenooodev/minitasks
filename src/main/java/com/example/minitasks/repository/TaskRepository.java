package com.example.minitasks.repository;

import com.example.minitasks.model.SimpleTask;

import java.util.List;
import java.util.Optional;

public interface TaskRepository {

    SimpleTask save(SimpleTask task);

    List<SimpleTask> findAll();

    Optional<SimpleTask> findById(Long id);

    void deleteById(Long id);
}
