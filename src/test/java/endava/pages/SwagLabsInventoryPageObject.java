package endava.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static ui.Browser.drv;

/**
 * Page object to represent <a href="https://www.saucedemo.com/inventory.html">www.saucedemo.com/inventory.html</a> page
 */
public class SwagLabsInventoryPageObject {
    @FindBy(id = "react-burger-menu-btn")
    WebElement burgerMenuBtn;
    @FindBy(className = "product_sort_container")
    WebElement sortingSelectElement;
    @FindBy(id = "inventory_container")
    WebElement productsContainer;

    List<ProductPanel> products;
    Select sortingMenu;
    public static final String PAGE_URL = "https://www.saucedemo.com/inventory.html";
    private static final Logger logger = LoggerFactory.getLogger(SwagLabsInventoryPageObject.class.getSimpleName());

    public SwagLabsInventoryPageObject() {
        logger.info("Initializing {} page", PAGE_URL);
        PageFactory.initElements(drv, this);
        sortingMenu = new Select(sortingSelectElement);
        List<WebElement> inventoryItems = productsContainer.findElements(By.className("inventory_item"));

    }
//
//    public void enterUsername(String username) {
//        logger.info("Entering username [{}]", username);
//        usernameInput.sendKeys(username);
//    }
//
//    public void enterPassword(String password) {
//        logger.info("Entering password [{}]", password);
//        passwordInput.sendKeys(password);
//    }
//
//    public void clickLoginButton() {
//        logger.info("Clicking LOGIN button");
//        loginButton.click();
//    }

//    /**
//     * Assess if browser currently open page is this one or not
//     *
//     * @return true if this is the opened page, false otherwise
//     */
//    public boolean isAt() {
//        logger.info("Checking if we are currently on the SwagLabsLoginPage");
//        return PAGE_URL.equalsIgnoreCase(getUrl())
//                && usernameInput != null
//                && passwordInput != null
//                && loginButton != null;
//    }

}
