package base;

import com.github.javafaker.Faker;
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
    public final Faker faker = new Faker();
    public static SharedState share;

    public CommonBaseTestStep() {
        logger.info("Invoking [CommonBaseTestStep] default constructor....");
    }

    @BeforeAll
    public static void initCommonBaseTest() {
        logger.info("Invoking [CommonBaseTestStep] @BeforeAll method....");
        logger.info("Loading ALL properties and putting them in both System and testProps");
        PropertyUtils.loadMavenPropertiesDefinedInPom();
//        Tests will be executed on multiple environments (dev, testing, staging, etc..), add necessary configurations.
        // either load specific or common configs, based on the 'env' property:
        String env = PropertyUtils.getProperty("env", "n/a");
        if("n/a".equals(env)){
            // load common properties:
            PropertyUtils.loadCommonConfigs();
        } else {
            PropertyUtils.loadSpecificConfig(env);
        }

        PropertyUtils.copyPropsIntoSystemAndViceVersa();

        testProps = PropertyUtils.getAllProperties();
        share = new SharedState();

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
