package com.example.minitasks.web;

import com.example.minitasks.dto.CreateTaskDTO;
import com.example.minitasks.model.SimpleTask;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/v2")
public class TaskControllerV2 {

    private final List<SimpleTask> inMemory = new ArrayList<>();
    private final AtomicLong seq = new AtomicLong(0);

    @GetMapping("/sample")
    public ResponseEntity<Map<String, Object>> sample() {
        return ResponseEntity.ok(Map.of("message", "hola", "time", System.currentTimeMillis()));
    }

    @GetMapping("/tasks")
    public ResponseEntity<List<SimpleTask>> list() {
        return ResponseEntity.ok(inMemory);
    }

    @PostMapping("/tasks")
    public ResponseEntity<SimpleTask> create(@RequestBody CreateTaskDTO dto) {
        SimpleTask task = new SimpleTask(seq.incrementAndGet(), dto.title(), false);
        inMemory.add(task);
        return ResponseEntity.created(URI.create("/v2/tasks/" + task.getId())).body(task);
    }

    @DeleteMapping("/tasks/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        inMemory.removeIf(task -> Objects.equals(task.getId(), id));
        return ResponseEntity.noContent().build();
    }
}
