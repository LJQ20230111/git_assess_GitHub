package com.assess.controller;

import com.assess.service.DatabaseHealthService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class HealthController {

    private final DatabaseHealthService databaseHealthService;

    public HealthController(DatabaseHealthService databaseHealthService) {
        this.databaseHealthService = databaseHealthService;
    }

    @GetMapping("/health")
    public Map<String, Object> health() {
        boolean dbOk = databaseHealthService.isConnected();
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("status", dbOk ? "ok" : "degraded");
        body.put("message", dbOk ? "后端服务运行正常" : "后端运行中，但数据库未连通");
        body.put("service", "assess-backend");
        body.put("database", dbOk ? "connected" : "disconnected");
        body.put("databaseMessage", databaseHealthService.statusMessage());
        return body;
    }
}
