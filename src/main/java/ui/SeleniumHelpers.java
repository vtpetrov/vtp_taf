package ui;

import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Optional;

import static ui.Browser.drv;

public class SeleniumHelpers {

    private static final Logger logger = LoggerFactory.getLogger(SeleniumHelpers.class.getSimpleName());

    /**
     * Helper method to extract the logic which checks if element is NOT NULL, is enabled and is visible:
     * Invoke Junit Assertion failure otherwise.
     * <pre>
     *     if({{element}}.isDisplayed() && {{element}}.isEnabled()) {
     *         {{element}}.click();
     *     } else {
     *         Assertions.fail("{{element}} not clickable!");
     *     }
     * </pre>
     * @param elemToClick Pass the web element we want to click
     */
    public static void clickWebElemSafelyOrFail(WebElement elemToClick) {
        if(elemToClick != null && elemToClick.isDisplayed() && elemToClick.isEnabled()) {
            elemToClick.click();
        } else {
            logger.info("Designated element cannot be clicked: {}"
                    , Optional.ofNullable(elemToClick).map(WebElement::getText).orElse(""));
            Assertions.fail("Element not clickable!");
        }
    }

    public static WebElement waitUntilClickable(int maxSecondsToWait, WebElement theElementToBeClickable) {
        return new WebDriverWait(drv, Duration.ofSeconds(maxSecondsToWait))
                .until(ExpectedConditions.elementToBeClickable(theElementToBeClickable));
    }
}
