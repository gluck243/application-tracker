package com.gluck.jobtracker.repository;

import com.gluck.jobtracker.model.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ApplicationRepository extends JpaRepository<JobApplicationEntity, Long> {

    @Query("SELECT ja FROM JobApplicationEntity ja WHERE LOWER(ja.position) LIKE LOWER(CONCAT('%', :typedName, '%'))")
    Page<JobApplicationEntity> searchByPosition(String typedName, Pageable pageable);

    @Query("SELECT ja FROM JobApplicationEntity ja WHERE LOWER(ja.companyName) LIKE LOWER(CONCAT('%', :typedName, '%'))")
    Page<JobApplicationEntity> searchByCompany(String typedName, Pageable pageable);

    @Query("SELECT ja FROM JobApplicationEntity ja WHERE LOWER(CAST(ja.description AS string)) LIKE LOWER(CONCAT('%', :typedName, '%'))")
    Page<JobApplicationEntity> searchByDescription(String typedName, Pageable pageable);

    Long countByPositionContainsIgnoreCase(String position);

    Long countByCompanyNameContainsIgnoreCase(String companyName);

    @Query("SELECT COUNT(ja) FROM JobApplicationEntity ja WHERE UPPER(CAST(ja.description AS string)) LIKE UPPER(CONCAT('%', :desc, '%'))")
    Long countByDescriptionContainsIgnoreCase(String desc);

    Long countByStatusIs(Status status);

}

