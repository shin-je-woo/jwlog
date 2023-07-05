package com.jwlog.crypto;

import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;

public final class PasswordEncoder {

    private static final int CPU_COST = 16;
    private static final int MEMORY_COST = 8;
    private static final int PARALLELIZATION = 1;
    private static final int KEY_LENGTH = 32;
    private static final int SALT_LENGTH = 64;

    private PasswordEncoder() {
        throw new AssertionError("This is utility class. do not instantiate!");
    }

    private final static SCryptPasswordEncoder encoder = new SCryptPasswordEncoder(
            CPU_COST,
            MEMORY_COST,
            PARALLELIZATION,
            KEY_LENGTH,
            SALT_LENGTH);

    public static String encrypt(String rawPassword) {
        return encoder.encode(rawPassword);
    }

    public static boolean matches(String rawPassword, String encyptedPassword) {
        return encoder.matches(rawPassword, encyptedPassword);
    }
}
