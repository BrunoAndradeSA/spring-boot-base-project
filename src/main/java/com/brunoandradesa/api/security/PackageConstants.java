package com.brunoandradesa.api.security;

public abstract class PackageConstants {

  public static final String[] PUBLIC_ENDPOINTS = {
    "/health/**",
    "/auth/**",
    "/",
    "/docs/**",
    "/v3/api-docs/**",
    "/swagger-ui/**",
    "/swagger-ui.html",
    "/changelog"
  };
}
