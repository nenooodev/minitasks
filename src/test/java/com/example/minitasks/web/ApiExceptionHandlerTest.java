package com.example.minitasks.web;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class ApiExceptionHandlerTest {

    private final ApiExceptionHandler handler = new ApiExceptionHandler();

    @Test
    void illegalArgument_returns404WithMessage() {
        ResponseEntity<Map<String, String>> response =
                handler.handleIllegalArgument(new IllegalArgumentException("Task not found"));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).containsEntry("error", "Task not found");
    }

    @Test
    void validationError_returns400WithFieldDetails() {
        BeanPropertyBindingResult bindingResult =
                new BeanPropertyBindingResult(new Object(), "createTaskDTO");
        bindingResult.addError(new FieldError("createTaskDTO", "title", "must not be blank"));
        MethodArgumentNotValidException ex =
                new MethodArgumentNotValidException(null, bindingResult);

        ResponseEntity<Map<String, Object>> response = handler.handleValidation(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).containsEntry("error", "Validation failed");
        @SuppressWarnings("unchecked")
        List<Map<String, String>> fields =
                (List<Map<String, String>>) response.getBody().get("fields");
        assertThat(fields).singleElement().satisfies(field -> {
            assertThat(field).containsEntry("field", "title");
            assertThat(field).containsEntry("message", "must not be blank");
        });
    }
}
