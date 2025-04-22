package com.stock.api.loginlog;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginLogService {

    private final LoginLogRepository loginLogRepository;

    public void saveLoginLog(Long userId) {

        loginLogRepository.save(new LoginLog(userId));
    }
}