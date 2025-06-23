package com.stock.api.emaillog;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class EmailLogService {

    private final EmailLogRepository emailLogRepository;

    public void saveEmailLog(EmailLog emailLog) {

        emailLogRepository.save(emailLog);
    }
}