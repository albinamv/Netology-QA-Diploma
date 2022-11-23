package ru.netology.helpers;

import com.github.javafaker.Faker;
import lombok.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Random;

public class DataHelper {
    static Random random = new Random();

    private DataHelper() {
    }

    // вложенный класс
    // все данные карты
    @Value
    public static class CardData {
        private String cardNumber;
        private String month;
        private String year;
        private String cardOwner;
        private String cvc;
    }

    private static String getApprovedCardNumber() {
        return "4444 4444 4444 4441";
    }

    private static String getDeclinedCardNumber() {
        return "4444 4444 4444 4442";
    }

    public static String generateCardOwner(String locale) {
        Faker faker = new Faker(new Locale(locale));
        return faker.name().firstName().toUpperCase(Locale.ROOT) + " " + faker.name().lastName().toUpperCase(Locale.ROOT);
    }

    public static String generateMonth() {
        return String.format("%02d", (random.nextInt(12) + 1));
    }

    public static String generateShiftedYearFromCurrent(int shift) {
        LocalDate date = LocalDate.now().plusYears(shift);
        return DateTimeFormatter.ofPattern("yy").format(date);
    }

    public static String generateNumericCode(int length) {
        Faker faker = new Faker();
        return faker.number().digits(length);
    }

    public static CardData generateValidCardData(int expiryYears) {
        return new CardData(getApprovedCardNumber(), generateMonth(), generateShiftedYearFromCurrent(random.nextInt(expiryYears) + 1), generateCardOwner("en"), generateNumericCode(3));
    }

    public static CardData generateCardDataWithDeclinedCard(int expiryYears) {
        return new CardData(getDeclinedCardNumber(), generateMonth(), generateShiftedYearFromCurrent(random.nextInt(expiryYears) + 1), generateCardOwner("en"), generateNumericCode(3));
    }

    // для генерации срока действия, истекающего месяцем ранее / в текущем месяце / в следующем месяце
    public static CardData generateCardDataExpireInShiftedMonthFromCurrent(int shiftInMonths) {
        LocalDate date = LocalDate.now().plusMonths(shiftInMonths);

        return new CardData(getApprovedCardNumber(), DateTimeFormatter.ofPattern("MM").format(date), DateTimeFormatter.ofPattern("yy").format(date), generateCardOwner("en"), generateNumericCode(3));

    }

    // для генерации срока действия, истекающего через 4-5-6 лет
    public static CardData generateCardDataWithShiftedYearFromCurrent(int shiftInYears) {
        LocalDate date = LocalDate.now().plusYears(shiftInYears);

        return new CardData(getApprovedCardNumber(), generateMonth(), DateTimeFormatter.ofPattern("yy").format(date), generateCardOwner("en"), generateNumericCode(3));
    }

    public static CardData generateCardDataWithZeroCVC(int expiryYears) {
        return new CardData(getApprovedCardNumber(), generateMonth(), generateShiftedYearFromCurrent(random.nextInt(expiryYears) + 1), generateCardOwner("en"), "000");
    }

    public static CardData generateCardDataWithIncompleteNumber(int expiryYears, int length) {
        return new CardData(generateNumericCode(length), generateMonth(), generateShiftedYearFromCurrent(random.nextInt(expiryYears) + 1), generateCardOwner("en"), generateNumericCode(3));
    }

}
