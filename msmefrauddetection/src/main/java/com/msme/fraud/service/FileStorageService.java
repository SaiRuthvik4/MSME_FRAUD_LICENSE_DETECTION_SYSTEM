package com.msme.fraud.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.security.MessageDigest;

@Service
public class FileStorageService {

    private final String UPLOAD_DIR = "uploads/";

    // Save file and return file path
    public String saveFile(MultipartFile file) {
        try {
            File dir = new File(UPLOAD_DIR);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            String filePath = UPLOAD_DIR + System.currentTimeMillis() + "_" + file.getOriginalFilename();

            FileOutputStream fos = new FileOutputStream(filePath);
            fos.write(file.getBytes());
            fos.close();

            return filePath;

        } catch (Exception e) {
            throw new RuntimeException("File upload failed");
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