package endava.pages.swaglabs;

import endava.pages.models.InventoryProduct;
import lombok.Getter;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.Misc;

import java.util.ArrayList;
import java.util.List;

import static ui.Browser.drv;
import static ui.Browser.getUrl;

/**
 * Page object to represent <a href="https://www.saucedemo.com/inventory.html">www.saucedemo.com/inventory.html</a> page
 */
//@Data
public class InventoryPage extends HeaderPage {
    @FindBy(className = "product_sort_container") // DOESN'T WORK!!!
    private WebElement sortingSelectElement;
    @FindBy(id = "inventory_container")
    private WebElement productsContainer;

    private final List<InventoryProduct> products = new ArrayList<>();
    private List<WebElement> inventoryItems;
    @Getter
    Select sortingMenu;
    public final String PAGE_URL = "https://www.saucedemo.com/inventory.html";
    public static final String PAGE_TITLE = "Products";
    private static final Logger logger = LoggerFactory.getLogger(InventoryPage.class.getSimpleName());

    public InventoryPage() {
        super();
        this.init();
    }

    private void init() {
//        logger.info("Initializing {} page", PAGE_URL);
        PageFactory.initElements(drv, this);

        this.sortingMenu = new Select(this.sortingSelectElement);
        this.inventoryItems = this.productsContainer.findElements(By.className("inventory_item"));
        this.products.clear();
        for (WebElement productWebElem : this.inventoryItems) {
            InventoryProduct product = new InventoryProduct(productWebElem);
            this.products.add(product);
        }
    }

    /**
     * Click 'ADD TO CART' button for this product (by Index)
     * <br/> And return an instance of the java object of type {@link InventoryProduct}
     *
     * @param productIndex index of the product to be added to cart
     * @return object representing the added product (title, description, price)
     */
    public InventoryProduct addProductToCart(int productIndex) {
        this.init();
        logger.info("Adding product {} to cart", productIndex + 1);
        InventoryProduct itemToAdd = Misc.getElementSafe(products, productIndex);

        if (itemToAdd != null) {
            itemToAdd.clickAddToCart();
        } else {
            Assertions.fail("Item not found to click it's 'ADD TO CART' button!");
        }

        return itemToAdd;
    }

    /**
     * Assess if browser currently open page is this one or not
     *
     * @return true if this is the opened page, false otherwise
     */
    public boolean isAt() {
        init();
        logger.info("Checking if we are currently on the InventoryPage");
        return this.PAGE_URL.equalsIgnoreCase(getUrl())
                && this.getTitle().equalsIgnoreCase(InventoryPage.PAGE_TITLE)
                && this.productsContainer != null
                && !this.inventoryItems.isEmpty()
                && this.sortingSelectElement != null;
    }

    public List<InventoryProduct> getProducts() {
        init();
        return this.products;
    }
}
