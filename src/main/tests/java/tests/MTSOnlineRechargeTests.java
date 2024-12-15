package tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.By; // Импорт для использования By
import pageobjects.OnlineRechargePage;
import java.util.List; // Импорт для использования List

import static org.junit.jupiter.api.Assertions.*;

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
    public void testBlockTitle() {
        assertEquals("Онлайн пополнение без комиссии", onlineRechargePage.getBlockTitle());
    }

    @Test
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
    public void testMoreInfoLink() {
        onlineRechargePage.clickMoreInfoLink();
        assertTrue(driver.getCurrentUrl().contains("https://www.mts.by/help/poryadok-oplaty-i-bezopasnost-internet-platezhey/")); 
        driver.navigate().back();
    }

    @Test
    public void testFormFunctionality() {
        onlineRechargePage.selectServiceType("Услуги связи");
        onlineRechargePage.enterPhoneNumber("297777777");
        onlineRechargePage.clickContinueButton();
        assertTrue(onlineRechargePage.isResultMessageDisplayed());
    } 
    
    @Test
    public void testEmptyFieldErrors() {
        String[] serviceTypes = {"Услуги связи", "Домашний интернет", "Рассрочка", "Задолженность"};

        for (String serviceType : serviceTypes) {
            onlineRechargePage.selectServiceType(serviceType);
            onlineRechargePage.clickContinueButton();

            // Проверка надписей в незаполненных полях
            assertTrue(onlineRechargePage.isFieldErrorDisplayed("Номер телефона"), "Ошибка не отображается для типа услуги: " + serviceType);
        }
    }

   @Test
    public void testServiceTypeOnlineRecharge() {
        String phoneNumber = "297777777"; // Корректный номер телефона
        String expectedAmount = "100"; // Ожидаемая сумма
        String cardNumber = "4111111111111111"; // Корректный номер карты
        String expiryDate = "12/25"; // Корректная дата истечения
        String cvc = "123"; // Корректный CVC

        onlineRechargePage.selectServiceType("Услуги связи");
        onlineRechargePage.enterPhoneNumber(phoneNumber);
        onlineRechargePage.clickContinueButton();

        // Проверка корректного отображения суммы и номера телефона
        assertEquals(expectedAmount, onlineRechargePage.getDisplayedAmount(), "Отображаемая сумма корректна.");
        assertEquals(phoneNumber, onlineRechargePage.getDisplayedPhoneNumber(), "Отображаемый номер телефона корректен.");

        // Проверка незаполненных полей для реквизитов карты
        assertTrue(onlineRechargePage.isFieldErrorDisplayed("Номер карты"));
        assertTrue(onlineRechargePage.isFieldErrorDisplayed("Срок действия"));
        assertTrue(onlineRechargePage.isFieldErrorDisplayed("CVC"));
        
        // Проверка наличия логотипов платежных систем
        List<String> paymentLogos = onlineRechargePage.getPaymentSystemLogosAltTexts();
        assertFalse(paymentLogos.isEmpty(), "Логотипы платежных систем не найдены.");
        System.out.println("Логотипы платежных систем найдены."); // Вывод сообщения об успешной проверке
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
   
     