package base;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.AssertionHelper;
import utils.PropertyUtils;

import java.util.Properties;

public class CommonBaseTestStep {

    public static Properties testProps = new Properties();
    private static final Logger logger = LoggerFactory.getLogger(CommonBaseTestStep.class.getSimpleName());
    protected TestInfo testInfo;
    protected AssertionHelper assertionHelper = new AssertionHelper();

    public CommonBaseTestStep() {
        logger.info("Invoking [CommonBaseTestStep] default constructor....");
    }

    @BeforeAll
    public static void initCommonBaseTest() {
        logger.info("Invoking [CommonBaseTestStep] @BeforeAll method....");
        logger.info("Loading ALL properties and putting them in both System and testProps");
        PropertyUtils.loadAllConfigs();
        PropertyUtils.copyPropsIntoSystemAndViceVersa();
        testProps = PropertyUtils.getAllProperties();
    }

    @BeforeEach
    public void beforeEachMethod(TestInfo testInfo) {
        logger.info("Invoking [CommonBaseTestStep] @BeforeEach method ....");
        this.testInfo = testInfo;
        logger.info("-------------  STARTing test:  {}#{}   -----------------", this.testInfo.getTestClass().orElse(null)
                , this.testInfo.getDisplayName());
    }

    @AfterEach
    public void afterEachMethod() {
        logger.info("Invoking [CommonBaseTestStep] @AfterEach method ....");
        logger.info("=============  ENDing test:  {}#{}   =================", this.testInfo.getTestClass().orElse(null)
                , this.testInfo.getDisplayName());
    }
}
