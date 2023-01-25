package endava.pages.models;

import lombok.Data;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ui.SeleniumHelpers;

import static ui.Browser.drv;

/**
 * Represent the burger menu with 4 options:
 * <li/> ALL ITEMS -> go back to Products / Inventory page
 * <li/> ABOUT -> goes to Sauce Lab home page <a href="https://saucelabs.com/">saucelabs.com</a>
 * <li/> LOGOUT -> logs out the user
 * <li/> RESET APP STATE -> at a glance does nothing. Link points to product page, but doesn't go there. Not sure if it works.
 */
@Data
public class HamburgerMenu {

    private static final Logger logger = LoggerFactory.getLogger(HamburgerMenu.class.getSimpleName());

    @FindBy(id = "inventory_sidebar_link")
    WebElement allItemsLink;

    @FindBy(id = "about_sidebar_link")
    WebElement aboutLink;

    @FindBy(id = "logout_sidebar_link")
    WebElement logoutLink;

    @FindBy(id = "reset_sidebar_link")
    WebElement resetAppStateLink;

    public HamburgerMenu() {
        init();
    }

    private void init() {
        PageFactory.initElements(drv, this);
    }

    public HamburgerMenu(WebElement productWebElem) {
        PageFactory.initElements(productWebElem, this);
    }

    public void clickAllItemsLink() {
        init();
        SeleniumHelpers.clickWebElemSafelyOrFail(SeleniumHelpers.waitUntilClickable(4, allItemsLink));
    }

    public void clickAboutLink() {
        init();
        SeleniumHelpers.clickWebElemSafelyOrFail(SeleniumHelpers.waitUntilClickable(4, aboutLink));
    }

    public void clickLogoutLink() {
        init();
        SeleniumHelpers.clickWebElemSafelyOrFail(SeleniumHelpers.waitUntilClickable(4, SeleniumHelpers.waitUntilClickable(4, logoutLink)));
    }

    public void clickResetStateLink() {
        init();
        SeleniumHelpers.clickWebElemSafelyOrFail(SeleniumHelpers.waitUntilClickable(4, resetAppStateLink));
    }

}
