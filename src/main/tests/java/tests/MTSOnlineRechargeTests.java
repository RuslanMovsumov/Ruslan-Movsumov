package tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import io.qameta.allure.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.By; 
import pageobjects.OnlineRechargePage;
import java.util.List; 

import static org.junit.jupiter.api.Assertions.*;

@Epic("Online Recharge Tests") 
@Feature("Payment Functionality") 
public class MTSOnlineRechargeTests {
    private WebDriver driver;
    private OnlineRechargePage onlineRechargePage;

    @BeforeEach
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--incognito"); // Открываем в режиме инкогнито
        driver = new ChromeDriver(options);
        driver.manage().window().maximize(); // Разворачиваем окно браузера

        driver.get("https://mts.by");

        // Принятие cookie
        driver.switchTo().activeElement();
        driver.findElement(By.cssSelector("#cookie-agree")).click();
        driver.switchTo().defaultContent();

        onlineRechargePage = new OnlineRechargePage(driver);
    }

    @Test
    @Description("Проверка заголовка блока онлайн пополнения") // Описание для теста
    public void testBlockTitle() {
        assertEquals("Онлайн пополнение без комиссии", onlineRechargePage.getBlockTitle());
    }

    @Test
    @Description("Проверка наличия логотипов платежных систем") // Описание для теста
    public void testPaymentSystemLogos() {
        List<String> actualLogoList = onlineRechargePage.arePaymentSystemLogosPresent();
        if (actualLogoList.isEmpty()) {
            fail("Логотипы не найдены");
        }

        assertAll(
            () -> { assertTrue(actualLogoList.contains("Visa")); },
            () -> { assertTrue(actualLogoList.contains("Verified By Visa")); },
            () -> { assertTrue(actualLogoList.contains("MasterCard")); },
            () -> { assertTrue(actualLogoList.contains("MasterCard Secure Code")); },
            () -> { assertTrue(actualLogoList.contains("Белкарт")); }
        );
    }

    @Test
    @Description("Проверка функциональности ссылки 'Подробнее'") // Описание для теста
    public void testMoreInfoLink() {
        onlineRechargePage.clickMoreInfoLink();
        assertTrue(driver.getCurrentUrl().contains("https://www.mts.by/help/poryadok-oplaty-i-bezopasnost-internet-platezhey/")); 
        driver.navigate().back();
    }

    @Test
    @Description("Проверка функциональности формы пополнения") // Описание для теста
    public void testFormFunctionality() {
        onlineRechargePage.selectServiceType("Услуги связи");
        onlineRechargePage.enterPhoneNumber("297777777");
        onlineRechargePage.clickContinueButton();
        assertTrue(onlineRechargePage.isResultMessageDisplayed());
    }

    @Test
    @Description("Проверка ошибок в полях для различных типов услуг") // Описание для теста
    public void testFieldErrorsForServiceTypes() {
        String[] serviceTypes = {"Услуги связи", "Домашний интернет", "Рассрочка", "Задолженность"};

        for (String serviceType : serviceTypes) {
            onlineRechargePage.selectServiceType(serviceType);
            onlineRechargePage.clickContinueButton();
            
            List<String> errors = onlineRechargePage.getFieldErrors();
            assertFalse(errors.isEmpty(), "Ошибки не найдены для типа услуги: " + serviceType);
        }
    }

    @Test
    @Description("Проверка онлайн пополнения для типа услуги") // Описание для теста
    public void testOnlineRechargeForServiceType() {
        String phoneNumber = "297777777"; 
        String expectedAmount = "100";  
        onlineRechargePage.selectServiceType("Услуги связи");
        onlineRechargePage.enterPhoneNumber(phoneNumber);
        onlineRechargePage.clickContinueButton();

        assertTrue(onlineRechargePage.isCorrectDisplayAfterContinue(expectedAmount, phoneNumber), 
            "Данные на экране найдены после продолжения.");

        // Проверка наличия иконок платежных систем
        List<String> actualLogos = onlineRechargePage.arePaymentSystemLogosPresent();
        assertFalse(actualLogos.isEmpty(), "Логотипы платежных систем найдены.");
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
