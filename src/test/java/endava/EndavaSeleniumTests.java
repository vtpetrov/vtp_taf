package endava;

import base.UiBaseTestStep;
import endava.ui.pages.models.CartProduct;
import endava.ui.pages.models.HamburgerMenu;
import endava.ui.pages.models.InventoryProduct;
import endava.ui.pages.swaglabs.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static ui.Browser.goTo;

/**
 * Task description -> <a href="https://github.com/Endava-Sofia/endava-testing-challenge/blob/master/Selenium_UI_Task.md">link</a>
 * <br/> Use the following public website for your automation task:
 * <br/> <a href="https://www.saucedemo.com/">https://www.saucedemo.com/</a>
 */
public class EndavaSeleniumTests extends UiBaseTestStep {

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
    @Tags({@Tag("endava"), @Tag("ui"), @Tag("s1")})
    public void uiScenarioOne() {
        String scenarioDescr = """
                Scenario 1
                    1. Log in with the standard user
                    2. Add the first and the last item in the cart, verify the correct items are added
                    3. Remove the first item and add previous to the last item to the cart, verify the content again
                    4. Go to checkout
                    5. Finish the order
                    6. Verify order is placed
                    7. Verify cart is empty
                    8. Logout from the system
                """;
        logger.info("Scenario 1, description: \n{}", scenarioDescr);
// 1. Log in with the standard user
        logger.info("1. Log in with the standard user .");
        loginWithStandardUser();

        InventoryPage inventoryPage = new InventoryPage();
        Assertions.assertTrue(inventoryPage.isAt()); // assert if inventory page is opened

//2. Add the first and the last item in the cart, verify the correct items are added
        logger.info("2. Add the first and the last item in the cart, verify the correct items are added ..");
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
        for (int c = 0; c < 2; c++) {
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

//3. Remove the first item and add previous to the last item to the cart, verify the content again
        logger.info("3. Remove the first item and add previous to the last item to the cart, verify the content again ...");
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
        for (int c = 0; c < 2; c++) {
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

// 4. Go to checkout
        logger.info("4. Go to checkout ....");
        cartPage.goToCheckout();
        CheckOutPage checkOutPage = new CheckOutPage();
        Assertions.assertTrue(checkOutPage.isAt()); // assert if checkout page is opened

// 5. Finish the order - proceed to order OVERVIEW page (assert info) and then FINISH
        logger.info("5. Finish the order .....");
        // enter data on checkout:
        checkOutPage.enterFirstName("Veselin");
        checkOutPage.enterLastName("Petrov");
        checkOutPage.enterZipPostCode("2050");
        checkOutPage.clickContinueBtn();
        // verify data on checkout overview page:
        CheckoutOverviewPage checkoutOverviewPage = new CheckoutOverviewPage();
        Assertions.assertTrue(checkoutOverviewPage.isAt()); // assert if checkout overview page is opened

        // click FINISH:
        checkoutOverviewPage.clickFinishBtn(); // navigate to checkout complete page


// 6. Verify order is placed
//        checkout complete page shows just greeting message! I'm going to assert it.
        logger.info("6. Verify order is placed ......");
        CheckoutCompletePage checkoutCompletePage = new CheckoutCompletePage();
        Assertions.assertTrue(checkoutCompletePage.isAt()); // assert if checkout complete page is opened
        checkoutCompletePage.assertCompleteHeader();
        checkoutCompletePage.assertCompleteText();


// 7. Verify cart is empty
        logger.info("7. Verify cart is empty .......");
        HeaderPage headerPage = new HeaderPage();
        headerPage.assertShoppingCartBadgeValue(0);
        headerPage.goToShoppingCart();
        cartPage.assertProductsCount(0);

// 8. Logout from the system
        logger.info("8. Logout from the system ........");
        headerPage.clickHamburgerMenu();
        HamburgerMenu hamburgerMenu = new HamburgerMenu();
        hamburgerMenu.clickLogoutLink();

        // assert we are at login page now:
        Assertions.assertTrue(new LoginPage().isAt()); // assert if login page is opened


        logger.info("Scenario 1 End!");
    }


    @Test
    @Tags({@Tag("endava"), @Tag("ui"), @Tag("s2")})
    public void uiScenarioTwo() {
        String scenarioDescr = """
                Scenario 2
                    1. Log in with the standard user
                    2. Verify when for sorting it is selected "Price (high to low)"
                    3. Then the items are sorted in the correct manner
                    4. Logout from the system
                """;

        logger.info("Scenario 2, description: \n{}", scenarioDescr);

// 1. Log in with the standard user
        logger.info("1. Log in with the standard user .");
        loginWithStandardUser();

        InventoryPage inventoryPage = new InventoryPage();
        Assertions.assertTrue(inventoryPage.isAt()); // assert if inventory page is opened

        List<Double> expectedPrices = new ArrayList<>(inventoryPage.getProducts().stream().map(InventoryProduct::getPrice)
                .map(price -> Double.valueOf(price.substring(1).replace(',', '.'))).toList());

        expectedPrices.sort(Collections.reverseOrder());

// 2. Verify when for sorting it is selected "Price (high to low)"
        logger.info("2. Verify when for sorting it is selected \"Price (high to low)\" ..");
        inventoryPage.getSortingMenu().selectByValue("hilo");
// 3. Then the items are sorted in the correct manner
        logger.info("3. Then the items are sorted in the correct manner ...");
        List<Double> actualPrices = new ArrayList<>(inventoryPage.getProducts().stream().map(InventoryProduct::getPrice)
                .map(price -> Double.valueOf(price.substring(1).replace(',', '.'))).toList());
        logger.info("Expected prices => {}", expectedPrices);
        logger.info("Actual   prices => {}", actualPrices);
        Assertions.assertEquals(expectedPrices, actualPrices, "Sorting by 'Price high to low' doesn't match expected");

// 4. Logout from the system
        logger.info("4. Logout from the system ....");
        inventoryPage.clickHamburgerMenu().clickLogoutLink();
        // assert we are at login page now:
        Assertions.assertTrue(new LoginPage().isAt()); // assert if login page is opened

        logger.info("Scenario 2 End!");
    }

    private static void loginWithStandardUser() {
        goTo(UI_BASE_URL); // navigate to the login page
        LoginPage loginPage = new LoginPage(); // initialize login page via PageFactory
        Assertions.assertTrue(loginPage.isAt()); // assert if login page is opened

        loginPage.enterUsername(STD_USER);
        loginPage.enterPassword(STD_PASS);
        loginPage.clickLoginButton();
    }
}
