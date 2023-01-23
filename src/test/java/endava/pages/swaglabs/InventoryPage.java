package endava.pages.swaglabs;

import endava.pages.models.InventoryProduct;
import lombok.Data;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static ui.Browser.drv;

/**
 * Page object to represent <a href="https://www.saucedemo.com/inventory.html">www.saucedemo.com/inventory.html</a> page
 */
@Data
public class InventoryPage extends HeaderPage {
    @FindBy(className = "product_sort_container") // DOESN'T WORK!!!
    private WebElement sortingSelectElement;
    @FindBy(id = "inventory_container")
    private WebElement productsContainer;

    List<InventoryProduct> products = new ArrayList<>();
    private final List<WebElement> inventoryItems;
    Select sortingMenu;
    public static final String PAGE_URL = "https://www.saucedemo.com/inventory.html";
    private static final Logger logger = LoggerFactory.getLogger(InventoryPage.class.getSimpleName());

    public InventoryPage() {
        super();
        logger.info("Initializing {} page", PAGE_URL);
        PageFactory.initElements(drv, this);

        sortingMenu = new Select(sortingSelectElement);
        inventoryItems = productsContainer.findElements(By.className("inventory_item"));
        for (WebElement productWebElem : inventoryItems) {
            InventoryProduct product = new InventoryProduct(productWebElem);
            products.add(product);
        }
    }
}
