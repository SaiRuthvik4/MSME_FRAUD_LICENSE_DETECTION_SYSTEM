package com.msme.fraud.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.msme.fraud.model.Applicant;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApplicantRepository extends JpaRepository<Applicant, Long> {

    // 🔍 Find by PAN
    List<Applicant> findByPanNumber(String panNumber);

    // 🔍 Find by Aadhaar
    List<Applicant> findByAadhaarNumber(String aadhaarNumber);

    // 🔍 Find by Mobile
    List<Applicant> findByMobile(String mobile);

    // 🔍 Find by GST
    Optional<Applicant> findByGstNumber(String gstNumber);

    // 🔍 Find by Document Hash (duplicate detection)
    Optional<Applicant> findByDocumentHash(String documentHash);

    // 🔥 NEW: Count applications by PAN (faster than list size)
    int countByPanNumber(String panNumber);

    // 🔥 NEW: Count applications by Aadhaar
    int countByAadhaarNumber(String aadhaarNumber);

    // 🔥 NEW: Count applications by Mobile
    int countByMobile(String mobile);
}