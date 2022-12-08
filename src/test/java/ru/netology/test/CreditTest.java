package ru.netology.test;

import org.junit.jupiter.api.*;
import ru.netology.helpers.DataHelper;
import ru.netology.helpers.SQLHelper;
import ru.netology.pages.CreditFormPage;
import ru.netology.pages.DashboardPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CreditTest extends TestBase {
    static DashboardPage dashboardPage;
    static CreditFormPage creditForm;

    final String creditRequestsTable = "credit_request_entity";

    @BeforeEach
    void openHost() {
        open(testHost);

        dashboardPage = new DashboardPage();
        creditForm = dashboardPage.openCreditForm();
    }

    @Nested
    class IncreasedTimeout {

        @Test
        @DisplayName("41. Credit approved (Happy path)")
        void shouldApprovePaymentWithValidData() {
            long rowsOrdersBefore = SQLHelper.getRowsAmountFrom(ordersTable);
            long rowsCreditsBefore = SQLHelper.getRowsAmountFrom(creditRequestsTable);

            creditForm.fillForm(DataHelper.generateValidCardData(expiryYears));
            creditForm.sendForm();

            creditForm.checkSuccessNotification(10); // проверка уведомления в UI
            // проверки на изменение кол-ва записей в БД (что точно добавилась новая запись)
            assertEquals(rowsOrdersBefore + 1, SQLHelper.getRowsAmountFrom(ordersTable));
            assertEquals(rowsCreditsBefore + 1, SQLHelper.getRowsAmountFrom(creditRequestsTable));
            // проверка статуса последней записи
            assertEquals(approved, SQLHelper.getLastStatusFromCreditsTable());
        }

        @Test
        @DisplayName("42. Max expiry year")
        void shouldSendFormWithMaxExpiryYear() {
            long rowsOrdersBefore = SQLHelper.getRowsAmountFrom(ordersTable);
            long rowsCreditsBefore = SQLHelper.getRowsAmountFrom(creditRequestsTable);

            creditForm.fillForm(DataHelper.generateCardDataWithShiftedYearFromCurrent(expiryYears));
            creditForm.sendForm();

            creditForm.checkSuccessNotification(10);
            assertEquals(rowsOrdersBefore + 1, SQLHelper.getRowsAmountFrom(ordersTable));
            assertEquals(rowsCreditsBefore + 1, SQLHelper.getRowsAmountFrom(creditRequestsTable));
            assertEquals(approved, SQLHelper.getLastStatusFromCreditsTable());
        }

        @Test
        @DisplayName("43. Max-1 expiry year")
        void shouldSendFormWithExpiryYearBelowMax() {
            long rowsOrdersBefore = SQLHelper.getRowsAmountFrom(ordersTable);
            long rowsCreditsBefore = SQLHelper.getRowsAmountFrom(creditRequestsTable);

            creditForm.fillForm(DataHelper.generateCardDataWithShiftedYearFromCurrent(expiryYears - 1));
            creditForm.sendForm();

            creditForm.checkSuccessNotification(10);
            assertEquals(rowsOrdersBefore + 1, SQLHelper.getRowsAmountFrom(ordersTable));
            assertEquals(rowsCreditsBefore + 1, SQLHelper.getRowsAmountFrom(creditRequestsTable));
            assertEquals(approved, SQLHelper.getLastStatusFromCreditsTable());
        }


        @Test
        @DisplayName("44. Expiry date in current month")
        void shouldSendFormWithExpiryDateInCurrentMonth() {
            long rowsOrdersBefore = SQLHelper.getRowsAmountFrom(ordersTable);
            long rowsCreditsBefore = SQLHelper.getRowsAmountFrom(creditRequestsTable);

            creditForm.fillForm(DataHelper.generateCardDataWithShiftedMonthFromCurrent(0));
            creditForm.sendForm();

            creditForm.checkSuccessNotification(10);
            assertEquals(rowsOrdersBefore + 1, SQLHelper.getRowsAmountFrom(ordersTable));
            assertEquals(rowsCreditsBefore + 1, SQLHelper.getRowsAmountFrom(creditRequestsTable));
            assertEquals(approved, SQLHelper.getLastStatusFromCreditsTable());
        }

        @Test
        @DisplayName("45. Expiry date in next month")
        void shouldSendFormWithExpiryDateInNextMonth() {
            long rowsOrdersBefore = SQLHelper.getRowsAmountFrom(ordersTable);
            long rowsCreditsBefore = SQLHelper.getRowsAmountFrom(creditRequestsTable);

            creditForm.fillForm(DataHelper.generateCardDataWithShiftedMonthFromCurrent(1));
            creditForm.sendForm();

            creditForm.checkSuccessNotification(10);
            assertEquals(rowsOrdersBefore + 1, SQLHelper.getRowsAmountFrom(ordersTable));
            assertEquals(rowsCreditsBefore + 1, SQLHelper.getRowsAmountFrom(creditRequestsTable));
            assertEquals(approved, SQLHelper.getLastStatusFromCreditsTable());
        }

        @Test
        @DisplayName("46. Card owner length = 26 symbols (max-1)")
        void shouldSendFormWithCardOwnerInLessThanMaxSymbols() {
            long rowsOrdersBefore = SQLHelper.getRowsAmountFrom(ordersTable);
            long rowsCreditsBefore = SQLHelper.getRowsAmountFrom(creditRequestsTable);

            creditForm.fillForm(DataHelper.generateCardDataWithCardOwnerFixedLength(cardOwnerMaxLength - 1, expiryYears));
            creditForm.sendForm();

            creditForm.checkSuccessNotification(10);
            assertEquals(rowsOrdersBefore + 1, SQLHelper.getRowsAmountFrom(ordersTable));
            assertEquals(rowsCreditsBefore + 1, SQLHelper.getRowsAmountFrom(creditRequestsTable));
            assertEquals(approved, SQLHelper.getLastStatusFromCreditsTable());
        }

        @Test
        @DisplayName("47. Card owner max length")
        void shouldSendFormWithCardOwnerMaxLength() {
            long rowsOrdersBefore = SQLHelper.getRowsAmountFrom(ordersTable);
            long rowsCreditsBefore = SQLHelper.getRowsAmountFrom(creditRequestsTable);

            creditForm.fillForm(DataHelper.generateCardDataWithCardOwnerFixedLength(cardOwnerMaxLength, expiryYears));
            creditForm.sendForm();

            creditForm.checkSuccessNotification(10);
            assertEquals(rowsOrdersBefore + 1, SQLHelper.getRowsAmountFrom(ordersTable));
            assertEquals(rowsCreditsBefore + 1, SQLHelper.getRowsAmountFrom(creditRequestsTable));
            assertEquals(approved, SQLHelper.getLastStatusFromCreditsTable());
        }

        @Test
        @DisplayName("48. Card owner with hyphen")
        void shouldSendFormWithCardOwnerWithHyphen() {
            long rowsOrdersBefore = SQLHelper.getRowsAmountFrom(ordersTable);
            long rowsCreditsBefore = SQLHelper.getRowsAmountFrom(creditRequestsTable);

            creditForm.fillForm(DataHelper.generateCardDataWithHyphenCardOwner(expiryYears));
            creditForm.sendForm();

            creditForm.checkSuccessNotification(10);
            assertEquals(rowsOrdersBefore + 1, SQLHelper.getRowsAmountFrom(ordersTable));
            assertEquals(rowsCreditsBefore + 1, SQLHelper.getRowsAmountFrom(creditRequestsTable));
            assertEquals(approved, SQLHelper.getLastStatusFromCreditsTable());
        }

        @Test
        @DisplayName("49. Valid data with zero CVC")
        void shouldSendFormWithZeroCVC() {
            long rowsOrdersBefore = SQLHelper.getRowsAmountFrom(ordersTable);
            long rowsCreditsBefore = SQLHelper.getRowsAmountFrom(creditRequestsTable);

            creditForm.fillForm(DataHelper.generateCardDataWithZeroCVC(expiryYears));
            creditForm.sendForm();

            creditForm.checkSuccessNotification(10);
            assertEquals(rowsOrdersBefore + 1, SQLHelper.getRowsAmountFrom(ordersTable));
            assertEquals(rowsCreditsBefore + 1, SQLHelper.getRowsAmountFrom(creditRequestsTable));
            assertEquals(approved, SQLHelper.getLastStatusFromCreditsTable());
        }

        @Test
        @DisplayName("50. Declined credit request")
        void shouldSendFormWithDeclinedCard() {
            long rowsOrdersBefore = SQLHelper.getRowsAmountFrom(ordersTable);
            long rowsCreditsBefore = SQLHelper.getRowsAmountFrom(creditRequestsTable);

            creditForm.fillForm(DataHelper.generateCardDataWithDeclinedCard(expiryYears));
            creditForm.sendForm();

            creditForm.checkErrorNotification(10);
            assertEquals(rowsOrdersBefore + 1, SQLHelper.getRowsAmountFrom(ordersTable));
            assertEquals(rowsCreditsBefore + 1, SQLHelper.getRowsAmountFrom(creditRequestsTable));
            assertEquals(declined, SQLHelper.getLastStatusFromCreditsTable());
        }
    }


    @Test
    @DisplayName("51. Empty card number")
    void shouldNotSendFormWithoutCardNumber() {
        creditForm.fillForm(DataHelper.generateCardDataWithEmptyCard(expiryYears));
        creditForm.sendForm();

        creditForm.checkCardNumberError("Неверный формат", 2);
    }

    @Test
    @DisplayName("52. Empty month")
    void shouldNotSendFormWithoutMonth() {
        creditForm.fillForm(DataHelper.generateCardDataWithEmptyMonth(expiryYears));
        creditForm.sendForm();

        creditForm.checkMonthError("Неверный формат", 2);
    }

    @Test
    @DisplayName("53. Empty year")
    void shouldNotSendFormWithoutYear() {
        creditForm.fillForm(DataHelper.generateCardDataWithEmptyYear(expiryYears));
        creditForm.sendForm();

        creditForm.checkYearError("Неверный формат", 2);
    }

    @Test
    @DisplayName("54. Empty card owner")
    void shouldNotSendFormWithoutCardOwner() {
        creditForm.fillForm(DataHelper.generateCardDataWithEmptyCardOwner(expiryYears));
        creditForm.sendForm();

        creditForm.checkCardOwnerError("Поле обязательно для заполнения", 2);
    }

    @Test
    @DisplayName("55. Empty CVC/CVV")
    void shouldNotSendFormWithoutCVC() {
        creditForm.fillForm(DataHelper.generateCardDataWithEmptyCVC(expiryYears));
        creditForm.sendForm();

        creditForm.checkCVCError("Неверный формат", 2);
    }

    @Test
    @DisplayName("56. Empty form")
    void shouldNotSendEmptyForm() {
        creditForm.sendForm();

        creditForm.checkCardNumberError("Неверный формат", 2);
        creditForm.checkMonthError("Неверный формат", 2);
        creditForm.checkYearError("Неверный формат", 2);
        creditForm.checkCardOwnerError("Поле обязательно для заполнения", 2);
        creditForm.checkCVCError("Неверный формат", 2);
    }

    @Test
    @DisplayName("57. Incomplete card number")
    void shouldNotSendFormWithIncompleteCardNumber() {
        creditForm.fillForm(DataHelper.generateCardDataWithIncompleteNumber(expiryYears, cardNumberMaxLength - 1));
        creditForm.sendForm();

        creditForm.checkCardNumberError("Неверный формат", 2);
    }

    @Test
    @DisplayName("58. Enter more symbols, than card number length")
    void shouldNotEnterMoreSymbolsInCardNumber() {
        String cardNumberExt = DataHelper.generateNumericCode(cardNumberMaxLength + 1);
        String expected = cardNumberExt.substring(0, cardNumberMaxLength);

        creditForm.fillCard(cardNumberExt);
        String actual = creditForm.getCardValue().replaceAll("\\s+","");

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("59. Enter invalid symbols in card number")
    void shouldNotEnterInvalidCardNumber() {
        creditForm.fillCard(DataHelper.getInvalidSymbolsForNumericFields());
        String actual = creditForm.getCardValue().replaceAll("\\s+","");

        assertEquals("", actual);
    }

    @Test
    @DisplayName("60. Card number consists of zeroes")
    void shouldNotSendFormWithZeroCard() {
        creditForm.fillForm(DataHelper.generateCardDataWithZeroCard(expiryYears));
        creditForm.sendForm();

        creditForm.checkCardNumberError("Неверный формат", 2);
    }

    @Test
    @DisplayName("61. Zero month")
    void shouldNotSendFormWithZeroMonth() {
        creditForm.fillForm(DataHelper.generateCardDataWithZeroMonth(expiryYears));
        creditForm.sendForm();

        creditForm.checkMonthError("Неверный формат", 2);
    }

    @Test
    @DisplayName("62. Non-existent month")
    void shouldNotSendFormWithWrongMonth() {
        creditForm.fillForm(DataHelper.generateCardDataWithWrongMonth(expiryYears));
        creditForm.sendForm();

        creditForm.checkMonthError("Неверно указан срок действия карты", 2);
    }

    @Test
    @DisplayName("63. Incomplete month (only 1 digit)")
    void shouldNotSendFormWithIncompleteMonth() {
        creditForm.fillForm(DataHelper.generateCardDataWithMonthInvalidLength(1, expiryYears));
        creditForm.sendForm();

        creditForm.checkMonthError("Неверный формат", 2);
    }

    @Test
    @DisplayName("64. Enter more symbols, than month length")
    void shouldNotEnterMoreSymbolsInMonth() {
        String monthExt = DataHelper.generateNumericCode(3);
        String expected = monthExt.substring(0, 2);

        creditForm.fillMonth(monthExt);
        String actual = creditForm.getMonthValue();

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("65. Enter invalid symbols in month")
    void shouldNotEnterInvalidSymbolsInMonth() {
        creditForm.fillMonth(DataHelper.getInvalidSymbolsForNumericFields());
        String actual = creditForm.getMonthValue();

        assertEquals("", actual);
    }

    @Test
    @DisplayName("66. Zero year")
    void shouldNotSendFormWithZeroYear() {
        creditForm.fillForm(DataHelper.generateCardDataWithZeroYear());
        creditForm.sendForm();

        creditForm.checkYearError("Истёк срок действия карты", 2);
    }

    @Test
    @DisplayName("67. Incomplete year")
    void shouldNotSendFormWithIncompleteYear() {
        creditForm.fillForm(DataHelper.generateCardDataWithYearInvalidLength(1));
        creditForm.sendForm();

        creditForm.checkYearError("Неверный формат", 2);
    }

    @Test
    @DisplayName("68. Enter more symbols, than year length")
    void shouldNotEnterMoreSymbolsInYear() {
        String yearExt = DataHelper.generateNumericCode(3);
        String expected = yearExt.substring(0, 2);

        creditForm.fillYear(yearExt);
        String actual = creditForm.getYearValue();

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("69. Enter invalid symbols in year")
    void shouldNotEnterInvalidSymbolsInYear() {
        creditForm.fillYear(DataHelper.getInvalidSymbolsForNumericFields());
        String actual = creditForm.getYearValue();

        assertEquals("", actual);
    }

    @Test
    @DisplayName("70. Previous year (card expired)")
    void shouldNotSendFormWithPrevYear() {
        creditForm.fillForm(DataHelper.generateCardDataWithShiftedYearFromCurrent(-1));
        creditForm.sendForm();

        creditForm.checkYearError("Истёк срок действия карты", 2);
    }

    @Test
    @DisplayName("71. More than expiry years")
    void shouldNotSendFormWithMoreThanExpiryYear() {
        creditForm.fillForm(DataHelper.generateCardDataWithShiftedYearFromCurrent(expiryYears + 1));
        creditForm.sendForm();

        creditForm.checkYearError("Неверно указан срок действия карты", 2);
    }

    @Test
    @DisplayName("72. Card expired in previous month")
    void shouldNotSendFormWithExpiryDateInPrevMonth() {
        creditForm.fillForm(DataHelper.generateCardDataWithShiftedMonthFromCurrent(-1));
        creditForm.sendForm();

        creditForm.checkMonthError("Неверно указан срок действия карты", 2);
    }

    @Test
    @DisplayName("73. Incomplete card owner (only one word)")
    void shouldNotSendFormWithIncompleteCardOwner() {
        creditForm.fillForm(DataHelper.generateCardDataWithIncompleteCardOwner(expiryYears));
        creditForm.sendForm();

        creditForm.checkCardOwnerError("Введите имя и фамилию, как указано на карте", 2);
    }

    @Test
    @DisplayName("74. Cyrillic symbols in card owner")
    void shouldNotSendFormWithCyrillicCardOwner() {
        creditForm.fillForm(DataHelper.generateCardDataWithInvalidCardOwnerLocale("ru", expiryYears));
        creditForm.sendForm();

        creditForm.checkCardOwnerError("Только латинские символы (A-Z), пробел и дефис", 2);
    }

    @Test
    @DisplayName("75. Invalid symbols in card owner")
    void shouldNotSendFormWithInvalidCardOwner() {
        creditForm.fillForm(DataHelper.generateCardDataWithInvalidCardOwnerSymbols(expiryYears));
        creditForm.sendForm();

        creditForm.checkCardOwnerError("Только латинские символы (A-Z), пробел и дефис", 2);
    }

    @Test
    @DisplayName("76. Spaces instead of card owner")
    void shouldNotSendFormWithSpacesInCardOwner() {
        creditForm.fillForm(DataHelper.generateCardDataWithCardOwnerSpaces(expiryYears));
        creditForm.sendForm();

        creditForm.checkCardOwnerError("Поле обязательно для заполнения", 2);
    }

    @Test
    @DisplayName("77. Card owner length more than 27 symbols")
    void shouldNotSendFormWithOverlongCardOwner() {
        creditForm.fillForm(DataHelper.generateCardDataWithCardOwnerFixedLength(cardOwnerMaxLength + 1, expiryYears));
        creditForm.sendForm();

        creditForm.checkCardOwnerError("Введите имя и фамилию, как указано на карте", 2);
    }


    @Test
    @DisplayName("78. Incomplete CVC")
    void shouldNotSendFormWithIncompleteCVC() {
        creditForm.fillForm(DataHelper.generateCardDataWithCVCInvalidLength(2, expiryYears));
        creditForm.sendForm();

        creditForm.checkCVCError("Неверный формат", 2);
    }

    @Test
    @DisplayName("79. Enter more symbols, than CVC length")
    void shouldNotEnterMoreSymbolsInCVC() {
        String cvcExt = DataHelper.generateNumericCode(4);
        String expected = cvcExt.substring(0, 3);

        creditForm.fillCVC(cvcExt);
        String actual = creditForm.getCVCValue();

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("80. Enter invalid symbols in CVC")
    void shouldNotEnterInvalidSymbolsInCVC() {
        creditForm.fillCVC(DataHelper.getInvalidSymbolsForNumericFields());
        String actual = creditForm.getCVCValue();

        assertEquals("", actual);
    }

}
