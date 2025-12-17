package org.example.expert.domain.todo.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class TodoGetPageRequest {
    private String weather;
    private LocalDate startDate;
    private LocalDate endDate;
}
