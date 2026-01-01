package org.example.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class TransactionNumberGenerator {

    private static final Random random = new Random();
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd");

    public static String generate() {
        int randomDigits = 1000 + random.nextInt(9000); // 4 digits
        return "TXN-" + LocalDate.now().format(DATE_FORMAT) + "-" + randomDigits;
    }
}
