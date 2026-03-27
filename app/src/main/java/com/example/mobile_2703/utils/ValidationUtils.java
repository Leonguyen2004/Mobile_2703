package com.example.mobile_2703.utils;

import android.text.TextUtils;
import android.util.Patterns;

/**
 * ValidationUtils - Các hàm validate dữ liệu nhập.
 *
 * USAGE:
 *   if (!ValidationUtils.isValidEmail(email)) { ... }
 */
public final class ValidationUtils {

    private ValidationUtils() {}

    public static boolean isEmpty(String value) {
        return TextUtils.isEmpty(value) || value.trim().isEmpty();
    }

    public static boolean isValidEmail(String email) {
        return !isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean isValidPassword(String password) {
        return !isEmpty(password) && password.length() >= 6;
    }

    public static boolean isValidUsername(String username) {
        return !isEmpty(username) && username.length() >= 3 && username.matches("[a-zA-Z0-9_]+");
    }

    public static boolean isPositiveNumber(String value) {
        if (isEmpty(value)) return false;
        try {
            return Double.parseDouble(value) > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isNonNegativeInt(String value) {
        if (isEmpty(value)) return false;
        try {
            return Integer.parseInt(value) >= 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
