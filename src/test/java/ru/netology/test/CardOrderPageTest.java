package ru.netology.test;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.List;

import static org.apache.commons.lang3.StringUtils.contains;
import static org.junit.jupiter.api.Assertions.*;


public class CardOrderPageTest {
    private WebDriver driver;

    @BeforeAll
    static void setAppAll() {
        System.setProperty("webdriver.chrome.driver", "webdriver/win/chromedriver.exe");
        WebDriverManager.chromedriver().setup();

    }


    @BeforeEach
    void setup() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        driver = new ChromeDriver(options);
    }

    @AfterEach
    void teardown() {
        driver.quit();
        driver = null;
    }

    //Наполнение страницы соответствует требованиям

    @Test
    void shouldCardNameStartPage() {
        driver.get("http://localhost:9999/");
        String cardName = driver.findElement(By.className("heading_size_m")).getText();
        assertEquals("Альфа-Карта", cardName.trim());

    }

    @Test
    void shouldHeadingStartPage() {
        driver.get("http://localhost:9999/");

        String heading = driver.findElement(By.className("heading_size_l")).getText();
        assertEquals("Заявка на дебетовую карту", heading.trim());

    }

    @Test
    void shouldPlaceholderName() {
        driver.get("http://localhost:9999/");

        List<WebElement> elementsPlaceholder = driver.findElements(By.className("input__top"));
        String placeholderName = elementsPlaceholder.get(0).getText();
        assertEquals("Фамилия и имя", placeholderName);


    }

    @Test
    void shouldPlaceholderTel() {
        driver.get("http://localhost:9999/");

        List<WebElement> elementsPlaceholder = driver.findElements(By.className("input__top"));
        String placeholderTel = elementsPlaceholder.get(1).getText();
        assertEquals("Мобильный телефон", placeholderTel);

    }

    @Test
    void shouldSubName() {
        driver.get("http://localhost:9999/");

        List<WebElement> elementsSub = driver.findElements(By.className("input__sub"));
        String subName = elementsSub.get(0).getText();
        assertEquals("Укажите точно как в паспорте", subName);


    }

    @Test
    void shouldSubTel() {
        driver.get("http://localhost:9999/");
        List<WebElement> elementsSub = driver.findElements(By.className("input__sub"));
        String subTel = elementsSub.get(1).getText();
        assertEquals("На указанный номер моб. тел. будет отправлен смс-код для подтверждения заявки на карту. Проверьте, что номер ваш и введен корректно.", subTel);

    }

    @Test
    void shouldCheckboxText() {
        driver.get("http://localhost:9999/");
        String checkboxText = driver.findElement(By.className("checkbox__text")).getText();
        assertEquals("Я соглашаюсь с условиями обработки и использования моих персональных данных и разрешаю сделать запрос в бюро кредитных историй", checkboxText);
    }

    @Test
    void shouldCheckboxStatus() {
        driver.get("http://localhost:9999/");
        String checkboxStatus = driver.findElement(By.className("checkbox__control")).getAttribute("autocomplete");
        assertEquals("off", checkboxStatus);
    }

    @Test
    void shouldButtonText() {
        driver.get("http://localhost:9999/");
        String buttonText = driver.findElement(By.className("button__text")).getText();
        assertEquals("Продолжить", buttonText);
    }

