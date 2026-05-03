package com.brunoandradesa.api.repository;

import com.brunoandradesa.api.domain.logs.RequestLogs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestLogsRepository
    extends JpaRepository<RequestLogs, Long>, JpaSpecificationExecutor<RequestLogs> {}
