package ui;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.PropertyUtils;


public class Browser {

    private static final Logger logger = LoggerFactory.getLogger(Browser.class.getSimpleName());
    public static final String BROWSER_NAME_PROP_KEY = "browser.name";
    public static WebDriver drv;

    public Browser() {
        logger.info("Invoking [Browser] default constructor....");
    }

    /**
     * Get System property 'browser.name' and use it to initialize the respective browser.
     * <br/> Should be one of ['chrome', 'firefox'] otherwise throw an error
     */
    public static void init() {
        String browserName = PropertyUtils.getProperty(BROWSER_NAME_PROP_KEY);
        logger.info("Initializing browser using property '{}' => '{}'", BROWSER_NAME_PROP_KEY, browserName);
        init(browserName);
    }

    /**
     * Initializes the designated browser.
     *
     * @param browserName The browser name to be instantiated. One of ['chrome', 'firefox']
     */
    public static void init(String browserName) {
        if (browserName == null) {
            throw new Error("Cannot initialize browser name 'null'");
        }

        logger.info("Initializing browser '{}'", browserName);

        switch (browserName.toLowerCase()) {
            case "chrome" -> {
                // supply browser options:
                ChromeOptions chromeOptions = new ChromeOptions();
                // TODO: see how we can set resolution/size
                drv = new ChromeDriver();

            }
            case "firefox" -> {
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                // TODO: see how we can set resolution/size
                drv = new FirefoxDriver();
            }

            default -> throw new Error("Unknown browser name '" + browserName + "'. Valid values are ['chrome', 'firefox']");
        }

    }

    public static void goTo(String url) {
        logger.info("Opening URL '{}'", url);
        Browser.drv.get(url);
    }

    public static String getUrl() {
//        logger.info("Getting current URL of the opened browser.");
        return Browser.drv.getCurrentUrl();
    }

    public static void tearDown() {
        if(drv != null) {
            logger.info("Closing browser at '{}'", getUrl());
            drv.quit();
        } else {
            logger.info("Browser already closed");
        }
    }
}
