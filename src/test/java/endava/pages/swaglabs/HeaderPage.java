package endava.pages.swaglabs;

import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.Misc;

import java.util.List;

import static ui.Browser.drv;

/**
 * Page object to represent the HEADER on all pages -> hamburger menu, swag labs logo, cart icon
 */
@Data
public class HeaderPage {
    @FindBy(id = "react-burger-menu-btn")
    protected WebElement burgerMenuBtn;

    @FindBy(id = "shopping_cart_container")
    protected WebElement shoppingCartElem;

    @FindBy(className = "shopping_cart_link")
    protected WebElement shoppingCartLink;
    private static final Logger logger = LoggerFactory.getLogger(HeaderPage.class.getSimpleName());

    public HeaderPage() {
        logger.info("Initializing HEADER object");
        PageFactory.initElements(drv, this);
    }

    /**
     * This shows how many items there are currently in the cart
     * @return the value found or 0 by default
     */
    public int getShoppingCartBadgeValue() {
        int returnVal = 0;
        List<WebElement> shoppingCartBadge = shoppingCartElem.findElements(By.className("shopping_cart_badge"));
        WebElement badge = Misc.getElementSafe(shoppingCartBadge, 0);
        if(badge != null) {
            returnVal = Integer.parseInt(badge.getText());
        }

        return returnVal;
    }

    /**
     * This action navigates to the Shopping Cart
     */
    public void clickShoppingCartIcon() {
        if(shoppingCartLink.isDisplayed() && shoppingCartLink.isEnabled()) {
            shoppingCartLink.click();
        } else {
            Assertions.fail("Shopping cart link not clickable!");
        }
    }

    public void clickHamburgerMenu() {
        if(burgerMenuBtn.isDisplayed() && burgerMenuBtn.isEnabled()) {
            burgerMenuBtn.click();
        } else {
            Assertions.fail("Hamburger menu not clickable!");
        }
    }
}
