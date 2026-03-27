// FILE: app/src/main/java/com/example/mobile_2703/utils/ValidationUtils.java
package com.example.mobile_2703.utils;

import android.util.Patterns;

/**
 * ValidationUtils - Tiện ích kiểm tra hợp lệ đầu vào.
 */
public final class ValidationUtils {

    private ValidationUtils() {}

    /**
     * Kiểm tra username hợp lệ.
     * Điều kiện: không null, không rỗng, >= 3 ký tự.
     */
    public static boolean isUsernameValid(String username) {
        return username != null && !username.trim().isEmpty() && username.trim().length() >= 3;
    }

    /**
     * Kiểm tra password hợp lệ.
     * Điều kiện: không null, không rỗng, >= 6 ký tự.
     */
    public static boolean isPasswordValid(String password) {
        return password != null && !password.trim().isEmpty() && password.trim().length() >= 6;
    }

    /**
     * Kiểm tra email hợp lệ dùng android.util.Patterns.EMAIL_ADDRESS.
     */
    public static boolean isEmailValid(String email) {
        return email != null
                && !email.trim().isEmpty()
                && Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches();
    }
}
