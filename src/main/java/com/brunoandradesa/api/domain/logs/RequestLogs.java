package com.brunoandradesa.api.domain.logs;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Data;

@Entity
@Table(name = "request_logs")
@Data
public class RequestLogs {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "method", nullable = false)
  private String method;

  @Column(name = "path", nullable = false)
  private String path;

  @Column(name = "status", nullable = false)
  private Integer status;

  @Column(name = "user_name")
  private String user;

  @Column(name = "duration_ms", nullable = false)
  private Long duration;

  @Column(name = "request_body", columnDefinition = "TEXT")
  private String requestBody;

  @Column(name = "response_body", columnDefinition = "TEXT")
  private String responseBody;

  @Column(name = "headers", columnDefinition = "TEXT")
  private String headers;

  @Column(name = "curl", columnDefinition = "TEXT", nullable = false)
  private String curl;

  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;
}
