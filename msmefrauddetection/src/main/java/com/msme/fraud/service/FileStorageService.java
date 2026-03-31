package com.msme.fraud.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.security.MessageDigest;

@Service
public class FileStorageService {

    private final String UPLOAD_DIR = System.getenv("UPLOAD_DIR");

    private String getUploadDirectory() {
        if (UPLOAD_DIR != null && !UPLOAD_DIR.isBlank()) {
            return UPLOAD_DIR;
        }
        return System.getProperty("java.io.tmpdir") + File.separator + "uploads" + File.separator;
    }

    // Save file and return file path
    public String saveFile(MultipartFile file) {
        try {
            String uploadDir = getUploadDirectory();
            File dir = new File(uploadDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            String filePath = uploadDir + System.currentTimeMillis() + "_" + file.getOriginalFilename();

            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                fos.write(file.getBytes());
            }

            return filePath;

        } catch (Exception e) {
            throw new RuntimeException("File upload failed: " + e.getMessage());
        }
    }

    // Generate file hash (to detect duplicate uploads)
    public String generateHash(MultipartFile file) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytes = md.digest(file.getBytes());

            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(String.format("%02x", b));
            }

            return sb.toString();

        } catch (Exception e) {
            throw new RuntimeException("Hash generation failed");
        }
    }
}