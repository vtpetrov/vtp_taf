package endava.pages.swaglabs;

import endava.pages.models.CartProduct;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ui.SeleniumHelpers;
import utils.Misc;

import java.util.ArrayList;
import java.util.List;

import static ui.Browser.drv;
import static ui.Browser.getUrl;

/**
 * Show overview of products that will be ckecked-out, price total, payment info, etc.
 */
public class CheckoutOverviewPage extends HeaderPage {
    @FindBy(id = "checkout_summary_container")
    private WebElement checkoutSummaryContainer;

    @FindBy(id = "cancel")
    private WebElement cancelBtn; // back to Products (inventory) page

    @FindBy(id = "finish")
    private WebElement finishBtn;

    @FindBy(className = "summary_subtotal_label")
    private WebElement subTotalPriceInfo;

    @FindBy(id = "summary_tax_label")
    private WebElement taxInfo;

    @FindBy(id = "summary_total_label")
    private WebElement totalPriceInfo;

    private final List<CartProduct> products = new ArrayList<>();
    private List<WebElement> overviewItems;

    public static final String PAGE_URL = "https://www.saucedemo.com/checkout-step-two.html";
    public static final String PAGE_TITLE = "Checkout: Overview";
    private static final Logger logger = LoggerFactory.getLogger(CheckoutOverviewPage.class.getSimpleName());

    public CheckoutOverviewPage() {
        super();
        init();
    }

    private void init() {
//        logger.info("Initializing {} page", PAGE_URL);
        PageFactory.initElements(drv, this);

        this.overviewItems = this.checkoutSummaryContainer.findElements(By.className("cart_item"));
        this.products.clear();
        for (WebElement itemWebElement : this.overviewItems) {
            CartProduct product = new CartProduct(itemWebElement);
            this.products.add(product);
        }
    }

    /**
     * Cancel checkout and go back to Products (Inventory) page
     */
    public void clickCancelBtn() {
        this.init();
        SeleniumHelpers.clickWebElemSafelyOrFail(SeleniumHelpers.waitUntilClickable(4, cancelBtn));
    }

    public void clickFinishBtn() {
        this.init();
        SeleniumHelpers.clickWebElemSafelyOrFail(SeleniumHelpers.waitUntilClickable(4, finishBtn));
    }

    /**
     * Naming alias overload for the method ({@link CheckoutOverviewPage#clickFinishBtn()})
     */
    public void finishCheckout() {
        clickFinishBtn();
    }

    /**
     * Assess if browser currently open page is this one or not
     *
     * @return true if this is the opened page, false otherwise
     */
    public boolean isAt() {
        init();
        logger.info("Checking if we are currently on the CheckoutOverviewPage");
        return PAGE_URL.equalsIgnoreCase(getUrl())
                && this.getTitle().equalsIgnoreCase(CheckoutOverviewPage.PAGE_TITLE)
                && this.checkoutSummaryContainer != null
                && !this.overviewItems.isEmpty()
                && this.finishBtn != null
                && this.cancelBtn != null;
    }

    /**
     * Get product from the checkout overview and return the instance of java object of type {@link CartProduct}
     * @param productIndex index of the product to get
     * @return the product itself or null if not found
     */
    public CartProduct getProduct(int productIndex) {
        this.init();
        return Misc.getElementSafe(products, productIndex);
    }

    public void assertPriceSubtotalValue(Double expectedSubTotal) {
    }

    public void assertPriceTaxValue(Double expectedTaxValue) {
    }

    public void assertPriceTotalValue(Double expectedTotal) {
    }
}
