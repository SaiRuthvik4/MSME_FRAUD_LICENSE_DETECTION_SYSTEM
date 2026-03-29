package com.msme.fraud.controller;

import com.msme.fraud.model.Applicant;
import com.msme.fraud.repository.ApplicantRepository;
import com.msme.fraud.service.FileStorageService;
import com.msme.fraud.service.FraudDetectionService;
import com.msme.fraud.service.OCRService;
import com.msme.fraud.util.ValidationUtil;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/applicants")
@CrossOrigin
public class ApplicantController {

    @Autowired
    private ApplicantRepository repo;

    @Autowired
    private FileStorageService fileService;

    @Autowired
    private FraudDetectionService fraudService;

    @Autowired
    private OCRService ocrService;

    // 🚀 MAIN API (UPDATED)
    @PostMapping("/register")
    public Applicant registerApplicant(
            @RequestParam String name,
            @RequestParam String mobile,
            @RequestParam String businessName,
            @RequestParam String panNumber,
            @RequestParam String aadhaarNumber,
            @RequestParam String gstNumber,
            @RequestParam MultipartFile aadhaarFile,
            @RequestParam MultipartFile panFile,
            HttpServletRequest request) {

        // ✅ 1. Basic validation
        if (!ValidationUtil.isValidPAN(panNumber)) {
            throw new RuntimeException("Invalid PAN format");
        }

        if (!ValidationUtil.isValidAadhaar(aadhaarNumber)) {
            throw new RuntimeException("Invalid Aadhaar");
        }

        if (!ValidationUtil.isValidMobile(mobile)) {
            throw new RuntimeException("Invalid mobile number");
        }

        // ✅ 2. File type validation
        if (aadhaarFile.isEmpty() || panFile.isEmpty()) {
            throw new RuntimeException("Both Aadhaar and PAN files are required");
        }

        if (!aadhaarFile.getContentType().startsWith("image") &&
            !aadhaarFile.getContentType().equals("application/pdf")) {
            throw new RuntimeException("Invalid Aadhaar file type");
        }

        if (!panFile.getContentType().startsWith("image") &&
            !panFile.getContentType().equals("application/pdf")) {
            throw new RuntimeException("Invalid PAN file type");
        }

        // ✅ 3. Save files
        String aadhaarPath = fileService.saveFile(aadhaarFile);
        String panPath = fileService.saveFile(panFile);

        // ✅ 4. Generate hashes
        String aadhaarHash = fileService.generateHash(aadhaarFile);
        String panHash = fileService.generateHash(panFile);

        // ✅ 5. OCR extraction (separate files 🔥)
        String aadhaarText = ocrService.extractText(aadhaarPath);
        String panText = ocrService.extractText(panPath);

        String extractedAadhaar = ocrService.extractAadhaar(aadhaarText);
        String extractedPAN = ocrService.extractPAN(panText);

        // ✅ 6. Create Applicant
        Applicant applicant = new Applicant();

        applicant.setName(name);
        applicant.setMobile(mobile);
        applicant.setBusinessName(businessName);
        applicant.setPanNumber(panNumber);
        applicant.setAadhaarNumber(aadhaarNumber);
        applicant.setGstNumber(gstNumber);

        // Save only Aadhaar path (you can extend later)
        applicant.setDocumentPath(aadhaarPath);
        applicant.setDocumentHash(aadhaarHash);

        applicant.setCreatedAt(LocalDateTime.now());

        // ✅ 7. Track IP
        applicant.setIpAddress(request.getRemoteAddr());

        // ✅ 8. Duplicate detection
        Optional<Applicant> existing = repo.findByDocumentHash(aadhaarHash);
        if (existing.isPresent()) {
            applicant.setFraudScore(40);
            applicant.setFraudReason("Duplicate Aadhaar document detected; ");
        }

        // ✅ 9. Document validation (CORE FIX 🔥)

        // Aadhaar check
        if (extractedAadhaar == null || !aadhaarNumber.equals(extractedAadhaar)) {
            applicant.setFraudScore(applicant.getFraudScore() + 40);
            applicant.setFraudReason(
                    (applicant.getFraudReason() == null ? "" : applicant.getFraudReason())
                            + "Aadhaar mismatch or invalid document; ");
        }

        // PAN check
        if (extractedPAN == null || !panNumber.equalsIgnoreCase(extractedPAN)) {
            applicant.setFraudScore(applicant.getFraudScore() + 40);
            applicant.setFraudReason(
                    (applicant.getFraudReason() == null ? "" : applicant.getFraudReason())
                            + "PAN mismatch or invalid document; ");
        }

        // ✅ 10. Fraud Engine
        fraudService.evaluateApplicant(applicant);

        // ✅ 11. Save
        return repo.save(applicant);
    }

    // 📌 GET ALL
    @GetMapping
    public List<Applicant> getAllApplicants() {
        return repo.findAll();
    }
}