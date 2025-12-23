package org.example.expert.domain.common.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Slf4j
@RestController
public class HealthController {

    @GetMapping("/health")
    public ResponseEntity<String> check(HttpServletRequest request) {

        StringBuilder sb = new StringBuilder();

        try {

            InetAddress localHost = InetAddress.getLocalHost();

            String ipAddress = localHost.getHostAddress();

            String ipAddressRemote = request.getRemoteAddr();

            String hostname = localHost.getHostName();

            sb.append("[Health Check] ip : ").append(ipAddress)
                    .append(", remote : ").append(ipAddressRemote)
                    .append(", hostname : ").append(hostname);

            log.info("[Health Check] ip : {}, remote : {}, hostname : {} check ", ipAddress, ipAddressRemote, hostname);

        } catch (UnknownHostException e) {

            log.info("Health Check Exception : {}", e.getLocalizedMessage());

            throw new RuntimeException(e);
        }

        return ResponseEntity.ok(sb.toString());
    }
}
