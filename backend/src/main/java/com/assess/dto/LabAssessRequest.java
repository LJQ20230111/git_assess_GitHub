package com.assess.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public class LabAssessRequest {

    @NotBlank(message = "姓名不能为空")
    @Size(max = 50, message = "姓名长度不能超过50")
    private String name;

    @NotNull(message = "考核方向不能为空")
    @Min(0)
    @Max(2)
    private Integer assessmentDirection;

    @Min(0)
    @Max(3)
    private Integer frontendResult = 0;

    @Min(0)
    @Max(3)
    private Integer backendResult = 0;

    @Min(0)
    @Max(3)
    private Integer dataManagementResult = 0;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime assessmentTime;

    @Size(max = 50, message = "考核人长度不能超过50")
    private String assessor;

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
