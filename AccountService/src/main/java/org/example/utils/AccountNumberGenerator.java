package org.example.utils;

import java.util.Random;

public class AccountNumberGenerator {
    private static final Random random = new Random();

    public static String generate(String holderName) {
        String initials = holderName
                .replaceAll("\\s+", "")
                .substring(0, Math.min(3, holderName.length()))
                .toUpperCase();

        int randomDigits = 1000 + random.nextInt(9000); // 4 digits

        return initials + randomDigits;
    }
}