//Функционал страницы соответствует требованиям

    @Test
    void getClickNameClass() {
        driver.get("http://localhost:9999/");
        driver.findElement(By.cssSelector("[type=\"text\"]")).click();
        String classTapName = driver.findElement(By.cssSelector("[data-test-id=\"name\"]")).getAttribute("class");
        String expected = "input_focused";
        assertTrue(classTapName.contains(expected));
    }

    @Test
    void getClickTabNameClass() {
        driver.get("http://localhost:9999/");
        driver.findElement(By.cssSelector("[type=\"text\"]")).click();
        driver.findElement(By.className("checkbox__box")).click();
        String classTapTel = driver.findElement(By.cssSelector("[data-test-id=\"name\"]")).getAttribute("class");
        String expected = "input_focused";
        assertFalse(classTapTel.contains(expected));
    }

    @Test
    void getClickTelClass() {
        driver.get("http://localhost:9999/");
        driver.findElement(By.cssSelector("[type=\"tel\"]")).click();
        String classTapTel = driver.findElement(By.cssSelector("[data-test-id=\"phone\"]")).getAttribute("class");
        String expected = "input_focused";
        assertTrue(classTapTel.contains(expected));
    }

    @Test
    void getClickTabTelClass() {
        driver.get("http://localhost:9999/");
        driver.findElement(By.cssSelector("[type=\"tel\"]")).click();
        driver.findElement(By.className("checkbox__box")).click();
        String classTapTel = driver.findElement(By.cssSelector("[data-test-id=\"phone\"]")).getAttribute("class");
        String expected = "input_focused";
        assertFalse(classTapTel.contains(expected));
    }

    @Test
    void getClickCheckboxClass() {
        driver.get("http://localhost:9999/");
        driver.findElement(By.className("checkbox__box")).click();
        String classTapCheckbox = driver.findElement(By.cssSelector("[data-test-id=\"agreement\"]")).getAttribute("class");
        String expected = "checkbox_checked";
        assertTrue(classTapCheckbox.contains(expected));
    }

    @ParameterizedTest
    @CsvSource({
            "Дмитрий Третьяков,+79999999999",
            "Анна Мария Петрова,+79999999999",
            "Николай Муравьев-Амурский,+79999999999"
    })
    void shouldSendApplication(String name, String tel) {
        driver.get("http://localhost:9999/");
        driver.findElement(By.cssSelector("[type=\"text\"]")).sendKeys(name);
        driver.findElement(By.cssSelector("[type=\"tel\"]")).sendKeys(tel);
        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.className("button__text")).click();
        String text = driver.findElement(By.className("paragraph")).getText();
        assertTrue(text.contains("Ваша заявка успешно отправлена!"));

    }

    //должна быть настроена валидация инпутов

    @ParameterizedTest
    @CsvSource({
            "Ivan",
            "null",
            "Сергей 3"
    }

    )
    void validationName(String name) {
        driver.get("http://localhost:9999/");

        driver.findElement(By.cssSelector("[type=\"text\"]")).sendKeys(name);
        driver.findElement(By.cssSelector("[type=\"tel\"]")).sendKeys("+79991231234");
        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.className("button__text")).click();
        String text = driver.findElement(By.cssSelector("[data-test-id=\"name\"]")).getText();
        assertTrue(text.contains("Имя и Фамилия указаные неверно"));


    }

    @ParameterizedTest
    @CsvSource({
            "8(423)1231234",
            "+7(999)1231234",
            "89991231234",
            "+7123456789",
            "+89991231234",
            "null"
    }

    )
    void validationPhone(String tel) {
        driver.get("http://localhost:9999/");

        driver.findElement(By.cssSelector("[type=\"text\"]")).sendKeys("Иван Севастьянов");
        driver.findElement(By.cssSelector("[type=\"tel\"]")).sendKeys(tel);
        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.className("button__text")).click();
        String textPhone = driver.findElement(By.cssSelector("[data-test-id=\"phone\"]")).getText();
        assertTrue(textPhone.contains("Телефон указан неверно"));


    }

    @Test
    void validationCheckbox() {
        driver.get("http://localhost:9999/");
        driver.findElement(By.cssSelector("[type=\"text\"]")).sendKeys("Иван Севастьяов");
        driver.findElement(By.cssSelector("[type=\"tel\"]")).sendKeys("+79991231234");
        driver.findElement(By.className("button__text")).click();
       String classCheckbox = driver.findElement(By.cssSelector("[data-test-id=\"agreement\"]")).getAttribute("class");
        assertTrue(classCheckbox.contains("input_invalid"));

    }

    @Test
    void shouldNotSendEmptyForm() {
        driver.get("http://localhost:9999/");
        driver.findElement(By.cssSelector("[type=\"text\"]")).sendKeys("null");
        driver.findElement(By.cssSelector("[type=\"tel\"]")).sendKeys("null");
        driver.findElement(By.className("button__text")).click();
        String classCheckbox = driver.findElement(By.cssSelector("[data-test-id=\"name\"]")).getAttribute("class");
        assertTrue(classCheckbox.contains("input_invalid"));

    }

}
