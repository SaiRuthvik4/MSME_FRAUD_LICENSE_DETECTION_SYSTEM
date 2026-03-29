package com.msme.fraud.controller;

import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin
public class ChatController {

    @PostMapping
    public Map<String, String> chat(@RequestBody Map<String, String> request) {

        String msg = request.get("message").toLowerCase();
        String reply;

        if (msg.contains("pan")) {
            reply = "PAN format should be like ABCDE1234F.";
        } 
        else if (msg.contains("aadhaar")) {
            reply = "Aadhaar must be 12 digits.";
        } 
        else if (msg.contains("gst")) {
            reply = "GST format example: 22ABCDE1234F1Z5.";
        } 
        else if (msg.contains("status")) {
            reply = "You can check your application status in dashboard.";
        } 
        else if (msg.contains("rejected") || msg.contains("fraud")) {
            reply = "Applications are flagged based on risk score. Admin will verify before final decision.";
        } 
        else if (msg.contains("upload")) {
            reply = "Please upload clear document images (Aadhaar/PAN) for better verification.";
        }
        else {
            reply = "Ask about PAN, Aadhaar, GST, upload, or application status.";
        }

        Map<String, String> response = new HashMap<>();
        response.put("reply", reply);

        return response;
    }
}