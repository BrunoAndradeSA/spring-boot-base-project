package com.brunoandradesa.api.shared.util;

import java.util.List;
import java.util.Set;

public abstract class AppConstants {

  public static final int SC_OK = 0;
  public static final int SC_GENERIC_ERROR = -99;

  public static final String REGEXP_DATE = "\\d{4}-\\d{2}-\\d{2}";

  public static final Set<String> SENSITIVE_HEADERS =
      Set.of("authorization", "cookie", "set-cookie", "x-api-key");

  public static final List<String> REQUEST_LOGS_EXCLUDED_PATHS =
      List.of("/api/auth", "/api/changelog", "/api/swagger-ui", "/api/v3/api-docs");
}
