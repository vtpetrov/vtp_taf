package endava.pages.models;

import lombok.Data;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Data
public class InventoryProduct {
    private static final Logger logger = LoggerFactory.getLogger(InventoryProduct.class.getSimpleName());

    @FindBy(className = "inventory_item_img")
    WebElement productImage;

//    @FindBy(xpath = "//div[@class='inventory_item_desc']/preceding-sibling::a")
    @FindBy(xpath = "//div[@class='inventory_item_label']/a")
    WebElement productName; // DOESN'T WORK for now. Looks like a bug. Tried with relative locator, but it fails with JS missing context.

    @FindBy(className = "inventory_item_desc")
    WebElement productDescription;

    @FindBy(className = "inventory_item_price")
    WebElement productPrice;

    @FindBy(className = "btn_inventory")
    WebElement productAddToCartBtn;

    String name;
    String description;
    String price;


    public InventoryProduct() {
    }

    public InventoryProduct(WebElement productWebElem) {
        logger.info("Constructing a InventoryProduct out of [{}]", productWebElem.getText());
        PageFactory.initElements(productWebElem, this);

        this.name = this.productName.getText();
        this.description = this.productDescription.getText();
        this.price = this.productPrice.getText();

//
//        By byNameRelLocator = RelativeLocator.with(By.tagName("a")).above(By.className("inventory_item_desc"));
//
//        WebElement nameAlt =
//                productWebElem.findElement(byNameRelLocator);
//        String altNameText = nameAlt.getText();

    }

    public void clickAddToCart() {
        this.productAddToCartBtn.click();
    }

}
