package org.example.expert.domain.todo.dto.request;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class TodoPageRequest {
    private final String title;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final String nickname;
    private final Integer page;
    private final Integer size;


    public TodoPageRequest(String title, LocalDate startDate, LocalDate endDate, String nickname, Integer page, Integer size) {
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.nickname = nickname;
        this.page = page == null ? 0 : page;
        this.size = size == null ? 10 : size;
    }
}
