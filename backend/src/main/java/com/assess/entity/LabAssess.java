package com.assess.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "lab_assess")
public class LabAssess {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(name = "assessment_direction", nullable = false)
    private Integer assessmentDirection;

    @Column(name = "frontend_result")
    private Integer frontendResult = 0;

    @Column(name = "backend_result")
    private Integer backendResult = 0;

    @Column(name = "data_management_result")
    private Integer dataManagementResult = 0;

    @Column(name = "assessment_time")
    private LocalDateTime assessmentTime;

    @Column(length = 50)
    private String assessor;

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
