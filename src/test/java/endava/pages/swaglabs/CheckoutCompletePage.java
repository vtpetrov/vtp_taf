package endava.pages.swaglabs;

import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ui.SeleniumHelpers;

import static ui.Browser.drv;
import static ui.Browser.getUrl;

/**
 * Checkout complete page (greetings)
 */
public class CheckoutCompletePage extends HeaderPage {

    @FindBy(id = "back-to-products")
    private WebElement backHomeBtn; // back to Products (inventory) page

    @FindBy(className = "complete-header")
    private WebElement completeHeader;

    @FindBy(className = "complete-text")
    private WebElement completeText;

    public static final String PAGE_URL = "https://www.saucedemo.com/checkout-complete.html";
    public static final String PAGE_TITLE = "Checkout: Complete!";
    public static final String COMPLETE_HEADER_VALUE = "THANK YOU FOR YOUR ORDER";
    public static final String COMPLETE_TEXT_VALUE = "Your order has been dispatched, and will arrive just as fast as the pony can get there!";
    private static final Logger logger = LoggerFactory.getLogger(CheckoutCompletePage.class.getSimpleName());

    public CheckoutCompletePage() {
        super();
        init();
    }

    private void init() {
//        logger.info("Initializing {} page", PAGE_URL);
        PageFactory.initElements(drv, this);
    }

    /**
     * Cancel checkout and go back to Products (Inventory) page
     */
    public void clickBackHomeBtn() {
        this.init();
        SeleniumHelpers.clickWebElemSafelyOrFail(SeleniumHelpers.waitUntilClickable(4, backHomeBtn));
    }

    /**
     * Assess if browser currently open page is this one or not
     *
     * @return true if this is the opened page, false otherwise
     */
    public boolean isAt() {
        init();
        logger.info("Checking if we are currently on the CheckoutCompletePage");
        return PAGE_URL.equalsIgnoreCase(getUrl())
                && this.getTitle().equalsIgnoreCase(CheckoutCompletePage.PAGE_TITLE)
                && this.completeHeader != null
                && this.completeText != null
                && this.backHomeBtn != null;
    }

    public void assertCompleteHeader() {
        init();
        Assertions.assertEquals(CheckoutCompletePage.COMPLETE_HEADER_VALUE
                , this.completeHeader.getText()
                , "Actual Checkout complete header value doesn't match expected!");
    }

    public void assertCompleteText() {
        init();
        Assertions.assertEquals(CheckoutCompletePage.COMPLETE_TEXT_VALUE
                , this.completeText.getText()
                , "Actual Checkout complete text value doesn't match expected!");
    }
}
