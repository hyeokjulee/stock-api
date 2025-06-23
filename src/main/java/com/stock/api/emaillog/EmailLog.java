package com.stock.api.emaillog;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "email_logs")
public class EmailLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "stock_alert_id")
    private Long stockAlertId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(nullable = false, length = 50)
    private String subject;

    @Column(nullable = false, length = 50)
    private String content;

    @Column(nullable = false)
    private boolean success;

    @Column(name = "error_message", length = 500)
    private String errorMessage;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public EmailLog(Long stockAlertId, Long userId, String subject, String content, boolean success, String errorMessage) {
        this.stockAlertId = stockAlertId;
        this.userId = userId;
        this.subject = subject;
        this.content = content;
        this.success = success;
        this.errorMessage = errorMessage;
    }
}