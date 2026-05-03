package com.brunoandradesa.api.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class ChangelogControllerTest {

  @Test
  @DisplayName("should return HTML when changelog file exists")
  void shouldReturnHtmlWhenChangelogExists() throws Exception {
    ChangelogController controller = new ChangelogController();
    MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

    mockMvc
        .perform(get("/changelog"))
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith("text/html"))
        .andExpect(content().string(org.hamcrest.Matchers.containsString("<!DOCTYPE html>")));
  }
}