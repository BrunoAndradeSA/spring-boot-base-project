package com.brunoandradesa.api.domain.logs;

import java.time.LocalDate;
import java.time.LocalDateTime;
import org.springframework.data.jpa.domain.Specification;

public class RequestLogsSpecification {

  public static Specification<RequestLogs> pathContains(String path) {
    return (root, query, cb) -> path == null ? null : cb.like(root.get("path"), "%" + path + "%");
  }

  public static Specification<RequestLogs> usernameContains(String username) {
    return (root, query, cb) ->
        username == null ? null : cb.like(root.get("user"), "%" + username + "%");
  }

  public static <T> Specification<RequestLogs> equalsTo(String field, T value) {
    return (root, query, cb) -> value == null ? null : cb.equal(root.get(field), value);
  }

  public static Specification<RequestLogs> dateEquals(String date) {
    return (root, query, cb) -> {
      if (date == null) return null;

      LocalDate localDate = LocalDate.parse(date);

      LocalDateTime start = localDate.atStartOfDay();
      LocalDateTime end = localDate.plusDays(1).atStartOfDay();

      return cb.between(root.get("createdAt"), start, end);
    };
  }
}
