package com.example.minitasks.service;

import com.example.minitasks.dto.CreateTaskDTO;
import com.example.minitasks.model.SimpleTask;
import com.example.minitasks.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository repository;

    public TaskService(TaskRepository repository) {
        this.repository = repository;
    }

    public SimpleTask create(CreateTaskDTO dto) {
        return repository.save(new SimpleTask(null, dto.title(), false));
    }

    public List<SimpleTask> list() {
        return repository.findAll();
    }

    public SimpleTask getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Task not found"));
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
