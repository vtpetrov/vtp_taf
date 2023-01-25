package base;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ui.Browser;
import utils.PropertyUtils;

import java.util.Properties;

public class BaseTestStep {

    public static Properties testProps = new Properties();
    private static final Logger logger = LoggerFactory.getLogger(BaseTestStep.class.getSimpleName());

    private TestInfo testInfo;

    private static void initBaseTest() {
        logger.info("Initializing BaseTestStep. Calling default constructor to load property files");
        PropertyUtils.loadAllConfigs();
        PropertyUtils.copyPropsIntoSystemAndViceVersa();
        testProps = PropertyUtils.getAllProperties();
    }

    @BeforeAll
    public static void beforeAllMethod() {
        // Executes 1 per each test Class
        initBaseTest();
    }

    @BeforeEach
    public void beforeEachMethod(TestInfo testInfo) {
        // Executes ones before each @Test method
        this.testInfo = testInfo;
        logger.info("-------------  STARTing test:  {}#{}   -----------------", this.testInfo.getTestClass().orElse(null), this.testInfo.getDisplayName());
        Browser.init();
    }

    @AfterEach
    public void afterEachMethod() {
        // Executes ones after each @Test method
        Browser.tearDown();
        logger.info("=============  ENDing test:  {}#{}   =================", testInfo.getTestClass().orElse(null), testInfo.getDisplayName());
    }

}
