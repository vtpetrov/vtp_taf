package endava;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Use the following public website for your automation task:
 * <br/> <a href="https://www.saucedemo.com/">https://www.saucedemo.com/</a>
 */
public class EndavaSeleniumTests {

    private static final Logger logger = LoggerFactory.getLogger(EndavaSeleniumTests.class.getSimpleName());
    private final Properties props = new Properties();

    EndavaSeleniumTests() {
        loadProperties();
    }

    private void loadProperties() {
        try {
            props.load(new FileInputStream("src/test/resources/endava.properties"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    public void testOne() {
        logger.info("System props: {}", props.stringPropertyNames());
        logger.info("selenium test 1");
        logger.info("System.getProperty(\"saucedemo_std_user\")= {}", props.getProperty("saucedemo_std_user"));
    }


}
