import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class XpertOperator {

    private static WebDriver driver;
    public WebDriverWait wait;

    private boolean isProductSearched = false;
    private boolean isProductSelected = false;
    private boolean isProductAddedToCart = false;
    private boolean isProductRemovedFromCart = false;
    private boolean isCartViewed = false;
    private boolean isCheckingOut = false;
    private boolean isUserLoggedIn = false;
    private boolean isUserLoggedOut = true;

    boolean loginUser(String username, String password) {
        driver = new ChromeDriver();
        driver.get("https://www.xpert.mt");
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement cookieAcceptButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("eu-cookie-ok")));
        cookieAcceptButton.click();

        WebElement myAccountLink = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[@data-loginurl='/en/login']")));
        myAccountLink.click();

        WebElement emailInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("Email")));
        WebElement passwordInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("Password")));

        emailInput.sendKeys(username);
        passwordInput.sendKeys(password);

        WebElement loginButton = driver.findElement(By.cssSelector("button.button-1.login-button"));
        loginButton.click();

        isUserLoggedIn = true;
        isUserLoggedOut = false;
        return isUserLoggedIn;
    }
    public boolean isUserLoggedIn() {
        return isUserLoggedIn;
    }

    boolean searchForProduct(String searchQuery) {
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement searchInputBox = driver.findElement(By.id("small-searchterms"));
        searchInputBox.clear();
        searchInputBox.sendKeys(searchQuery);

        WebElement searchButton = driver.findElement(By.className("search-box-button"));
        searchButton.click();

        isProductSearched = true;
        return isProductSearched;
    }

    public boolean isProductSearched() {
        return isProductSearched;
    }

    boolean selectFirstProduct() {

        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        try {
            List < WebElement > products = driver.findElements(By.cssSelector(".item-box .product-item"));
            if (!products.isEmpty()) {
                WebElement firstProductTitleLink = products.get(0).findElement(By.cssSelector("h2.product-title > a"));

                firstProductTitleLink.click();

                isProductSelected = true;
            } else {
                System.err.println("No products found on the page.");

                isProductSelected = false;
            }
        } catch (Exception e) {
            System.err.println("Error clicking first product: " + e.getMessage());

            isProductSelected = false;
        }

        return isProductSelected;
    }
    public boolean isProductSelected() {
        return isProductSelected;
    }

    boolean addProductToCart() {
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement addToCartButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(@class,'add-to-cart-button')]")));

        addToCartButton.click();

        isProductAddedToCart = true;
        return isProductAddedToCart;
    }

    public boolean isProductAddedToCart() {
        return isProductAddedToCart;
    }
    public boolean viewCart() {
        try {

            List < WebElement > cartPageIndicators = driver.findElements(By.xpath("//div[@class='page-title']/h1[text()='Shopping cart']"));
            if (!cartPageIndicators.isEmpty()) {
                isCartViewed = true;
                return true;
            }

            WebElement cartButton = wait.until(ExpectedConditions.elementToBeClickable(By.className("ico-cart")));
            cartButton.click();

            WebElement pageTitle = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='page-title']/h1")));
            boolean isShoppingCartPage = pageTitle.getText().trim().equals("Shopping cart");

            isCartViewed = isShoppingCartPage;
            return isShoppingCartPage;
        } catch (StaleElementReferenceException e) {

            driver.navigate().refresh();
            return viewCart();
        }
    }

    public boolean isCartViewed() {
        return isCartViewed;
    }

    boolean checkout() {
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement checkoutButton = wait.until(ExpectedConditions.elementToBeClickable(By.name("checkout")));
        checkoutButton.click();

        boolean checkoutPageVisible = false;

        try {

            WebElement checkoutTitle = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='page-title']/h1[text()='Checkout']")));
            if (checkoutTitle != null) {
                checkoutPageVisible = true;
            }
        } catch (Exception e) {

            try {
                WebElement signInMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='page-title']/h1[text()='Welcome, Please Sign In!']")));
                if (signInMessage != null) {
                    checkoutPageVisible = true;
                }
            } catch (Exception ex) {
                checkoutPageVisible = false;
            }
        }

        isCheckingOut = checkoutPageVisible;
        return isCheckingOut;
    }

    public boolean isCheckingOut() {
        return isCheckingOut;
    }

    public boolean logoutUser() {
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement myAccountLink = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(@class, 'ico-account') and contains(text(), 'My account')]")));
        myAccountLink.click();

        WebElement logoutButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(@class, 'ico-logout')]")));
        logoutButton.click();

        isUserLoggedOut = true;

        isUserLoggedIn = false;
        return isUserLoggedOut;
    }

    public boolean isUserLoggedOut() {
        return isUserLoggedOut;
    }
    boolean removeFromCart() {
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement cartPageTitle = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='page-title']/h1[text()='Shopping cart']")));
        WebElement removeButton = wait.until(ExpectedConditions.elementToBeClickable(By.name("updatecart")));
        try {
            removeButton.click();
            isProductRemovedFromCart = true;
        } catch (Exception ex) {
            isProductRemovedFromCart = false;
        }
        return isProductRemovedFromCart;
    }

    public boolean isProductRemovedFromCart() {
        return isProductRemovedFromCart;
    }

    public void closeBrowser() {
        if (driver != null) {
            try {
                driver.quit();
            } catch (Exception e) {}
        }
    }

}