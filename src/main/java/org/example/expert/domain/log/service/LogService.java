package org.example.expert.domain.log.service;

import lombok.RequiredArgsConstructor;
import org.example.expert.domain.log.entity.Log;
import org.example.expert.domain.log.repository.LogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LogService {

    private final LogRepository logRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveLog(Long userId, Long todoId, Long managerId) {

        Log log = new Log(userId, todoId, managerId, "유저(id:" + userId + ")가 " +"할 일(id:" + todoId + ")에 매니저(id:" + managerId + ")를 등록 요청 했습니다.");

        logRepository.save(log);
    }

}
