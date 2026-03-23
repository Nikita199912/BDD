package ru.netology.web.data;

import lombok.Value;
import java.util.Random;

public class DataHelper {
    private DataHelper() {}

    @Value
    public static class AuthInfo {
        String login;
        String password;
    }

    public static AuthInfo getAuthInfo() {
        return new AuthInfo("vasya", "qwerty123");
    }

    public static String getVerificationCode() {
        return "12345";
    }

    @Value
    public static class CardInfo {
        String cardNumber;
    }

    public static CardInfo getFirstCardInfo() {
        return new CardInfo("5559 0000 0000 0001");
    }

    public static CardInfo getSecondCardInfo() {
        return new CardInfo("5559 0000 0000 0002");
    }

    // Генерирует сумму меньше баланса
    public static int generateValidAmount(int balance) {
        if (balance <= 0) return 0;
        return new Random().nextInt(balance);
    }

    // Генерирует сумму больше баланса
    public static int generateInvalidAmount(int balance) {
        return balance + 5000;
    }
}