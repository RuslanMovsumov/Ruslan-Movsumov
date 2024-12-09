import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class OnlineRechargePageTest {
    private WebDriver driver;
    private OnlineRechargePage onlineRechargePage;

    @BeforeClass
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        onlineRechargePage = new OnlineRechargePage(driver);
        driver.get("https://mts.by/online-recharge");
    }

    @Test
    public void testEmptyFieldPlaceholders() {
        onlineRechargePage.selectServiceType("Услуги связи");
        onlineRechargePage.enterPhoneNumber("");
        onlineRechargePage.clickContinueButton();

        assertEquals("Введите номер карты", onlineRechargePage.getEmptyFieldPlaceholder());
    }

    @Test
    public void testServicePaymentDetails() {
        onlineRechargePage.selectServiceType("Услуги связи");
        onlineRechargePage.enterPhoneNumber("297777777");
        onlineRechargePage.clickContinueButton();

        assertEquals("Ожидаемая сумма", onlineRechargePage.getDisplayedAmount());
        assertEquals("297777777", onlineRechargePage.getDisplayedPhone());

        // Проверка на наличие плейсхолдеров
        assertEquals("Введите номер карты", onlineRechargePage.getCardNumberPlaceholder());
        assertEquals("Введите срок действия карты", onlineRechargePage.getExpirationDatePlaceholder());
        assertEquals("Введите CVC", onlineRechargePage.getCVCPlaceholder());

        List<String> actualLogos = onlineRechargePage.getPaymentSystemLogos();
        assertTrue(actualLogos.contains("Visa"));
        assertTrue(actualLogos.contains("MasterCard"));
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
