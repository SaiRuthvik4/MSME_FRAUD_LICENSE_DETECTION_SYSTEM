package com.msme.fraud.util;

import java.util.regex.Pattern;

public class ValidationUtil {

    // PAN: ABCDE1234F
    public static boolean isValidPAN(String pan) {
        if (pan == null) return false;
        return Pattern.matches("[A-Z]{5}[0-9]{4}[A-Z]{1}", pan);
    }

    // Aadhaar: 12 digits
    public static boolean isValidAadhaar(String aadhaar) {
        if (aadhaar == null) return false;
        return Pattern.matches("\\d{12}", aadhaar);
    }

    // GST: 22ABCDE1234F1Z5
    public static boolean isValidGST(String gst) {
        if (gst == null) return false;
        return Pattern.matches("\\d{2}[A-Z]{5}\\d{4}[A-Z]{1}[A-Z\\d]{1}[Z]{1}[A-Z\\d]{1}", gst);
    }

    // Mobile: 10 digits (India)
    public static boolean isValidMobile(String mobile) {
        if (mobile == null) return false;
        return Pattern.matches("[6-9]\\d{9}", mobile);
    }

    // Business Name (basic check)
    public static boolean isValidBusinessName(String name) {
        return name != null && name.trim().length() >= 3;
    }
}