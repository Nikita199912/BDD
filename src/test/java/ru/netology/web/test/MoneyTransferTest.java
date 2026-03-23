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
        // 1. Берем начальные балансы
        var firstCardBalance = dashboardPage.getCardBalance(0);
        var secondCardBalance = dashboardPage.getCardBalance(1);

        // 2. Генерируем сумму БОЛЬШЕ баланса
        var amount = DataHelper.generateInvalidAmount(secondCardBalance);

        // 3. Делаем перевод
        var transferPage = dashboardPage.selectCardToTransfer(0);
        transferPage.makeTransfer(String.valueOf(amount), DataHelper.getSecondCardInfo().getCardNumber());

        // 4. ПРОВЕРЯЕМ НЕИЗМЕННОСТЬ БАЛАНСОВ (Пункт 4 правок Андрея)
        // Если приложение НЕ БАЖНОЕ, то балансы останутся прежними, и этот тест будет ЗЕЛЕНЫМ.
        // Если приложение БАЖНОЕ (как сейчас), то балансы ИЗМЕНЯТСЯ, и этот тест УПАДЕТ с AssertionFailedError.
        // Это и будет "правильное" падение теста.
        assertEquals(firstCardBalance, dashboardPage.getCardBalance(0));
        assertEquals(secondCardBalance, dashboardPage.getCardBalance(1));
    }
}