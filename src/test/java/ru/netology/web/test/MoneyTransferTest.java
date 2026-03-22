package ru.netology.web.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.web.data.DataHelper;
import ru.netology.web.page.LoginPage;
import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MoneyTransferTest {

    @BeforeEach
    void setup() {
        var loginPage = open("http://localhost:9999", LoginPage.class);
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCode();
        verificationPage.validVerify(verificationCode);
    }

    @Test
    void shouldTransferFromSecondToFirst() {
        var dashboardPage = new ru.netology.web.page.DashboardPage();
        var firstCardBalance = dashboardPage.getCardBalance(0);
        var secondCardBalance = dashboardPage.getCardBalance(1);
        int amount = 500;
        var expectedFirst = firstCardBalance + amount;
        var expectedSecond = secondCardBalance - amount;
        var transferPage = dashboardPage.selectCardToTransfer(0); // пополняем первую
        dashboardPage = transferPage.makeTransfer(String.valueOf(amount), DataHelper.getSecondCardInfo().getCardNumber());
        assertEquals(expectedFirst, dashboardPage.getCardBalance(0));
        assertEquals(expectedSecond, dashboardPage.getCardBalance(1));
    }
    @Test
    void shouldNotTransferMoreThanBalance() {
        var dashboardPage = new ru.netology.web.page.DashboardPage();
        var secondCardBalance = dashboardPage.getCardBalance(1);
        int amount = secondCardBalance + 1000;
        var transferPage = dashboardPage.selectCardToTransfer(0);
        transferPage.makeTransfer(String.valueOf(amount), ru.netology.web.data.DataHelper.getSecondCardInfo().getCardNumber());
        var actualBalanceSecondCard = dashboardPage.getCardBalance(1);
        org.junit.jupiter.api.Assertions.assertTrue(actualBalanceSecondCard >= 0, "Баланс не может быть отрицательным!");
    }

}