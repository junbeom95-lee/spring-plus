package org.example.expert.domain.user.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.expert.domain.user.entity.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class UserBatchRepository {

    private final JdbcTemplate jdbcTemplate;

    @Transactional
    public void batchInsert(List<User> userList) {

        LocalDateTime now = LocalDateTime.now();

        log.info("[UserBatchRepository] batchInsert start");

        String sql = "insert into users (email, nickname, password, user_role, created_at, modified_at) values (?, ?, ?, ?, ?, ?)";

        jdbcTemplate.batchUpdate(sql, userList, 1000,
                (ps, u) -> {
                    ps.setString(1, u.getEmail());
                    ps.setString(2, u.getNickname());
                    ps.setString(3, u.getPassword());
                    ps.setString(4, u.getUserRole().name());
                    ps.setObject(5, now);
                    ps.setObject(6, now);
                });

        log.info("[UserBatchRepository] batchInsert fin");
    }
}
