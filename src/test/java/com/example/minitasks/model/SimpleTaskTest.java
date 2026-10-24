package com.example.minitasks.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SimpleTaskTest {

    @Test
    void constructor_storesIdTitleAndDone() {
        SimpleTask task = new SimpleTask(1L, "Comprar pan", false);

        assertThat(task.getId()).isEqualTo(1L);
        assertThat(task.getTitle()).isEqualTo("Comprar pan");
        assertThat(task.isDone()).isFalse();
    }

    @Test
    void setters_updateValues() {
        SimpleTask task = new SimpleTask();
        task.setId(2L);
        task.setTitle("Estudiar");
        task.setDone(true);

        assertThat(task.getId()).isEqualTo(2L);
        assertThat(task.getTitle()).isEqualTo("Estudiar");
        assertThat(task.isDone()).isTrue();
    }
}
