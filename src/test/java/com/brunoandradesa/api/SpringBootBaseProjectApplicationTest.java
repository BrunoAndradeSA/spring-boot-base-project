package com.brunoandradesa.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@SpringBootTest(properties = "spring.config.location=classpath:application-test.yaml")
class SpringBootBaseProjectApplicationTest {

  @SuppressWarnings("unused")
  @Autowired
  private ApplicationContext context;

  @Test
  void contextLoads() {}
}
