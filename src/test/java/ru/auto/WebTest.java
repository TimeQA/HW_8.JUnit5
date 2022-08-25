package ru.auto;
import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.stream.Stream;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byText;

import static com.codeborne.selenide.Selenide.*;

public class WebTest {

    //    Usual test. Result test not empty
    @DisplayName("Результаты поиска не пустые для запроса 'HUAWEI' на сайте магазина mvideo")
    @Test
    void MvideoTest() {
        open("https://www.mvideo.ru/");
        $("div.app-header-navbar input.input__field").setValue("HUAWEI");
        $("[type=\"search\"]").click();
        $$("ul li.c-category-list__item").
                shouldBe(CollectionCondition.sizeGreaterThan(0));
    }

    //    Parameterized test, two arguments input. Result test not empty
    @Disabled("Блокирует доступ к ресурсу при приверке второго параметра")
    @ValueSource(strings = {"Xiaomi", "HUAWEI"})
    @ParameterizedTest(name = "Результаты поиска не пустые для запроса {0}")
    void mvideoTest(String testData) {
        open("https://www.mvideo.ru/");
        $("div.app-header-navbar input.input__field").setValue(testData);
        $("[type=\"search\"]").click();
        $$("ul li.c-category-list__item").
                shouldBe(CollectionCondition.sizeGreaterThan(0));
    }

    //    Parameterized test, two arguments input. Result test not empty
    @ValueSource(strings = {"купить телефон", "купить ноутбук"})
    @ParameterizedTest(name = "Результы поиска не пустые для запроса {0}")
    void commonAvitoTest(String testData) {
        open("https://www.avito.ru/");
        $("[data-marker=\"search-form/suggest\"]").setValue(testData);
        $(".form-part-button-qO9Yf").click();
        $$("div[data-marker=\"item\"]").
                shouldBe(CollectionCondition.sizeGreaterThan(0));
    }

    //    Parameterized test, two arguments input and expected output two arguments. Result match expected.
    @CsvSource(value = {
            "купить смартфон, Телефоны",
            "купить ноутбук, Ноутбуки"
    })
    @ParameterizedTest(name = "Результы поиска содержат раздел \"{1}\" запроса \"{0}\"")
    void commonComplexAvitoTest(String testData, String expectedResult) {
        open("https://www.avito.ru/");
        $("[data-marker=\"search-form/suggest\"]").setValue(testData);
        $(".form-part-button-qO9Yf").click();
        $(".rubricator-root-SshbP").shouldHave(text(expectedResult));
    }

    //    Parameterized test, two arguments input and expected output array. Result match expected.
//    Ресурс блокирует доступ из-за автоматических запрсоов.
    static Stream<Arguments> commonComplexAvtoRuDropMenuTest() {
        return Stream.of(
                Arguments.of("Коммерческие", List.of("Лёгкие коммерческие", "Грузовики", "Седельные тягачи", "Автобусы",
                        "Прицепы и полуприцепы", "Сельскохозяйственная", "Строительная и дорожная",
                        "Погрузчики", "Автокраны", "Экскаваторы", "Бульдозеры", "Коммунальная")),
                Arguments.of("Мото", List.of("Мотоциклы", "Скутеры", "Мотовездеходы", "Снегоходы"))
        );
    }

    @MethodSource
    @ParameterizedTest(name = "Для типа транспорта \"{0}\" отображаются виды \"{1}\"")
    void commonComplexAvtoRuDropMenuTest(String typeTransport, List<String> expectedSubspeciesTransport) {
        open("https://auto.ru/");
//        Не помогает обходить капчу
        clearBrowserCookies();
        clearBrowserLocalStorage();
        $$(".HeaderMainNav__item").findBy(text(typeTransport)).hover();
        $$(".HeaderMainNav__menu_opened li").shouldHave(CollectionCondition.texts(expectedSubspeciesTransport));
//        Не помогает обходить капчу
        clearBrowserCookies();
        clearBrowserLocalStorage();
    }

//    Проверка правильности выбранных селекторов, так как прошлый тест полностью не выполняется
    @CsvSource(value = {
            "Коммерческие, Грузовики",
            "Мото, Скутеры"
    })
    @ParameterizedTest(name = "Результы поиска содержат раздел \"{1}\" запроса \"{0}\"")
    void commonAvtoRuTest(String testData, String expectedResult) {
        open("https://auto.ru/");
//        Не уверен нужно ли это здесь
        clearBrowserCookies();
        clearBrowserLocalStorage();
        $(".HeaderMainNav__list").$(byText(testData)).hover();
        $(".HeaderMainNav__menu_opened ul:nth-child(1)").shouldHave(text(expectedResult));
//        Не уверен нужно ли это здесь
        clearBrowserCookies();
        clearBrowserLocalStorage();
    }
}