package com.msme.fraud.service;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class OCRService {

    // 🔥 MAIN OCR METHOD
    public String extractText(String filePath) {

        try {
            ITesseract tesseract = new Tesseract();

            // ✅ Correct tessdata path
            tesseract.setDatapath("C:\\Program Files\\Tesseract-OCR\\tessdata");

            File file = new File(filePath);

            String rawText = tesseract.doOCR(file);

            // 🔥 Clean text (important)
            return rawText.replaceAll("[^A-Za-z0-9 ]", " ").toUpperCase();

        } catch (Exception e) {
            throw new RuntimeException("OCR extraction failed: " + e.getMessage());
        }
    }

    // 🔥 PAN EXTRACTION (STRICT)
    public String extractPAN(String text) {

        Pattern pattern = Pattern.compile("[A-Z]{5}[0-9]{4}[A-Z]");
        Matcher matcher = pattern.matcher(text);

        if (matcher.find()) {
            return matcher.group();
        }

        return null; // ❗ important (means not found)
    }

    // 🔥 AADHAAR EXTRACTION (STRICT)
    public String extractAadhaar(String text) {

        Pattern pattern = Pattern.compile("\\b\\d{12}\\b");
        Matcher matcher = pattern.matcher(text);

        if (matcher.find()) {
            return matcher.group();
        }

        return null; // ❗ important
    }

    // 🔥 DOCUMENT TYPE CHECK (NEW FEATURE 🚀)

    public boolean isAadhaarDocument(String text) {
        return text.contains("GOVERNMENT OF INDIA") || text.contains("AADHAAR");
    }

    public boolean isPanDocument(String text) {
        return text.contains("INCOME TAX DEPARTMENT") || text.contains("PERMANENT ACCOUNT NUMBER");
    }
}