package com.assess.dto;

import com.assess.entity.LabAssess;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public class LabAssessResponse {

    private Long id;
    private String name;
    private Integer assessmentDirection;
    private Integer frontendResult;
    private Integer backendResult;
    private Integer dataManagementResult;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime assessmentTime;

    private String assessor;

    public static LabAssessResponse from(LabAssess entity) {
        LabAssessResponse response = new LabAssessResponse();
        response.setId(entity.getId());
        response.setName(entity.getName());
        response.setAssessmentDirection(entity.getAssessmentDirection());
        response.setFrontendResult(entity.getFrontendResult());
        response.setBackendResult(entity.getBackendResult());
        response.setDataManagementResult(entity.getDataManagementResult());
        response.setAssessmentTime(entity.getAssessmentTime());
        response.setAssessor(entity.getAssessor());
        return response;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAssessmentDirection() {
        return assessmentDirection;
    }

    public void setAssessmentDirection(Integer assessmentDirection) {
        this.assessmentDirection = assessmentDirection;
    }

    public Integer getFrontendResult() {
        return frontendResult;
    }

    public void setFrontendResult(Integer frontendResult) {
        this.frontendResult = frontendResult;
    }

    public Integer getBackendResult() {
        return backendResult;
    }

    public void setBackendResult(Integer backendResult) {
        this.backendResult = backendResult;
    }

    public Integer getDataManagementResult() {
        return dataManagementResult;
    }

    public void setDataManagementResult(Integer dataManagementResult) {
        this.dataManagementResult = dataManagementResult;
    }

    public LocalDateTime getAssessmentTime() {
        return assessmentTime;
    }

    public void setAssessmentTime(LocalDateTime assessmentTime) {
        this.assessmentTime = assessmentTime;
    }

    public String getAssessor() {
        return assessor;
    }

    public void setAssessor(String assessor) {
        this.assessor = assessor;
    }
}
