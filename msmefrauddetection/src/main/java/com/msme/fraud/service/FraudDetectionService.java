package com.msme.fraud.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.msme.fraud.model.Applicant;
import com.msme.fraud.repository.ApplicantRepository;

import java.util.List;

@Service
public class FraudDetectionService {

    @Autowired
    private ApplicantRepository repo;

    public void evaluateApplicant(Applicant applicant) {

        int score = applicant.getFraudScore(); // 🔥 keep previous score (from OCR)
        StringBuilder reason = new StringBuilder(
                applicant.getFraudReason() == null ? "" : applicant.getFraudReason());

        // 🔍 Fetch existing records
        List<Applicant> panList = repo.findByPanNumber(applicant.getPanNumber());
        List<Applicant> aadhaarList = repo.findByAadhaarNumber(applicant.getAadhaarNumber());
        List<Applicant> mobileList = repo.findByMobile(applicant.getMobile());

        // ==============================
        // ✅ PAN LOGIC (SMART)
        // ==============================
        if (!panList.isEmpty()) {

            for (Applicant old : panList) {

                // Same PAN + Same GST → HIGH RISK
                if (old.getGstNumber() != null &&
                        old.getGstNumber().equals(applicant.getGstNumber())) {

                    score += 40;
                    reason.append("Same PAN & GST reused; ");
                }

                // Same PAN + Different GST → NORMAL BUSINESS CASE
                else {
                    score += 5;
                    reason.append("PAN used for multiple businesses; ");
                }
            }
        }

        // ==============================
        // ✅ AADHAAR LOGIC
        // ==============================
        if (!aadhaarList.isEmpty()) {

            // Aadhaar reused too many times → suspicious
            if (aadhaarList.size() >= 3) {
                score += 25;
                reason.append("Aadhaar used multiple times; ");
            } else {
                score += 10;
                reason.append("Aadhaar reused; ");
            }
        }

        // ==============================
        // ✅ MOBILE LOGIC
        // ==============================
        if (!mobileList.isEmpty()) {
            score += 5;
            reason.append("Mobile reused; ");
        }

        // ==============================
        // ✅ APPLICATION COUNT
        // ==============================
        int totalApplications = panList.size();
        applicant.setApplicationCount(totalApplications + 1);

        if (totalApplications >= 3) {
            score += 20;
            reason.append("Too many applications from same PAN; ");
        }

        // ==============================
        // ✅ SECOND LICENSE LOGIC (🔥 IMPORTANT)
        // ==============================
        if (totalApplications >= 1 && score < 20) {
            applicant.setStatus("SECOND_LICENSE_APPROVED");
            reason.append("Valid second license request; ");
        }

        // ==============================
        // ✅ TRUSTED USER BENEFIT
        // ==============================
        if (applicant.isTrustedUser()) {
            score -= 20;
            reason.append("Trusted user; ");
        }

        // ==============================
        // ✅ FINAL SAFETY CHECK
        // ==============================
        if (score < 0) {
            score = 0;
        }

        // ==============================
        // ✅ SET FINAL VALUES
        // ==============================
        applicant.setFraudScore(score);

        if (reason.length() == 0) {
            reason.append("No issues detected");
        }

        applicant.setFraudReason(reason.toString());

        // If not already second license, decide normally
        if (applicant.getStatus() == null ||
                !applicant.getStatus().equals("SECOND_LICENSE_APPROVED")) {

            applicant.setStatus(decideStatus(score));
        }
    }

    // 🎯 STATUS DECISION
    private String decideStatus(int score) {

        if (score >= 60)
            return "HIGH_RISK";

        else if (score >= 30)
            return "MEDIUM_RISK";

        else
            return "LOW_RISK";
    }
}