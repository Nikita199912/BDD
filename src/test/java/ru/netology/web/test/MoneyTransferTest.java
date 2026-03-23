package ru.netology.web.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.web.data.DataHelper;
import ru.netology.web.page.DashboardPage;
import ru.netology.web.page.LoginPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MoneyTransferTest {
    DashboardPage dashboardPage;

    @BeforeEach
    void setup() {
        var loginPage = open("http://localhost:9999", LoginPage.class);
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCode();
        dashboardPage = verificationPage.validVerify(verificationCode);
    }

    @Test
    void shouldTransferMoneyFromSecondToFirst() {
        var firstCardBalance = dashboardPage.getCardBalance(0);
        var secondCardBalance = dashboardPage.getCardBalance(1);
        var amount = DataHelper.generateValidAmount(secondCardBalance);

        var expectedBalanceFirstCard = firstCardBalance + amount;
        var expectedBalanceSecondCard = secondCardBalance - amount;

        var transferPage = dashboardPage.selectCardToTransfer(0);
        transferPage.makeTransfer(String.valueOf(amount), DataHelper.getSecondCardInfo().getCardNumber());

        assertEquals(expectedBalanceFirstCard, dashboardPage.getCardBalance(0));
        assertEquals(expectedBalanceSecondCard, dashboardPage.getCardBalance(1));
    }

    @Test
    void shouldNotTransferIfAmountMoreThanBalance() {
        var firstCardBalance = dashboardPage.getCardBalance(0);
        var secondCardBalance = dashboardPage.getCardBalance(1);
        var amount = DataHelper.generateInvalidAmount(secondCardBalance);
        var transferPage = dashboardPage.selectCardToTransfer(0);
        transferPage.makeTransfer(String.valueOf(amount), DataHelper.getSecondCardInfo().getCardNumber());
        assertEquals(firstCardBalance, dashboardPage.getCardBalance(0));
        assertEquals(secondCardBalance, dashboardPage.getCardBalance(1));
    }
}