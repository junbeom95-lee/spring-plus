package org.example.expert.domain.todo.repository;

import org.example.expert.domain.todo.dto.request.TodoPageRequest;
import org.example.expert.domain.todo.dto.response.TodoPageResponse;
import org.example.expert.domain.todo.entity.Todo;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface TodoCustomRepository {

    Optional<Todo> findByIdWithUser(Long todoId);

    Page<TodoPageResponse> findAllWithCond(TodoPageRequest request);
}
