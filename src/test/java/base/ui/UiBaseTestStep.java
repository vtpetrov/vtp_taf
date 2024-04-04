package base.ui;

import base.CommonBaseTestStep;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ui.Browser;

public class UiBaseTestStep extends CommonBaseTestStep {
    private static final Logger logger = LoggerFactory.getLogger(UiBaseTestStep.class.getSimpleName());

    public UiBaseTestStep() {
        super();
        logger.info("Invoking [UiBaseTestStep] default constructor.... (super() as was already executed)");
    }

    @BeforeEach
    public void beforeEachUiMethod() {
        logger.info("Invoking [UiBaseTestStep] @BeforeEach method ....");
        // Executes ones before each UI @Test method:
        Browser.init();
    }

    @AfterEach
    public void afterEachUiMethod() {
        logger.info("Invoking [UiBaseTestStep] @AfterEach method ....");
        // Executes ones after each UI @Test method
        Browser.tearDown();
    }

}
