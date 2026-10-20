package org.example.minimini;

public class Task {
    public final long id;
    public String title;
    Boolean completed = false;
    public Task(long id, String title) {
        this.id = id;
        this.title = title;
    }
    public void toggle() {
        this.completed = !this.completed;
    }

    public boolean isCompleted() {
        return completed;
    }
}

