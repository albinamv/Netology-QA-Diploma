package ru.netology.pages;

import com.codeborne.selenide.SelenideElement;
import lombok.Getter;
import org.openqa.selenium.Keys;
import ru.netology.helpers.DataHelper;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@Getter
public class PaymentFormPage {
    private SelenideElement heading = $$("h3").findBy(text("Оплата по карте"));

    private SelenideElement card = $(byText("Номер карты")).parent();
    private SelenideElement month = $(byText("Месяц")).parent();
    private SelenideElement year = $(byText("Год")).parent();
    private SelenideElement cardOwner = $(byText("Владелец")).parent();
    private SelenideElement cvc = $(byText("CVC/CVV")).parent();
    private SelenideElement continueButton = $(byText("Продолжить"));

    private String inputClass = ".input__control";
    private String indicationClass = ".input__sub";

    private SelenideElement notificationSuccess = $(".notification_status_ok");
    private SelenideElement notificationError = $(".notification_status_error");

    public PaymentFormPage() {
        heading.shouldBe(visible);
    }

    public void fillTheForm(DataHelper.CardData data) {
        fillField(card, data.getCardNumber());
        fillField(month, data.getMonth());
        fillField(year, data.getYear());
        fillField(cardOwner, data.getCardOwner());
        fillField(cvc, data.getCvc());
    }

    public void fillField(SelenideElement element, String value) {
        element.$(inputClass).setValue(value);
    }

    public String getFieldValue(SelenideElement element) {
        return element.$(inputClass).getValue();
    }

    public void clearField(SelenideElement element) {
        element.$(inputClass).sendKeys(Keys.CONTROL + "A");
        element.$(inputClass).sendKeys(Keys.BACK_SPACE);
    }

    public void checkSuccessNotification() {
        notificationSuccess.shouldBe(visible);
        notificationSuccess.$(".notification__content").shouldHave(exactText("Операция одобрена Банком."));
    }

    public void checkErrorNotification() {
        notificationError.shouldBe(visible);
        notificationError.$(".notification__content").shouldHave(exactText("Ошибка! Банк отказал в проведении операции."));
    }

    public void checkCardErrorIndication() {
        card.$(indicationClass).shouldHave(exactText("Неверный формат"));
    }

    public void checkMonthErrorEmpty() {
        month.$(indicationClass).shouldHave(exactText("Неверный формат"));
    }

    public void checkYearErrorEmpty() {
        year.$(indicationClass).shouldHave(exactText("Неверный формат"));
    }

    public void checkCardOwnerErrorEmpty() {
        cardOwner.$(indicationClass).shouldHave(exactText("Поле обязательно для заполнения"));
    }

    public void checkCVCErrorIndication() {
        cvc.$(indicationClass).shouldHave(exactText("Неверный формат"));
    }

    public void clearTheForm() {
        card.$(inputClass).sendKeys(Keys.CONTROL + "A");
        card.$(inputClass).sendKeys(Keys.BACK_SPACE);

        month.$(inputClass).sendKeys(Keys.CONTROL + "A");
        month.$(inputClass).sendKeys(Keys.BACK_SPACE);

        year.$(inputClass).sendKeys(Keys.CONTROL + "A");
        year.$(inputClass).sendKeys(Keys.BACK_SPACE);

        cardOwner.$(inputClass).sendKeys(Keys.CONTROL + "A");
        cardOwner.$(inputClass).sendKeys(Keys.BACK_SPACE);

        cvc.$(inputClass).sendKeys(Keys.CONTROL + "A");
        cvc.$(inputClass).sendKeys(Keys.BACK_SPACE);
    }
}
