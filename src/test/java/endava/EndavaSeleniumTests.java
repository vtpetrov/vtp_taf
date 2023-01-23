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
    private static final String BASE_URL_PROP_KEY = "base_url";
    private static String BASE_URL;
    private static final String STD_USER_PROP_KEY = "saucedemo_std_user";
    private static String STD_USER;
    private static final String STD_PASS_PROP_KEY = "saucedemo_pass";
    private static String STD_PASS;

    public EndavaSeleniumTests() {
        super();
        BASE_URL = testProps.getProperty(BASE_URL_PROP_KEY);
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

        goTo(BASE_URL); // navigate to the login page
        logger.info("Log in with the standard user....");
        LoginPage loginPage = new LoginPage(); // initialize login page via PageFactory
        Assertions.assertTrue(loginPage.isAt()); // assert if login page is opened

        loginPage.enterUsername(STD_USER);
        loginPage.enterPassword(STD_PASS);
        loginPage.clickLoginButton();

        InventoryPage inventoryPage = new InventoryPage();

//- Add the first and the last item in the cart, verify the correct items are added
        logger.info("- Add the first and the last item in the cart, verify the correct items are added...");
        List<InventoryProduct> products = inventoryPage.getProducts();
        InventoryProduct firstProduct = products.stream().findFirst().orElse(new InventoryProduct());
        firstProduct.clickAddToCart();
        InventoryProduct lastProduct = products.get(products.size() - 1);
        lastProduct.clickAddToCart();

        // verify Cart icon top-right has number 2 on it:
        int actualShoppingCartBadgeValue = inventoryPage.getShoppingCartBadgeValue();
        Assertions.assertEquals(2, actualShoppingCartBadgeValue,"Actual badge count doesn't match expected!");

        // Verify cart content
        List<InventoryProduct> expectedCartContent = new ArrayList<>();
        expectedCartContent.add(firstProduct);
        expectedCartContent.add(lastProduct);

        // Navigate to the Cart page
        inventoryPage.clickShoppingCartIcon();
        CartPage cartPage = new CartPage();

        List<CartProduct> actualCartContent = new ArrayList<>(cartPage.getProducts());

        // Compare expected vs. actual items
        logger.info("Asserting cart content");
        for(int c = 0; c < 2; c++) {
            InventoryProduct expectedProduct = expectedCartContent.get(c);
            CartProduct actualProduct = actualCartContent.get(c);

            int expectedQty = 1;
            Assertions.assertEquals(expectedQty, actualProduct.getQuantity()
                    , "Actual quantity doesn't match Expected!");
            Assertions.assertEquals(expectedProduct.getDescription()
                    , actualProduct.getDescription(), "Actual description doesn't match Expected!");
            Assertions.assertEquals(expectedProduct.getPrice()
                    , actualProduct.getPrice(), "Actual price doesn't match Expected!");
        }

//- Remove the first item and add previous to the last item to the cart, verify the content again:


        Misc.sleepSeconds(2);
        logger.info("end");

    }


}
