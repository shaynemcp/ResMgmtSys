package com.umgc.swen646;

import com.eatthepath.otp.TimeBasedOneTimePasswordGenerator;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.SecretKey;
import java.time.Instant;
import org.apache.commons.codec.binary.Base32;
import java.security.SecureRandom;

/**
 * Utility class for MFA (TOTP) operations.
 */
public class MFAUtil {
    private static final String BASE32_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ234567";
    private static final int SECRET_LENGTH = 16; // 16 chars is standard for TOTP

    /**
     * Generates a random Base32 secret for TOTP (Google/Microsoft Authenticator).
     * @return Base32-encoded secret
     */
    public static String generateSecret() {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(SECRET_LENGTH);
        for (int i = 0; i < SECRET_LENGTH; i++) {
            int idx = random.nextInt(BASE32_CHARS.length());
            sb.append(BASE32_CHARS.charAt(idx));
        }
        return sb.toString();
    }

    /**
     * Verifies a TOTP code for a given secret using java-otp (works with Google/Microsoft Authenticator).
     * @param secret Base32-encoded secret
     * @param code 6-digit code from user
     * @return true if valid, false otherwise
     */
    public static boolean verifyCode(String secret, String code) {
        try {
            Base32 base32 = new Base32();
            byte[] keyBytes = base32.decode(secret);
            SecretKey key = new SecretKeySpec(keyBytes, "RAW");

            TimeBasedOneTimePasswordGenerator totp = new TimeBasedOneTimePasswordGenerator();
            int validCode = totp.generateOneTimePassword(key, Instant.now());
            return String.format("%06d", validCode).equals(code);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
} 