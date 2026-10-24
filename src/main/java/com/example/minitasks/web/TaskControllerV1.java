package com.example.minitasks.web;

import com.example.minitasks.dto.CreateTaskDTO;
import com.example.minitasks.model.SimpleTask;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/v1")
public class TaskControllerV1 {

    private final List<SimpleTask> inMemory = new ArrayList<>();
    private final AtomicLong seq = new AtomicLong(0);

    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }

    @GetMapping("/sample")
    public Map<String, Object> sample() {
        return Map.of("message", "hola", "time", System.currentTimeMillis());
    }

    @GetMapping("/tasks")
    public List<SimpleTask> list() {
        return inMemory;
    }

    @PostMapping("/tasks")
    @ResponseStatus(HttpStatus.CREATED)
    public SimpleTask create(@RequestBody CreateTaskDTO dto) {
        SimpleTask task = new SimpleTask(seq.incrementAndGet(), dto.title(), false);
        inMemory.add(task);
        return task;
    }

    @DeleteMapping("/tasks/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        inMemory.removeIf(task -> Objects.equals(task.getId(), id));
    }
}
