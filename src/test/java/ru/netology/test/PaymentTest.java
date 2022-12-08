package ru.netology.test;

import org.junit.jupiter.api.*;
import ru.netology.helpers.DataHelper;
import ru.netology.helpers.SQLHelper;
import ru.netology.pages.DashboardPage;
import ru.netology.pages.PaymentFormPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PaymentTest extends TestBase {
    static DashboardPage dashboardPage;
    static PaymentFormPage paymentForm;

    final String paymentsTable = "payment_entity";
    String expectedPrice = "4500000";

    @BeforeEach
    void openHost() {
        open(testHost);

        dashboardPage = new DashboardPage();
        paymentForm = dashboardPage.openPaymentForm();
    }

    @Nested
    class IncreasedTimeout {

        @Test
        @DisplayName("1. Payment approved (Happy path)")
        void shouldApprovePaymentWithValidData() {
            long rowsOrdersBefore = SQLHelper.getRowsAmountFrom(ordersTable);
            long rowsPaymentsBefore = SQLHelper.getRowsAmountFrom(paymentsTable);

            paymentForm.fillForm(DataHelper.generateValidCardData(expiryYears));
            paymentForm.sendForm();

            paymentForm.checkSuccessNotification(10); // проверка уведомления в UI

            // проверки на изменение кол-ва записей в БД (что точно добавилась новая запись)
            assertEquals(rowsOrdersBefore + 1, SQLHelper.getRowsAmountFrom(ordersTable));
            assertEquals(rowsPaymentsBefore + 1, SQLHelper.getRowsAmountFrom(paymentsTable));
            SQLHelper.Payment lastEntry = SQLHelper.getLastEntryFromPaymentsTable();
            // проверка суммы оплаты
            assertEquals(expectedPrice, lastEntry.getAmount());
            // проверка статуса последней записи
            assertEquals(approved, lastEntry.getStatus());
        }

        @Test
        @DisplayName("2. Max expiry year")
        void shouldSendFormWithMaxExpiryYear() {
            long rowsOrdersBefore = SQLHelper.getRowsAmountFrom(ordersTable);
            long rowsPaymentsBefore = SQLHelper.getRowsAmountFrom(paymentsTable);

            paymentForm.fillForm(DataHelper.generateCardDataWithShiftedYearFromCurrent(expiryYears));
            paymentForm.sendForm();

            paymentForm.checkSuccessNotification(10);
            assertEquals(rowsOrdersBefore + 1, SQLHelper.getRowsAmountFrom(ordersTable));
            assertEquals(rowsPaymentsBefore + 1, SQLHelper.getRowsAmountFrom(paymentsTable));
            SQLHelper.Payment lastEntry = SQLHelper.getLastEntryFromPaymentsTable();
            assertEquals(expectedPrice, lastEntry.getAmount());
            assertEquals(approved, lastEntry.getStatus());
        }

        @Test
        @DisplayName("3. Max-1 expiry year")
        void shouldSendFormWithExpiryYearBelowMax() {
            long rowsOrdersBefore = SQLHelper.getRowsAmountFrom(ordersTable);
            long rowsPaymentsBefore = SQLHelper.getRowsAmountFrom(paymentsTable);

            paymentForm.fillForm(DataHelper.generateCardDataWithShiftedYearFromCurrent(expiryYears - 1));
            paymentForm.sendForm();

            paymentForm.checkSuccessNotification(10);
            assertEquals(rowsOrdersBefore + 1, SQLHelper.getRowsAmountFrom(ordersTable));
            assertEquals(rowsPaymentsBefore + 1, SQLHelper.getRowsAmountFrom(paymentsTable));
            SQLHelper.Payment lastEntry = SQLHelper.getLastEntryFromPaymentsTable();
            assertEquals(expectedPrice, lastEntry.getAmount());
            assertEquals(approved, lastEntry.getStatus());
        }


        @Test
        @DisplayName("4. Expiry date in current month")
        void shouldSendFormWithExpiryDateInCurrentMonth() {
            long rowsOrdersBefore = SQLHelper.getRowsAmountFrom(ordersTable);
            long rowsPaymentsBefore = SQLHelper.getRowsAmountFrom(paymentsTable);

            paymentForm.fillForm(DataHelper.generateCardDataWithShiftedMonthFromCurrent(0));
            paymentForm.sendForm();

            paymentForm.checkSuccessNotification(10);
            assertEquals(rowsOrdersBefore + 1, SQLHelper.getRowsAmountFrom(ordersTable));
            assertEquals(rowsPaymentsBefore + 1, SQLHelper.getRowsAmountFrom(paymentsTable));
            SQLHelper.Payment lastEntry = SQLHelper.getLastEntryFromPaymentsTable();
            assertEquals(expectedPrice, lastEntry.getAmount());
            assertEquals(approved, lastEntry.getStatus());
        }

        @Test
        @DisplayName("5. Expiry date in next month")
        void shouldSendFormWithExpiryDateInNextMonth() {
            long rowsOrdersBefore = SQLHelper.getRowsAmountFrom(ordersTable);
            long rowsPaymentsBefore = SQLHelper.getRowsAmountFrom(paymentsTable);

            paymentForm.fillForm(DataHelper.generateCardDataWithShiftedMonthFromCurrent(1));
            paymentForm.sendForm();

            paymentForm.checkSuccessNotification(10);
            assertEquals(rowsOrdersBefore + 1, SQLHelper.getRowsAmountFrom(ordersTable));
            assertEquals(rowsPaymentsBefore + 1, SQLHelper.getRowsAmountFrom(paymentsTable));
            SQLHelper.Payment lastEntry = SQLHelper.getLastEntryFromPaymentsTable();
            assertEquals(expectedPrice, lastEntry.getAmount());
            assertEquals(approved, lastEntry.getStatus());
        }

        @Test
        @DisplayName("6. Card owner length = 26 symbols (max-1)")
        void shouldSendFormWithCardOwnerInLessThanMaxSymbols() {
            long rowsOrdersBefore = SQLHelper.getRowsAmountFrom(ordersTable);
            long rowsPaymentsBefore = SQLHelper.getRowsAmountFrom(paymentsTable);

            paymentForm.fillForm(DataHelper.generateCardDataWithCardOwnerFixedLength(cardOwnerMaxLength - 1, expiryYears));
            paymentForm.sendForm();

            paymentForm.checkSuccessNotification(10);
            assertEquals(rowsOrdersBefore + 1, SQLHelper.getRowsAmountFrom(ordersTable));
            assertEquals(rowsPaymentsBefore + 1, SQLHelper.getRowsAmountFrom(paymentsTable));
            SQLHelper.Payment lastEntry = SQLHelper.getLastEntryFromPaymentsTable();
            assertEquals(expectedPrice, lastEntry.getAmount());
            assertEquals(approved, lastEntry.getStatus());
        }

        @Test
        @DisplayName("7. Card owner max length")
        void shouldSendFormWithCardOwnerMaxLength() {
            long rowsOrdersBefore = SQLHelper.getRowsAmountFrom(ordersTable);
            long rowsPaymentsBefore = SQLHelper.getRowsAmountFrom(paymentsTable);

            paymentForm.fillForm(DataHelper.generateCardDataWithCardOwnerFixedLength(cardOwnerMaxLength, expiryYears));
            paymentForm.sendForm();

            paymentForm.checkSuccessNotification(10);
            assertEquals(rowsOrdersBefore + 1, SQLHelper.getRowsAmountFrom(ordersTable));
            assertEquals(rowsPaymentsBefore + 1, SQLHelper.getRowsAmountFrom(paymentsTable));
            SQLHelper.Payment lastEntry = SQLHelper.getLastEntryFromPaymentsTable();
            assertEquals(expectedPrice, lastEntry.getAmount());
            assertEquals(approved, lastEntry.getStatus());
        }

        @Test
        @DisplayName("8. Card owner with hyphen")
        void shouldSendFormWithCardOwnerWithHyphen() {
            long rowsOrdersBefore = SQLHelper.getRowsAmountFrom(ordersTable);
            long rowsPaymentsBefore = SQLHelper.getRowsAmountFrom(paymentsTable);

            paymentForm.fillForm(DataHelper.generateCardDataWithHyphenCardOwner(expiryYears));
            paymentForm.sendForm();

            paymentForm.checkSuccessNotification(10);
            assertEquals(rowsOrdersBefore + 1, SQLHelper.getRowsAmountFrom(ordersTable));
            assertEquals(rowsPaymentsBefore + 1, SQLHelper.getRowsAmountFrom(paymentsTable));
            SQLHelper.Payment lastEntry = SQLHelper.getLastEntryFromPaymentsTable();
            assertEquals(expectedPrice, lastEntry.getAmount());
            assertEquals(approved, lastEntry.getStatus());
        }

        @Test
        @DisplayName("9. Valid data with zero CVC")
        void shouldSendFormWithZeroCVC() {
            long rowsOrdersBefore = SQLHelper.getRowsAmountFrom(ordersTable);
            long rowsPaymentsBefore = SQLHelper.getRowsAmountFrom(paymentsTable);

            paymentForm.fillForm(DataHelper.generateCardDataWithZeroCVC(expiryYears));
            paymentForm.sendForm();

            paymentForm.checkSuccessNotification(10);
            assertEquals(rowsOrdersBefore + 1, SQLHelper.getRowsAmountFrom(ordersTable));
            assertEquals(rowsPaymentsBefore + 1, SQLHelper.getRowsAmountFrom(paymentsTable));
            SQLHelper.Payment lastEntry = SQLHelper.getLastEntryFromPaymentsTable();
            assertEquals(expectedPrice, lastEntry.getAmount());
            assertEquals(approved, lastEntry.getStatus());
        }

        @Test
        @DisplayName("10. Declined payment")
        void shouldSendFormWithDeclinedCard() {
            long rowsOrdersBefore = SQLHelper.getRowsAmountFrom(ordersTable);
            long rowsPaymentsBefore = SQLHelper.getRowsAmountFrom(paymentsTable);

            paymentForm.fillForm(DataHelper.generateCardDataWithDeclinedCard(expiryYears));
            paymentForm.sendForm();

            paymentForm.checkErrorNotification(10);
            assertEquals(rowsOrdersBefore + 1, SQLHelper.getRowsAmountFrom(ordersTable));
            assertEquals(rowsPaymentsBefore + 1, SQLHelper.getRowsAmountFrom(paymentsTable));
            SQLHelper.Payment lastEntry = SQLHelper.getLastEntryFromPaymentsTable();
            assertEquals(expectedPrice, lastEntry.getAmount());
            assertEquals(declined, lastEntry.getStatus());
        }
    }

    @Test
    @DisplayName("11. Empty card number")
    void shouldNotSendFormWithoutCardNumber() {
        paymentForm.fillForm(DataHelper.generateCardDataWithEmptyCard(expiryYears));
        paymentForm.sendForm();

        paymentForm.checkCardNumberError("Неверный формат", 2);
    }

    @Test
    @DisplayName("12. Empty month")
    void shouldNotSendFormWithoutMonth() {
        paymentForm.fillForm(DataHelper.generateCardDataWithEmptyMonth(expiryYears));
        paymentForm.sendForm();

        paymentForm.checkMonthError("Неверный формат",2);
    }

    @Test
    @DisplayName("13. Empty year")
    void shouldNotSendFormWithoutYear() {
        paymentForm.fillForm(DataHelper.generateCardDataWithEmptyYear(expiryYears));
        paymentForm.sendForm();

        paymentForm.checkYearError("Неверный формат",2);
    }

    @Test
    @DisplayName("14. Empty card owner")
    void shouldNotSendFormWithoutCardOwner() {
        paymentForm.fillForm(DataHelper.generateCardDataWithEmptyCardOwner(expiryYears));
        paymentForm.sendForm();

        paymentForm.checkCardOwnerError("Поле обязательно для заполнения", 2);
    }

    @Test
    @DisplayName("15. Empty CVC/CVV")
    void shouldNotSendFormWithoutCVC() {
        paymentForm.fillForm(DataHelper.generateCardDataWithEmptyCVC(expiryYears));
        paymentForm.sendForm();

        paymentForm.checkCVCError("Неверный формат",2);
    }

    @Test
    @DisplayName("16. Empty form")
    void shouldNotSendEmptyForm() {
        paymentForm.sendForm();

        paymentForm.checkCardNumberError("Неверный формат",2);
        paymentForm.checkMonthError("Неверный формат", 2);
        paymentForm.checkYearError("Неверный формат", 2);
        paymentForm.checkCardOwnerError("Поле обязательно для заполнения", 2);
        paymentForm.checkCVCError("Неверный формат", 2);
    }

    @Test
    @DisplayName("17. Incomplete card number")
    void shouldNotSendFormWithIncompleteCardNumber() {
        paymentForm.fillForm(DataHelper.generateCardDataWithIncompleteNumber(expiryYears, cardNumberMaxLength - 1));
        paymentForm.sendForm();

        paymentForm.checkCardNumberError("Неверный формат", 2);
    }

    @Test
    @DisplayName("18. Enter more symbols, than card number length")
    void shouldNotEnterMoreSymbolsInCardNumber() {
        String cardNumberExt = DataHelper.generateNumericCode(cardNumberMaxLength + 1);
        String expected = cardNumberExt.substring(0, cardNumberMaxLength);

        paymentForm.fillCard(cardNumberExt);
        String actual = paymentForm.getCardValue().replaceAll("\\s+","");

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("19. Enter invalid symbols in card number")
    void shouldNotEnterInvalidCardNumber() {
        paymentForm.fillCard(DataHelper.getInvalidSymbolsForNumericFields());
        String actual = paymentForm.getCardValue().replaceAll("\\s+","");

        assertEquals("", actual);
    }

    @Test
    @DisplayName("20. Card number consists of zeroes")
    void shouldNotSendFormWithZeroCard() {
        paymentForm.fillForm(DataHelper.generateCardDataWithZeroCard(expiryYears));
        paymentForm.sendForm();

        paymentForm.checkCardNumberError("Неверный формат", 2);
    }

    @Test
    @DisplayName("21. Zero month")
    void shouldNotSendFormWithZeroMonth() {
        paymentForm.fillForm(DataHelper.generateCardDataWithZeroMonth(expiryYears));
        paymentForm.sendForm();

        paymentForm.checkMonthError("Неверный формат", 2);
    }

    @Test
    @DisplayName("22. Non-existent month")
    void shouldNotSendFormWithWrongMonth() {
        paymentForm.fillForm(DataHelper.generateCardDataWithWrongMonth(expiryYears));
        paymentForm.sendForm();

        paymentForm.checkMonthError("Неверно указан срок действия карты", 2);
    }

    @Test
    @DisplayName("23. Incomplete month (only 1 digit)")
    void shouldNotSendFormWithIncompleteMonth() {
        paymentForm.fillForm(DataHelper.generateCardDataWithMonthInvalidLength(1, expiryYears));
        paymentForm.sendForm();

        paymentForm.checkMonthError("Неверный формат", 2);
    }

    @Test
    @DisplayName("24. Enter more symbols, than month length")
    void shouldNotEnterMoreSymbolsInMonth() {
        String monthExt = DataHelper.generateNumericCode(3);
        String expected = monthExt.substring(0, 2);

        paymentForm.fillMonth(monthExt);
        String actual = paymentForm.getMonthValue();

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("25. Enter invalid symbols in month")
    void shouldNotEnterInvalidSymbolsInMonth() {
        paymentForm.fillMonth(DataHelper.getInvalidSymbolsForNumericFields());
        String actual = paymentForm.getMonthValue();

        assertEquals("", actual);
    }

    @Test
    @DisplayName("26. Zero year")
    void shouldNotSendFormWithZeroYear() {
        paymentForm.fillForm(DataHelper.generateCardDataWithZeroYear());
        paymentForm.sendForm();

        paymentForm.checkYearError("Истёк срок действия карты", 2);
    }

    @Test
    @DisplayName("27. Incomplete year")
    void shouldNotSendFormWithIncompleteYear() {
        paymentForm.fillForm(DataHelper.generateCardDataWithYearInvalidLength(1));
        paymentForm.sendForm();

        paymentForm.checkYearError("Неверный формат", 2);
    }

    @Test
    @DisplayName("28. Enter more symbols, than year length")
    void shouldNotEnterMoreSymbolsInYear() {
        String yearExt = DataHelper.generateNumericCode(3);
        String expected = yearExt.substring(0, 2);

        paymentForm.fillYear(yearExt);
        String actual = paymentForm.getYearValue();

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("29. Enter invalid symbols in year")
    void shouldNotEnterInvalidSymbolsInYear() {
        paymentForm.fillYear(DataHelper.getInvalidSymbolsForNumericFields());
        String actual = paymentForm.getYearValue();

        assertEquals("", actual);
    }

    @Test
    @DisplayName("30. Previous year (card expired)")
    void shouldNotSendFormWithPrevYear() {
        paymentForm.fillForm(DataHelper.generateCardDataWithShiftedYearFromCurrent(-1));
        paymentForm.sendForm();

        paymentForm.checkYearError("Истёк срок действия карты", 2);
    }

    @Test
    @DisplayName("31. More than expiry years")
    void shouldNotSendFormWithMoreThanExpiryYear() {
        paymentForm.fillForm(DataHelper.generateCardDataWithShiftedYearFromCurrent(expiryYears + 1));
        paymentForm.sendForm();

        paymentForm.checkYearError("Неверно указан срок действия карты", 2);
    }

    @Test
    @DisplayName("32. Card expired in previous month")
    void shouldNotSendFormWithExpiryDateInPrevMonth() {
        paymentForm.fillForm(DataHelper.generateCardDataWithShiftedMonthFromCurrent(-1));
        paymentForm.sendForm();

        paymentForm.checkMonthError("Неверно указан срок действия карты", 2);
    }

    @Test
    @DisplayName("33. Incomplete card owner (only one word)")
    void shouldNotSendFormWithIncompleteCardOwner() {
        paymentForm.fillForm(DataHelper.generateCardDataWithIncompleteCardOwner(expiryYears));
        paymentForm.sendForm();

        paymentForm.checkCardOwnerError("Введите имя и фамилию, как указано на карте", 2);
    }

    @Test
    @DisplayName("34. Cyrillic symbols in card owner")
    void shouldNotSendFormWithCyrillicCardOwner() {
        paymentForm.fillForm(DataHelper.generateCardDataWithInvalidCardOwnerLocale("ru", expiryYears));
        paymentForm.sendForm();

        paymentForm.checkCardOwnerError("Только латинские символы (A-Z), пробел и дефис", 2);
    }

    @Test
    @DisplayName("35. Invalid symbols in card owner")
    void shouldNotSendFormWithInvalidCardOwner() {
        paymentForm.fillForm(DataHelper.generateCardDataWithInvalidCardOwnerSymbols(expiryYears));
        paymentForm.sendForm();

        paymentForm.checkCardOwnerError("Только латинские символы (A-Z), пробел и дефис", 2);
    }

    @Test
    @DisplayName("36. Spaces instead of card owner")
    void shouldNotSendFormWithSpacesInCardOwner() {
        paymentForm.fillForm(DataHelper.generateCardDataWithCardOwnerSpaces(expiryYears));
        paymentForm.sendForm();

        paymentForm.checkCardOwnerError("Поле обязательно для заполнения", 2);
    }

    @Test
    @DisplayName("37. Card owner length more than 27 symbols")
    void shouldNotSendFormWithOverlongCardOwner() {
        paymentForm.fillForm(DataHelper.generateCardDataWithCardOwnerFixedLength(cardOwnerMaxLength + 1, expiryYears));
        paymentForm.sendForm();

        paymentForm.checkCardOwnerError("Введите имя и фамилию, как указано на карте", 2);
    }


    @Test
    @DisplayName("38. Incomplete CVC")
    void shouldNotSendFormWithIncompleteCVC() {
        paymentForm.fillForm(DataHelper.generateCardDataWithCVCInvalidLength(2, expiryYears));
        paymentForm.sendForm();

        paymentForm.checkCVCError("Неверный формат", 2);
    }

    @Test
    @DisplayName("39. Enter more symbols, than CVC length")
    void shouldNotEnterMoreSymbolsInCVC() {
        String cvcExt = DataHelper.generateNumericCode(4);
        String expected = cvcExt.substring(0, 3);

        paymentForm.fillCVC(cvcExt);
        String actual = paymentForm.getCVCValue();

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("40. Enter invalid symbols in CVC")
    void shouldNotEnterInvalidSymbolsInCVC() {
        paymentForm.fillCVC(DataHelper.getInvalidSymbolsForNumericFields());
        String actual = paymentForm.getCVCValue();

        assertEquals("", actual);
    }

}
