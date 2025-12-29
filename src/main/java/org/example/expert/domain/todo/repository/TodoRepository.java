package org.example.expert.domain.todo.repository;

import org.example.expert.domain.todo.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface TodoRepository extends JpaRepository<Todo, Long>, TodoCustomRepository {

    //조건 없을 때
    @Query("SELECT t FROM Todo t LEFT JOIN FETCH t.user u ORDER BY t.modifiedAt DESC")
    Page<Todo> findAllByOrderByModifiedAtDesc(Pageable pageable);

    //날씨, 시작, 끝
    @Query("select t from Todo t left join fetch t.user u where t.weather like %:weather% and t.modifiedAt between :start and :end order by t.modifiedAt desc")
    Page<Todo> findAllByWithWeatherStartEndDesc(Pageable pageable, @Param("weather") String weather, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    //날씨, 시작
    @Query("select t from Todo t left join fetch t.user u where t.weather like %:weather% and t.modifiedAt >= :start order by t.modifiedAt desc")
    Page<Todo> findAllByWithWeatherStartDesc(Pageable pageable, @Param("weather") String weather, @Param("start") LocalDateTime start);

    //날씨, 끝
    @Query("select t from Todo t left join fetch t.user u where t.weather like %:weather% and t.modifiedAt <= :end order by t.modifiedAt desc")
    Page<Todo> findAllByWithWeatherEndDesc(Pageable pageable, @Param("weather") String weather, @Param("end") LocalDateTime end);

    //날씨
    @Query("select t from Todo  t left join fetch t.user u where t.weather like %:weather% order by t.modifiedAt desc")
    Page<Todo> findAllByWithWeatherDesc(Pageable pageable, @Param("weather") String weather);

    //시작, 끝
    @Query("select t from Todo t left join fetch t.user u where t.modifiedAt between :start and :end order by t.modifiedAt desc")
    Page<Todo> findAllByWithStartEndDesc(Pageable pageable, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    //시작
    @Query("select t from Todo t left join fetch t.user u where t.modifiedAt >= :start order by t.modifiedAt desc")
    Page<Todo> findAllByWithStartDesc(Pageable pageable, @Param("start") LocalDateTime start);

    //끝
    @Query("select t from Todo t left join fetch t.user u where t.modifiedAt <= :end order by t.modifiedAt desc")
    Page<Todo> findAllByWithEndDesc(Pageable pageable, @Param("end") LocalDateTime end);

}
