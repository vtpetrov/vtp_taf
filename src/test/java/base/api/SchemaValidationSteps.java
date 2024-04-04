package base.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jackson.JsonLoader;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import org.junit.jupiter.api.Assertions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class SchemaValidationSteps extends ApiBaseTestStep {

    private static final Logger logger = LoggerFactory.getLogger(SchemaValidationSteps.class.getSimpleName());

    /**
     * Validate API response body structure agains JSON schema.
     * @param responseBody the response body to be validated against the schema
     * @param schemaPathAndName path to the schema together with file name. <b>Must start with '/' !</b>
     */
    public static void validateResponseAgainstJsonSchema(String responseBody, String schemaPathAndName) {
        logger.info("Validating JSON schema for api response....");
        JsonNode schemaNode, dataNode;
        try {
            schemaNode = JsonLoader.fromResource(schemaPathAndName);
            dataNode = JsonLoader.fromString(responseBody);
        } catch (IOException e) {
            logger.error("Loading JsonNode from resource failed: {} \n{}", schemaPathAndName, e.getMessage());
            throw new RuntimeException(e);
        }

        JsonSchemaFactory factory = JsonSchemaFactory.byDefault();

        ProcessingReport report;
        try {
            JsonSchema schema = factory.getJsonSchema(schemaNode);
            report = schema.validate(dataNode);
        } catch (ProcessingException e) {
            logger.error("Loading JsonNode from resource failed: {}", e.getMessage());
            throw new RuntimeException(e);
        }

        if (!report.isSuccess()) {
            Assertions.fail("JSON schema validation failed:\n" + report);
        }

        logger.info("Schema validation [PASS]");
    }

}
