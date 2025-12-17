package org.example.expert.domain.todo.service;

import lombok.RequiredArgsConstructor;
import org.example.expert.client.WeatherClient;
import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.common.exception.InvalidRequestException;
import org.example.expert.domain.todo.dto.request.TodoGetPageRequest;
import org.example.expert.domain.todo.dto.request.TodoSaveRequest;
import org.example.expert.domain.todo.dto.response.TodoResponse;
import org.example.expert.domain.todo.dto.response.TodoSaveResponse;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.todo.repository.TodoRepository;
import org.example.expert.domain.user.dto.response.UserResponse;
import org.example.expert.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TodoService {

    private final TodoRepository todoRepository;
    private final WeatherClient weatherClient;

    @Transactional
    public TodoSaveResponse saveTodo(AuthUser authUser, TodoSaveRequest todoSaveRequest) {
        User user = User.fromAuthUser(authUser);

        String weather = weatherClient.getTodayWeather();

        Todo newTodo = new Todo(
                todoSaveRequest.getTitle(),
                todoSaveRequest.getContents(),
                weather,
                user
        );
        Todo savedTodo = todoRepository.save(newTodo);

        return new TodoSaveResponse(
                savedTodo.getId(),
                savedTodo.getTitle(),
                savedTodo.getContents(),
                weather,
                new UserResponse(user.getId(), user.getEmail())
        );
    }

    @Transactional(readOnly = true)
    public Page<TodoResponse> getTodos(int page, int size, TodoGetPageRequest request) {

        Pageable pageable = PageRequest.of(page - 1, size);

        boolean existWeather = request.getWeather() != null && !request.getWeather().isBlank();
        LocalDateTime start = request.getStartDate() != null ? request.getStartDate().atStartOfDay() : null;
        LocalDateTime end = request.getEndDate() != null ? request.getEndDate().plusDays(1).atStartOfDay() : null;

        boolean existStart = request.getStartDate() != null;
        boolean existEnd = request.getEndDate() != null;

        Page<Todo> todos;

        if (existWeather && existStart && existEnd) {   //날씨, 시작, 끝
            todos = todoRepository.findAllByWithWeatherStartEndDesc(pageable, request.getWeather(), start, end);
        } else if (existWeather && existStart) {        //날씨, 시작
            todos = todoRepository.findAllByWithWeatherStartDesc(pageable, request.getWeather(), start);
        } else if (existWeather && existEnd) {          //날씨, 끝
            todos = todoRepository.findAllByWithWeatherEndDesc(pageable, request.getWeather(), end);
        } else if (existStart && existEnd) {            //시작, 끝
            todos = todoRepository.findAllByWithStartEndDesc(pageable, start, end);
        } else if (existStart) {                        //시작
            todos = todoRepository.findAllByWithStartDesc(pageable, start);
        } else if (existEnd) {                          //끝
            todos = todoRepository.findAllByWithEndDesc(pageable, end);
        } else todos = todoRepository.findAllByOrderByModifiedAtDesc(pageable); //조건 없을 때

        return todos.map(todo -> new TodoResponse(
                todo.getId(),
                todo.getTitle(),
                todo.getContents(),
                todo.getWeather(),
                new UserResponse(todo.getUser().getId(), todo.getUser().getEmail()),
                todo.getCreatedAt(),
                todo.getModifiedAt()
        ));
    }

    @Transactional(readOnly = true)
    public TodoResponse getTodo(long todoId) {
        Todo todo = todoRepository.findByIdWithUser(todoId)
                .orElseThrow(() -> new InvalidRequestException("Todo not found"));

        User user = todo.getUser();

        return new TodoResponse(
                todo.getId(),
                todo.getTitle(),
                todo.getContents(),
                todo.getWeather(),
                new UserResponse(user.getId(), user.getEmail()),
                todo.getCreatedAt(),
                todo.getModifiedAt()
        );
    }
}
