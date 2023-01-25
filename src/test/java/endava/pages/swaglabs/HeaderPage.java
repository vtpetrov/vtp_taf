package endava.pages.swaglabs;

import base.BasePageObject;
import endava.pages.models.HamburgerMenu;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ui.SeleniumHelpers;
import utils.Misc;

import java.util.List;

import static ui.Browser.drv;

/**
 * Page object to represent the HEADER on all pages -> hamburger menu, swag labs logo, cart icon, title
 */
//@Data
public class HeaderPage extends BasePageObject {
    @FindBy(className = "title")
    protected WebElement title;

    @FindBy(id = "react-burger-menu-btn")
    protected WebElement burgerMenuBtn;

    @FindBy(id = "shopping_cart_container")
    protected WebElement shoppingCartElem;

    @FindBy(className = "shopping_cart_link")
    protected WebElement shoppingCartLink;
    private static final Logger logger = LoggerFactory.getLogger(HeaderPage.class.getSimpleName());

    public HeaderPage() {
        super();
        init();
    }

    private void init() {
//        logger.info("Initializing HEADER object");
        PageFactory.initElements(drv, this);
    }

    /**
     * This shows how many items there are currently in the cart
     * @return the value found or 0 by default
     */
    public int getShoppingCartBadgeValue() {
        this.init();
        int returnVal = 0;
        List<WebElement> shoppingCartBadge = shoppingCartElem.findElements(By.className("shopping_cart_badge"));
        WebElement badge = Misc.getElementSafe(shoppingCartBadge, 0);
        if(badge != null) {
            try {
                returnVal = Integer.parseInt(badge.getText());
            } catch (NumberFormatException ignored) {
            }
        }

        return returnVal;
    }

    /**
     * This action navigates to the Shopping Cart
     */
    public void clickShoppingCartIcon() {
        this.init();
        logger.info("Navigating to shopping cart...");
        SeleniumHelpers.clickWebElemSafelyOrFail(SeleniumHelpers.waitUntilClickable(4, shoppingCartLink));
    }

    /**
     * Naming alias overload for the method ({@link HeaderPage#clickShoppingCartIcon()})
     */
    public void goToShoppingCart() {
        clickShoppingCartIcon();
    }
    public HamburgerMenu clickHamburgerMenu() {
        this.init();
        SeleniumHelpers.clickWebElemSafelyOrFail(SeleniumHelpers.waitUntilClickable(4, burgerMenuBtn));
        HamburgerMenu hamburgerMenu = new HamburgerMenu();
        return hamburgerMenu;
    }

    /**
     * Asserts the count of items added to the cart and visible/displayed on the cart badge in the Header
     * @param expectedValue the expected number of items (count)
     */
    public void assertShoppingCartBadgeValue(int expectedValue) {
        this.init();
        logger.info("Asserting cart icon badge shows a certain count => {}", expectedValue);
        // verify Cart icon top-right has number X on it:
        int actualShoppingCartBadgeValue = this.getShoppingCartBadgeValue();
        Assertions.assertEquals(expectedValue, actualShoppingCartBadgeValue,"Actual badge count doesn't match expected!");

    }

    protected String getTitle() {
        if (this.title != null) {
            return this.title.getText();
        } else {
            return "";
        }
    }
}
