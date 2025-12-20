package org.example.expert.domain.todo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TodoPageResponse {
    private String title;
    private long managerCount;
    private long commentCount;
}
