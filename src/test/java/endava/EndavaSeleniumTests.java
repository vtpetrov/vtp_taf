package endava;

import base.BaseTestStep;
import endava.pages.models.CartProduct;
import endava.pages.models.InventoryProduct;
import endava.pages.swaglabs.CartPage;
import endava.pages.swaglabs.InventoryPage;
import endava.pages.swaglabs.LoginPage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.Misc;

import java.util.ArrayList;
import java.util.List;

import static ui.Browser.goTo;

/**
 * Use the following public website for your automation task:
 * <br/> <a href="https://www.saucedemo.com/">https://www.saucedemo.com/</a>
 */
public class EndavaSeleniumTests extends BaseTestStep {

    private static final Logger logger = LoggerFactory.getLogger(EndavaSeleniumTests.class.getSimpleName());
    private static final String UI_BASE_URL_PROP_KEY = "ui_base_url";
    protected static String UI_BASE_URL;
    private static final String STD_USER_PROP_KEY = "saucedemo_std_user";
    protected static String STD_USER;
    private static final String STD_PASS_PROP_KEY = "saucedemo_pass";
    protected static String STD_PASS;

    public EndavaSeleniumTests() {
        UI_BASE_URL = testProps.getProperty(UI_BASE_URL_PROP_KEY);
        STD_USER = testProps.getProperty(STD_USER_PROP_KEY);
        STD_PASS = testProps.getProperty(STD_PASS_PROP_KEY);
    }

    @Test
    public void scenarioOne() {
        String scenarioDescr = """
                - Log in with the standard user
                - Add the first and the last item in the cart, verify the correct items are added
                - Remove the first item and add previous to the last item to the cart, verify the content again
                - Go to checkout
                - Finish the order
                - Verify order is placed
                - Verify cart is empty
                - Logout from the system
                """;
        logger.info("Executing Scenario 1: \n{}", scenarioDescr);

        goTo(UI_BASE_URL); // navigate to the login page
        logger.info("Log in with the standard user....");
        LoginPage loginPage = new LoginPage(); // initialize login page via PageFactory
        Assertions.assertTrue(loginPage.isAt()); // assert if login page is opened

        loginPage.enterUsername(STD_USER);
        loginPage.enterPassword(STD_PASS);
        loginPage.clickLoginButton();

        InventoryPage inventoryPage = new InventoryPage();
        Assertions.assertTrue(inventoryPage.isAt()); // assert if inventory page is opened

//- Add the first and the last item in the cart, verify the correct items are added
        logger.info("- Add the first and the last item in the cart, verify the correct items are added...");
        List<InventoryProduct> expectedCartContent = new ArrayList<>();
        InventoryProduct firstProduct = inventoryPage.addProductToCart(0);
        InventoryProduct lastProduct = inventoryPage.addProductToCart(inventoryPage.getProducts().size() - 1);
        expectedCartContent.add(firstProduct);
        expectedCartContent.add(lastProduct);

        // verify Cart icon top-right has number 2 on it:
        inventoryPage.assertShoppingCartBadgeValue(2);

        // Navigate to the Cart page
        inventoryPage.clickShoppingCartIcon();
        CartPage cartPage = new CartPage();
        Assertions.assertTrue(cartPage.isAt()); // assert if cart page is opened

        // verify cart content
        // Compare expected vs. actual items: quantity, description, and price
        logger.info("Asserting cart content 1");
        for(int c = 0; c < 2; c++) {
            InventoryProduct expectedProduct = expectedCartContent.get(c);
            CartProduct actualProduct = cartPage.getProduct(c);

            int expectedQty = 1;
            Assertions.assertEquals(expectedQty, actualProduct.getQuantity()
                    , "Actual quantity doesn't match Expected!");
            Assertions.assertEquals(expectedProduct.getDescription()
                    , actualProduct.getDescription(), "Actual description doesn't match Expected!");
            Assertions.assertEquals(expectedProduct.getPrice()
                    , actualProduct.getPrice(), "Actual price doesn't match Expected!");
        }

//- Remove the first item and add previous to the last item to the cart, verify the content again:
        cartPage.removeItemFromCart(0);
        expectedCartContent.remove(0);
        // assert shopping cart badge value is 1 now:
        cartPage.assertShoppingCartBadgeValue(1);
        cartPage.clickContinueShoppingBtn(); // go back to products inventory page
        // add the previous to last (5th, index 4) product to the cart, and put it in the Expected collection:
        expectedCartContent.add(inventoryPage.addProductToCart(4));
        // assert shopping cart badge value is 2 again:
        inventoryPage.assertShoppingCartBadgeValue(2);
        // navigate to the cart and verify that actual content match expected:
        inventoryPage.goToShoppingCart();
        // Compare expected vs. actual items: quantity, description, and price
        logger.info("Asserting cart content 2");
        for(int c = 0; c < 2; c++) {
            InventoryProduct expectedProduct = expectedCartContent.get(c);
            CartProduct actualProduct = cartPage.getProduct(c);

            int expectedQty = 1;
            Assertions.assertEquals(expectedQty, actualProduct.getQuantity()
                    , "Actual quantity doesn't match Expected!");
            Assertions.assertEquals(expectedProduct.getDescription()
                    , actualProduct.getDescription(), "Actual description doesn't match Expected!");
            Assertions.assertEquals(expectedProduct.getPrice()
                    , actualProduct.getPrice(), "Actual price doesn't match Expected!");
        }


        Misc.sleepSeconds(2);
        logger.info("end");

    }


}
