import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import config.env_target;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Login extends env_target {

    private WebDriverWait wait;

    @BeforeEach
    void setUp() {
        // setup driver chrome
        System.setProperty("webdriver.chrome.driver", "src\\main\\resources\\drivers\\chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--incognito", "--disable-save-password-bubble");
        driver = new ChromeDriver(options);

        // buka browser
        driver.manage().window().maximize();
        driver.get(baseUrl);

        // inisialisasi explicit wait
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @Test
    void positiveLogin() {
        // tunggu tombol login siap
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login-button")));

        // isi form login
        driver.findElement(By.id("user-name")).sendKeys("standard_user");
        driver.findElement(By.id("password")).sendKeys("secret_sauce");
        driver.findElement(By.id("login-button")).click();

        // validasi halaman products terbuka
        wait.until(ExpectedConditions.urlContains("inventory.html"));
        wait.until(ExpectedConditions.textToBe(By.cssSelector(".title"), "Products"));
    }

    @Test
    void negativeLoginUsernameSalah() {
        // tunggu tombol login siap
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login-button")));

        // isi form login dengan password salah
        driver.findElement(By.id("user-name")).sendKeys("standard_user1");
        driver.findElement(By.id("password")).sendKeys("secret_sauce");
        driver.findElement(By.id("login-button")).click();

        // validasi error message muncul
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("h3[data-test='error']")));

        // ambil error text
        String errorText = driver.findElement(By.cssSelector("h3[data-test='error']")).getText();

        // validasi teks error sesuai ekspektasi
        assertEquals(
                "Epic sadface: Username and password do not match any user in this service",
                errorText
        );

    }

    @Test
    void negativeLoginPasswordSalah() {
        // tunggu tombol login siap
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login-button")));

        // isi form login dengan password salah
        driver.findElement(By.id("user-name")).sendKeys("standard_user");
        driver.findElement(By.id("password")).sendKeys("secret_sauce1");
        driver.findElement(By.id("login-button")).click();

        // validasi error message muncul
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("h3[data-test='error']")));

        // ambil error text
        String errorText = driver.findElement(By.cssSelector("h3[data-test='error']")).getText();

        // validasi teks error sesuai ekspektasi
        assertEquals(
                "Epic sadface: Username and password do not match any user in this service",
                errorText
        );

    }

    @Test
    void negativeLoginUsernamePasswordKosong() {
        // tunggu tombol login siap
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login-button")));

        // tidak mengisi username dan password dan langsung klik button
        driver.findElement(By.id("login-button")).click();

        // validasi error message muncul
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("h3[data-test='error']")));

        // ambil error text
        String errorText = driver.findElement(By.cssSelector("h3[data-test='error']")).getText();

        // validasi teks error sesuai ekspektasi
        assertEquals(
                "Epic sadface: Username is required",
                errorText
        );

    }

    @Test
    void negativeLoginUsernameKosong() {
        // tunggu tombol login siap
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login-button")));

        // tidak mengisi username dan langsung klik button
        driver.findElement(By.id("password")).sendKeys("secret_sauce");
        driver.findElement(By.id("login-button")).click();

        // validasi error message muncul
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("h3[data-test='error']")));

        // ambil error text
        String errorText = driver.findElement(By.cssSelector("h3[data-test='error']")).getText();

        // validasi teks error sesuai ekspektasi
        assertEquals(
                "Epic sadface: Username is required",
                errorText
        );

    }

    @Test
    void negativeLoginPasswordKosong() {
        // tunggu tombol login siap
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login-button")));

        // tidak mengisi username dan langsung klik button
        driver.findElement(By.id("user-name")).sendKeys("standard_user");
        driver.findElement(By.id("login-button")).click();

        // validasi error message muncul
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("h3[data-test='error']")));

        // ambil error text
        String errorText = driver.findElement(By.cssSelector("h3[data-test='error']")).getText();

        // validasi teks error sesuai ekspektasi
        assertEquals(
                "Epic sadface: Password is required",
                errorText
        );

    }

    @Test
    void logout() {
        // login dulu
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login-button")));
        driver.findElement(By.id("user-name")).sendKeys("standard_user");
        driver.findElement(By.id("password")).sendKeys("secret_sauce");
        driver.findElement(By.id("login-button")).click();

        // validasi sudah masuk inventory
        wait.until(ExpectedConditions.urlToBe("https://www.saucedemo.com/inventory.html"));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[@class = \"title\"] [text() = 'Products']\n")));

        // buka menu lalu klik logout
        driver.findElement(By.id("react-burger-menu-btn")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("logout_sidebar_link"))).click();

        // validasi balik ke halaman login
        wait.until(ExpectedConditions.urlToBe("https://www.saucedemo.com/"));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login-button")));
    }

    @AfterEach
    void tearDown() {
        // tutup browser
        if (driver != null) driver.quit();
    }
}
