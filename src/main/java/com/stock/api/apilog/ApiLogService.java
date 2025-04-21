package com.stock.api.apilog;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ApiLogService {

    private final ApiLogRepository apiLogRepository;

    public void saveApiLog(ApiLog apiLog) {

        apiLogRepository.save(apiLog);
    }
}
