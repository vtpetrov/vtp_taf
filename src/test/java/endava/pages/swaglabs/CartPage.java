package endava.pages.swaglabs;

import endava.pages.models.CartProduct;
import lombok.Data;
import org.junit.jupiter.api.Assertions;
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
 * Page object to represent <a href="https://www.saucedemo.com/cart.html">www.saucedemo.com/cart.html</a> page
 */
@Data
public class CartPage extends HeaderPage {
    @FindBy(id = "cart_contents_container")
    private WebElement cartContentContainer;

    @FindBy(id = "continue-shopping")
    private WebElement continueShoppingBtn; // back to Products (inventory) page

    @FindBy(id = "checkout")
    private WebElement checkoutBtn;

    private List<CartProduct> products = new ArrayList<>();
    private List<WebElement> cartItems;

    public static final String PAGE_URL = "https://www.saucedemo.com/cart.html";
    public static final String PAGE_TITLE = "Your Cart";
    private static final Logger logger = LoggerFactory.getLogger(CartPage.class.getSimpleName());

    public CartPage() {
        super();
        init();
    }

    private void init() {
//        logger.info("Initializing {} page", PAGE_URL);
        PageFactory.initElements(drv, this);

        this.cartItems = this.cartContentContainer.findElements(By.className("cart_item"));
        this.products.clear();
        for (WebElement itemWebElement : this.cartItems) {
            CartProduct product = new CartProduct(itemWebElement);
            this.products.add(product);
        }
    }

    /**
     * Remove item from the Cart (click it's 'REMOVE' button)
     * @param index the index of the item (zero based)
     */
    public CartProduct removeItemFromCart(int index) {
        this.init();
        logger.info("removing item {} from cart", index + 1);
        CartProduct itemToRemove = Misc.getElementSafe(products, index);

        if(itemToRemove != null) {
            itemToRemove.clickRemoveButton();
        } else {
            Assertions.fail("Item not found to click it's 'REMOVE' button!");
        }

        return itemToRemove;
    }

    public void clickContinueShoppingBtn() {
        this.init();
        logger.info("Clicking 'continue shopping' button to go back to Products page");
        SeleniumHelpers.clickWebElemSafelyOrFail(SeleniumHelpers.waitUntilClickable(4, continueShoppingBtn));
    }

    public void clickCheckoutBtn() {
        this.init();
        logger.info("Going to checkout...");
        SeleniumHelpers.clickWebElemSafelyOrFail(SeleniumHelpers.waitUntilClickable(4, checkoutBtn));
    }

    /**
     * Naming alias overload for the method ({@link CartPage#clickCheckoutBtn()})
     */
    public void goToCheckout() {
        clickCheckoutBtn();
    }

    /**
     * Assess if browser currently open page is this one or not
     *
     * @return true if this is the opened page, false otherwise
     */
    public boolean isAt() {
        init();
        logger.info("Checking if we are currently on the CartPage");
        return PAGE_URL.equalsIgnoreCase(getUrl())
                && this.getTitle().equalsIgnoreCase(CartPage.PAGE_TITLE)
                && this.cartContentContainer != null
                && !this.cartItems.isEmpty()
                && this.checkoutBtn != null
                && this.continueShoppingBtn != null;
    }

    /**
     * Get product from the cart and return the instance of java object of type {@link CartProduct}
     * @param productIndex index of the product to get
     * @return the product itself or null if not found
     */
    public CartProduct getProduct(int productIndex) {
        this.init();
        return Misc.getElementSafe(products, productIndex);
    }

    public void assertProductsCount(int expectedCount) {
        init();
        Assertions.assertEquals(expectedCount, products.size(), "Actual products count in Cart doesn't match expected!");
    }
}
