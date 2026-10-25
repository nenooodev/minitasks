package com.example.minitasks.repository;

import com.example.minitasks.model.SimpleTask;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class InMemoryTaskRepository implements TaskRepository {

    private final List<SimpleTask> tasks = new ArrayList<>();
    private final AtomicLong seq = new AtomicLong(0);

    @Override
    public SimpleTask save(SimpleTask task) {
        if (task.getId() == null) {
            task.setId(seq.incrementAndGet());
        }
        tasks.add(task);
        return task;
    }

    @Override
    public List<SimpleTask> findAll() {
        return tasks;
    }

    @Override
    public Optional<SimpleTask> findById(Long id) {
        return tasks.stream()
                .filter(task -> Objects.equals(task.getId(), id))
                .findFirst();
    }

    @Override
    public void deleteById(Long id) {
        tasks.removeIf(task -> Objects.equals(task.getId(), id));
    }
}
