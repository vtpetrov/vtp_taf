package endava;

import base.BaseTestStep;
import endava.pages.SwagLabsLoginPageObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static ui.Browser.goTo;
import static utils.Misc.sleepSeconds;

/**
 * Use the following public website for your automation task:
 * <br/> <a href="https://www.saucedemo.com/">https://www.saucedemo.com/</a>
 */
public class EndavaSeleniumTests extends BaseTestStep {

    private static final Logger logger = LoggerFactory.getLogger(EndavaSeleniumTests.class.getSimpleName());
    public static final String BASE_URL_PROP_KEY = "base_url";
    public static String BASE_URL;
    public static final String STD_USER_PROP_KEY = "saucedemo_std_user";
    public static String STD_USER;
    public static final String STD_PASS_PROP_KEY = "saucedemo_pass";
    public static String STD_PASS;

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
        SwagLabsLoginPageObject loginPage = new SwagLabsLoginPageObject(); // initialize login page via PageFactory
        Assertions.assertTrue(loginPage.isAt()); // assert if login page is opened

        loginPage.enterUsername(STD_USER);
        sleepSeconds(2);
        loginPage.enterPassword(STD_PASS);
        sleepSeconds(2);
        loginPage.clickLoginButton();
        sleepSeconds(5);



    }


}
