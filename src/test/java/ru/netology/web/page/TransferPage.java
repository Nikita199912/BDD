package ru.netology.web.page;

import static com.codeborne.selenide.Selenide.$;

public class TransferPage {
    public DashboardPage makeTransfer(String amount, String cardNumber) {
        $("[data-test-id=amount] input").setValue(amount);
        $("[data-test-id=from] input").setValue(cardNumber);
        $("[data-test-id=action-transfer]").click();
        return new DashboardPage();
    }
}