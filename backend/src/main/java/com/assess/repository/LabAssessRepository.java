package com.assess.repository;

import com.assess.entity.LabAssess;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LabAssessRepository extends JpaRepository<LabAssess, Long> {

    List<LabAssess> findAllByOrderByIdDesc();
}
