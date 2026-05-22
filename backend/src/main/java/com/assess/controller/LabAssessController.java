package com.assess.controller;

import com.assess.dto.LabAssessRequest;
import com.assess.dto.LabAssessResponse;
import com.assess.service.LabAssessService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/assess")
public class LabAssessController {

    private final LabAssessService service;

    public LabAssessController(LabAssessService service) {
        this.service = service;
    }

    @GetMapping
    public List<LabAssessResponse> list() {
        return service.listAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LabAssessResponse create(@Valid @RequestBody LabAssessRequest request) {
        return service.create(request);
    }
}
