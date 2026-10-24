package com.example.minitasks.dto;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CreateTaskDTOTest {

    private static ValidatorFactory factory;
    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @AfterAll
    static void closeValidatorFactory() {
        factory.close();
    }

    @Test
    void storesTitle() {
        CreateTaskDTO dto = new CreateTaskDTO("Comprar pan");

        assertThat(dto.title()).isEqualTo("Comprar pan");
    }

    @Test
    void blankTitle_isInvalid() {
        assertThat(validator.validate(new CreateTaskDTO(" "))).isNotEmpty();
    }

    @Test
    void nonBlankTitle_isValid() {
        assertThat(validator.validate(new CreateTaskDTO("Comprar pan"))).isEmpty();
    }
}
