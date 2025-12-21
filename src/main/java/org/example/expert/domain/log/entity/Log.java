package org.example.expert.domain.log.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "log")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Log {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long todoId;

    @Column(nullable = false)
    private Long managerId;

    private String description;

    private LocalDateTime timestamp;

    public Log(Long userId, Long todoId, Long managerId, String description) {
        this.userId = userId;
        this.todoId = todoId;
        this.managerId = managerId;
        this.description = description;
        this.timestamp = LocalDateTime.now();
    }
}
