package ru.netology.pages;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class CreditFormPage {
    private SelenideElement heading = $$("h3").findBy(text("Кредит по данным карты"));

    public CreditFormPage() {
        heading.shouldBe(visible);
    }
}
