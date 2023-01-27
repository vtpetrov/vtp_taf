package endava.ui.pages.models;

import lombok.Data;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ui.SeleniumHelpers;

@Data
public class CartProduct {

    private static final Logger logger = LoggerFactory.getLogger(CartProduct.class.getSimpleName());

    @FindBy(className = "cart_quantity")
    WebElement itemQuantity;

    @FindBy(className = "inventory_item_name")
    WebElement itemName;

    @FindBy(className = "inventory_item_desc") // Common for both product types
    WebElement itemDescription;

    @FindBy(className = "inventory_item_price") // Common for both product types
    WebElement productPrice;

    @FindBy(className = "cart_button")
    WebElement itemRemoveBtn;

    int quantity;
    String name;
    String description;
    String price;


    public CartProduct() {
    }

    public CartProduct(WebElement productWebElem) {
//        logger.info("Constructing a CartProduct out of [{}]", productWebElem.getText());
        PageFactory.initElements(productWebElem, this);

        this.quantity = Integer.parseInt(this.itemQuantity.getText());
        this.name = this.itemName.getText();
        this.description = this.itemDescription.getText();
        this.price = this.productPrice.getText();
    }

    public void clickRemoveButton() {
        SeleniumHelpers.clickWebElemSafelyOrFail(SeleniumHelpers.waitUntilClickable(4, itemRemoveBtn));
    }

}
