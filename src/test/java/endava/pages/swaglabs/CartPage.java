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
import utils.Misc;

import java.util.ArrayList;
import java.util.List;

import static ui.Browser.drv;

/**
 * Page object to represent <a href="https://www.saucedemo.com/cart.html">www.saucedemo.com/cart.html</a> page
 */
@Data
public class CartPage extends HeaderPage {
    @FindBy(id = "cart_contents_container")
    private WebElement cartContentContainer;

    List<CartProduct> products = new ArrayList<>();
    private final List<WebElement> cartItems;

    public static final String PAGE_URL = "https://www.saucedemo.com/cart.html";
    private static final Logger logger = LoggerFactory.getLogger(CartPage.class.getSimpleName());

    public CartPage() {
        super();
        logger.info("Initializing {} page", PAGE_URL);
        PageFactory.initElements(drv, this);

        cartItems = cartContentContainer.findElements(By.className("cart_item"));
        for (WebElement itemWebElement : cartItems) {
            CartProduct product = new CartProduct(itemWebElement);
            products.add(product);
        }
    }

    /**
     * Remove item from the Cart (click it's 'REMOVE' button)
     * @param index the index of the item (zero based)
     */
    public void removeItemFromCart(int index) {
        CartProduct itemToRemove = Misc.getElementSafe(products, index);

        if(itemToRemove != null) {
            itemToRemove.clickRemoveButton();
        } else {
            Assertions.fail("Item not found to click it's 'REMOVE' button!");
        }

    }

}
