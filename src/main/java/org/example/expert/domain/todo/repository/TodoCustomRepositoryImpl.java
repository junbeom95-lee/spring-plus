package org.example.expert.domain.todo.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.example.expert.domain.todo.dto.request.TodoPageRequest;
import org.example.expert.domain.todo.dto.response.TodoPageResponse;
import org.example.expert.domain.todo.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.example.expert.domain.comment.entity.QComment.comment;
import static org.example.expert.domain.manager.entity.QManager.manager;
import static org.example.expert.domain.todo.entity.QTodo.todo;

@RequiredArgsConstructor
public class TodoCustomRepositoryImpl implements TodoCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Todo> findByIdWithUser(Long todoId) {

        return Optional.ofNullable(queryFactory.select(todo)
                .from(todo)
                .leftJoin(todo.user)
                .fetchJoin()
                .where(todo.id.eq(todoId))
                .fetchOne());
    }

    @Override
    public Page<TodoPageResponse> findAllWithCond(TodoPageRequest request) {

        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());

        LocalDateTime start = null;
        LocalDateTime end = null;
        if (request.getStartDate() != null) start = request.getStartDate().atStartOfDay();
        if (request.getEndDate() != null) end = request.getEndDate().plusDays(1).atStartOfDay();


        List<TodoPageResponse> list = queryFactory
                .select(Projections.constructor(TodoPageResponse.class,
                        todo.title,
                        manager.countDistinct(),
                        comment.countDistinct())
                )
                .from(todo)
                .leftJoin(todo.managers, manager)
                .leftJoin(todo.comments, comment)
                .where(
                        titleCont(request.getTitle()),
                        dateEq(start, end),
                        nicknameCont(request.getNickname())
                )
                .groupBy(todo.id)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long listCount = queryFactory
                .select(todo.count())
                .from(todo)
                .leftJoin(todo.managers, manager)
                .leftJoin(todo.comments, comment)
                .where(
                        titleCont(request.getTitle()),
                        dateEq(start, end),
                        nicknameCont(request.getNickname())
                )
                .fetchOne();

        long total = listCount != null ? listCount : 0L;

        return new PageImpl<>(list, pageable, total);
    }

    //제목 부분 일치 확인
    private BooleanExpression titleCont(String title) {
        return title == null ? null : todo.title.contains(title);
    }

    //생성일 범위 확인
    private BooleanExpression dateEq(LocalDateTime startDate, LocalDateTime endDate) {

        if(startDate != null && endDate != null) return todo.createdAt.between(startDate, endDate);
        else if (startDate != null) return todo.createdAt.goe(startDate);
        else if (endDate != null) return todo.createdAt.loe(endDate);
        else return null;
    }

    //담당자 닉네임 부분 확인
    private BooleanExpression nicknameCont(String nickname) {
        return nickname == null ? null : manager.user.nickname.contains(nickname);
    }
}
