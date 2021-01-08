package com.bank.application.other;

public class BankUtils {
    private static final int MIN = 0;
    private static final int MAX = 9;
    private static final int NUMBER_OF_DIGITS_ACCOUNT = 26;
    private static final int NUMBER_OF_DIGITS_CARD = 16;


    private BankUtils() {}

    public static String generateRandomAccountNumber() {
        return generateRandomStringOfNumbers(NUMBER_OF_DIGITS_ACCOUNT);
    }

    public static String generateRandomCardNumber() {
        return generateRandomStringOfNumbers(NUMBER_OF_DIGITS_CARD);
    }

    private static String generateRandomStringOfNumbers(int rightBound) {
        StringBuilder accountNumber = new StringBuilder();

        for (int i = 0; i < rightBound; i++) {
            int randomNum = (int) (Math.random() * (MAX - MIN + 1) + MIN);
            accountNumber.append(randomNum);
        }

        return accountNumber.toString();
    }
}
