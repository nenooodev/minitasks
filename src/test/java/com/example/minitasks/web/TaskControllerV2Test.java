package com.example.minitasks.web;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskControllerV2.class)
class TaskControllerV2Test {

    @Autowired
    MockMvc mvc;

    @Test
    void sample_returnsMessageAndTime() throws Exception {
        mvc.perform(get("/v2/sample"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("hola"))
                .andExpect(jsonPath("$.time").exists());
    }

    @Test
    void list_returnsCreatedTask() throws Exception {
        String title = "Listado V2";
        mvc.perform(post("/v2/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"" + title + "\"}"))
                .andExpect(status().isCreated());

        mvc.perform(get("/v2/tasks"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[*].title", hasItem(title)));
    }

    @Test
    void create_returns201_withLocation() throws Exception {
        MvcResult result = mvc.perform(post("/v2/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Test V2\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.title").value("Test V2"))
                .andExpect(jsonPath("$.done").value(false))
                .andReturn();

        Number id = JsonPath.read(result.getResponse().getContentAsString(), "$.id");
        assertThat(result.getResponse().getHeader("Location"))
                .isEqualTo("/v2/tasks/" + id.longValue());
    }
}
