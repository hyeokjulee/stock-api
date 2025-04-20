package com.stock.api.log.apilog;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Table(name = "api_logs")
public class ApiLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(nullable = false, length = 7)
    private String method;

    @Column(nullable = false, length = 127)
    private String url;

    @Column(name = "response_time_ms", nullable = false)
    private Integer responseTimeMs;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public ApiLog(Long userId, String method, String url, Integer responseTimeMs) {
        this.userId = userId;
        this.method = method;
        this.url = url;
        this.responseTimeMs = responseTimeMs;
    }
}
