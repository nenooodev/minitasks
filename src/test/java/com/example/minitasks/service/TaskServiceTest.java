package com.example.minitasks.service;

import com.example.minitasks.dto.CreateTaskDTO;
import com.example.minitasks.model.SimpleTask;
import com.example.minitasks.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    TaskRepository repository;

    @InjectMocks
    TaskService service;

    @Test
    void createStripsTitleEdges() {
        when(repository.save(any(SimpleTask.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        SimpleTask created = service.create(new CreateTaskDTO("  Repasar Spring Boot  "));

        assertThat(created.getTitle()).isEqualTo("Repasar Spring Boot");
    }

    @Test
    void createKeepsInnerSpacesAndDoneFalse() {
        when(repository.save(any(SimpleTask.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        SimpleTask created = service.create(new CreateTaskDTO("Repasar Spring Boot"));

        assertThat(created.getTitle()).isEqualTo("Repasar Spring Boot");
        assertThat(created.isDone()).isFalse();
    }
}
