package ru.netology.web.page;

import com.codeborne.selenide.SelenideElement;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class TransferPage {
    private SelenideElement amountInput = $("[data-test-id='amount'] input");
    private SelenideElement fromInput = $("[data-test-id='from'] input");
    private SelenideElement transferButton = $("[data-test-id='action-transfer']");

    public void makeTransfer(String amount, String cardNumber) {
        amountInput.setValue(amount);
        fromInput.setValue(cardNumber);
        transferButton.click();
    }
}