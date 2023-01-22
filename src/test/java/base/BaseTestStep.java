package base;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ui.Browser;
import utils.PropertyUtils;

import java.util.Properties;

public class BaseTestStep {

    public static Properties testProps = new Properties();
    private static final Logger logger = LoggerFactory.getLogger(BaseTestStep.class.getSimpleName());

//    public BaseTestStep() {
//
//    }

    private static void initBaseTest() {
        logger.info("Initializing BaseTestStep. Calling default constructor to load property files");
        PropertyUtils.loadAllConfigs();
        PropertyUtils.copyPropsIntoSystemAndViceVersa();
        testProps = PropertyUtils.getAllProperties();

        Browser.init();
    }

    @BeforeAll
    public static void beforeAllMethod() {
        initBaseTest();
    }



    @AfterAll
    public static void afterAllMethod() {
        Browser.tearDown();
    }

}
