package endava.pages.swaglabs;

import lombok.Data;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ui.SeleniumHelpers;

import static ui.Browser.drv;
import static ui.Browser.getUrl;

/**
 * Page object to represent <a href="https://www.saucedemo.com/checkout-step-one.html">www.saucedemo.com/checkout-step-one.html</a> page
 */
@Data
public class CheckOutPage extends HeaderPage {

    @FindBy(id = "first-name")
    private WebElement firstNameInput;

    @FindBy(id = "last-name")
    private WebElement lastNameInput;

    @FindBy(id = "postal-code")
    private WebElement zipPostCodeInput;

    @FindBy(id = "cancel")
    private WebElement cancelBtn; // go back to Cart

    @FindBy(id = "continue")
    private WebElement continueBtn;


    public static final String PAGE_URL = "https://www.saucedemo.com/checkout-step-one.html";
    public static final String PAGE_TITLE = "Checkout: Your Information";
    private static final Logger logger = LoggerFactory.getLogger(CheckOutPage.class.getSimpleName());

    public CheckOutPage() {
        super();
        init();
    }

    private void init() {
//        logger.info("Initializing {} page", PAGE_URL);
        PageFactory.initElements(drv, this);
    }

    public void enterFirstName(String firstName) {
        init();
        logger.info("Entering first name [{}]", firstName);
        firstNameInput.sendKeys(firstName);
    }

    public void enterLastName(String lastName) {
        init();
        logger.info("Entering last name [{}]", lastName);
        lastNameInput.sendKeys(lastName);
    }

    public void enterZipPostCode(String zipPostCode) {
        init();
        logger.info("Entering ZIP code [{}]", zipPostCode);
        zipPostCodeInput.sendKeys(zipPostCode);
    }

    public void clickCancelBtn() {
        this.init();
        SeleniumHelpers.clickWebElemSafelyOrFail(SeleniumHelpers.waitUntilClickable(4, this.cancelBtn));
    }

    public void clickContinueBtn() {
        this.init();
        SeleniumHelpers.clickWebElemSafelyOrFail(SeleniumHelpers.waitUntilClickable(4, this.continueBtn));
    }

    /**
     * Assess if browser currently open page is this one or not
     *
     * @return true if this is the opened page, false otherwise
     */
    public boolean isAt() {
        init();
        logger.info("Checking if we are currently on the CheckOutPage");
        return PAGE_URL.equalsIgnoreCase(getUrl())
                && this.getTitle().equalsIgnoreCase(CheckOutPage.PAGE_TITLE)
                && this.cancelBtn != null
                && this.continueBtn != null
                && this.firstNameInput != null
                && this.lastNameInput != null
                && this.zipPostCodeInput != null;
    }
}
