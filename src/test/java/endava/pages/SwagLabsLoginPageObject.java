package endava.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static ui.Browser.drv;
import static ui.Browser.getUrl;

/**
 * Page object to represent <a href="https://www.saucedemo.com/">www.saucedemo.com</a> login page
 */
public class SwagLabsLoginPageObject {
    @FindBy(id = "user-name")
    WebElement usernameInput;
    @FindBy(id = "password")
    WebElement passwordInput;
    @FindBy(id = "login-button")
    WebElement loginButton;
    public static final String PAGE_URL = "https://www.saucedemo.com/";
    private static final Logger logger = LoggerFactory.getLogger(SwagLabsLoginPageObject.class.getSimpleName());

    public SwagLabsLoginPageObject() {
        logger.info("Initializing {} page", PAGE_URL);
        PageFactory.initElements(drv, this);
    }

    public void enterUsername(String username) {
        logger.info("Entering username [{}]", username);
        usernameInput.sendKeys(username);
    }

    public void enterPassword(String password) {
        logger.info("Entering password [{}]", password);
        passwordInput.sendKeys(password);
    }

    public void clickLoginButton() {
        logger.info("Clicking LOGIN button");
        loginButton.click();
    }

    /**
     * Assess if browser currently open page is this one or not
     *
     * @return true if this is the opened page, false otherwise
     */
    public boolean isAt() {
        logger.info("Checking if we are currently on the SwagLabsLoginPage");
        return PAGE_URL.equalsIgnoreCase(getUrl())
                && usernameInput != null
                && passwordInput != null
                && loginButton != null;
    }

}
