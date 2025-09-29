import config.env_target;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CartTest extends env_target {
    private WebDriverWait wait;

    @BeforeEach
    void setUp() {
        // Setup driver
        System.setProperty("webdriver.chrome.driver", "src\\main\\resources\\drivers\\chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--incognito", "--disable-save-password-bubble");
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();

        // buka browser
        driver.manage().window().maximize();
        driver.get(baseUrl);

        // inisialisasi explicit wait
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // LOGIN UI (independent per test)
        // isi form login
        driver.findElement(By.id("user-name")).sendKeys("standard_user");
        driver.findElement(By.id("password")).sendKeys("secret_sauce");
        driver.findElement(By.id("login-button")).click();

        // validasi jika sudah di halaman utama
        wait.until(ExpectedConditions.urlToBe("https://www.saucedemo.com/inventory.html"));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[@class = \"title\"] [text() = 'Products']\n")));
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    void addProduct() {
        // klik button Add to cart
        driver.findElement(By.id("add-to-cart-sauce-labs-backpack")).click();

        // validasi angka badge berubah
        String cartBadge = wait
                .until(ExpectedConditions.visibilityOfElementLocated(By.className("shopping_cart_badge")))
                .getText();
        assertEquals("1", cartBadge);

        // validasi button berubah menjadi "Remove"
        String buttonText = driver.findElement(By.id("remove-sauce-labs-backpack")).getText();
        assertEquals("Remove", buttonText);

        // validasi data masuk ke cart
        driver.findElement(By.className("shopping_cart_link")).click();
        wait.until(ExpectedConditions.urlToBe("https://www.saucedemo.com/cart.html"));

        String namaProduk = driver.findElement(By.className("inventory_item_name")).getText();
        assertEquals("Sauce Labs Backpack", namaProduk);
    }

    @Test
    void removeProductInHome() {
        // klik button Add to cart
        driver.findElement(By.id("add-to-cart-sauce-labs-backpack")).click();

        // validasi angka badge berubah
        String cartBadge = wait
                .until(ExpectedConditions.visibilityOfElementLocated(By.className("shopping_cart_badge")))
                .getText();
        assertEquals("1", cartBadge);

        // validasi button berubah menjadi "Remove"
        String buttonRemoveText = driver.findElement(By.id("remove-sauce-labs-backpack")).getText();
        assertEquals("Remove", buttonRemoveText);

        // klik button Remove
        driver.findElement(By.id("remove-sauce-labs-backpack")).click();

        // validasi angka badge hilang
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className("shopping_cart_badge")));

        // validasi button kembali menjadi "Add to cart"
        String buttonAddToCartText = driver.findElement(By.id("add-to-cart-sauce-labs-backpack")).getText();
        assertEquals("Add to cart", buttonAddToCartText);
    }
}
