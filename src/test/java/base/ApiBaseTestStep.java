package base;

import api.rest.RestClient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApiBaseTestStep extends CommonBaseTestStep{

    private static final Logger logger = LoggerFactory.getLogger(ApiBaseTestStep.class.getSimpleName());
    public String apiBaseUrl;
    public ApiBaseTestStep() {
        logger.info("Invoking [ApiBaseTestStep] default constructor....");
        apiBaseUrl = testProps.getProperty("api_base_url", "DefaultBaseUri");
        RestClient.setBaseURI(apiBaseUrl);
    }

    @BeforeEach
    public void beforeEachApiMethod() {
        logger.info("Invoking [ApiBaseTestStep] @BeforeEach method ....");
        // Executes ones before each API @Test method:
        // TODO perhaps load base_url, etc.
    }

    @AfterEach
    public void afterEachApiMethod() {
        logger.info("Invoking [ApiBaseTestStep] @AfterEach method ....");
        // Executes ones after each API @Test method
        // TODO add something usable if needed
    }

}
