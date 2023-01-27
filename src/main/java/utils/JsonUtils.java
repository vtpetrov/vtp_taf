package utils;

import api.rest.Response;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.fge.jackson.JsonLoader;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.jayway.jsonpath.JsonPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class JsonUtils {

    private static final Logger logger = LoggerFactory.getLogger(JsonUtils.class.getSimpleName());

    /**
     * Validate a JSON against a JSON schema
     *
     * @param jsonData   the JSON as string
     * @param jsonSchema the JSON schema as string
     * @return true if the validation passes, false otherwise
     * @throws IOException
     * @throws ProcessingException
     */
    public static boolean validateJsonSchema(String jsonData, String jsonSchema) throws IOException, ProcessingException {
        JsonNode schemaNode = JsonLoader.fromString(jsonSchema);
        JsonNode dataNode = JsonLoader.fromString(jsonData);

        JsonSchemaFactory factory = JsonSchemaFactory.byDefault();
        JsonSchema schema = factory.getJsonSchema(schemaNode);
        ProcessingReport report = schema.validate(dataNode);

        System.out.println(report.toString());
        return report.isSuccess();
    }

    /**
     * @param key  The key you want to find inside the JSON
     * @param json The JSON you want to search in
     * @return NULL if specified key is not found inside the JSON provided. Otherwise, the Value of the key provided.
     * @throws IOException the exception
     */
    public static Object getValueOfKeyFromJson(String key, String json) throws IOException {

        ObjectNode object = (ObjectNode) (JsonLoader.fromString(json));

        JsonNode valueOfKey = object.get(key);
        if (valueOfKey == null) {
            return null;
        } else {
            return valueOfKey.asText();
        }
    }

    /**
     * Removes the key from the JSON
     *
     * @param key  the key that will be removed
     * @param json the json as String
     * @return the JSON as String after the key was removed
     * @throws IOException
     */
    public static String removeKeyFromJson(String key, String json) throws IOException {
        ObjectNode object = (ObjectNode) (JsonLoader.fromString(json));
        object.remove(key);
        return object.toString();
    }

    /**
     * Loads a map from a json file
     * The json file should be located in main/resources
     * <p>
     * We are using multiple "profiles" in a json in order to easily manage different test data
     * used by the same scenario.The json format should be:
     * {
     * "profile_name1" : {some json},
     * "profile_name2" : {some other json}
     * }
     *
     * @param jsonName    the name of the json file. If the file is in a subfolder, include the folder in the name
     *                    Example: "/REST.GID.schemas/example_schema.json"
     * @param profileName the name of the json "profile"
     * @return a map with the key value pairs found in the json "profile"
     * @throws IOException
     */
    public static Map<String, String> loadMapFromResource(String jsonName, String profileName) throws IOException {
        Gson gson = new GsonBuilder().disableHtmlEscaping().serializeNulls().create();
        return gson.fromJson(getProfileFromJson(jsonName, profileName),
                new TypeToken<HashMap<String, String>>() {
                }.getType());
    }

    /**
     * Gets a "profile" from a json
     * <p>
     * We are using multiple "profiles" in a json in order to easily manage different test data
     * used by the same scenario.The json format should be:
     * {
     * "profile_name1" : {some json},
     * "profile_name2" : {some other json}
     * }
     *
     * @param jsonName    the name of the json file. If the file is in a subfolder, include the folder in the name
     *                    Example: "/test data/.json"
     * @param profileName the name of the json "profile"
     * @return the loaded JSON "profile" as string
     * @throws IOException
     */
    public static String getProfileFromJson(String jsonName, String profileName) {
        String json = loadJson("/" + jsonName);

        Gson gson = new GsonBuilder().disableHtmlEscaping().serializeNulls().create();
        Map<String, JsonElement> retMap = gson.fromJson(json, new TypeToken<HashMap<String, JsonElement>>() {
        }.getType());
        return gson.toJson(retMap.get(profileName));
    }

    /**
     * Loads a json from a file
     *
     * @param jsonName the name of the json file. If the file is in a subfolder, include the folder in the name
     *                 Example: "/test data/.json"
     * @return the loaded JSON as string
     * @throws IOException the exception to throw
     */
    public static String loadJson(String jsonName) {
        try {
            return JsonLoader.fromResource(jsonName).toString();
        } catch (IOException e) {
            logger.error("Exception occurred while loading JSON: {}", jsonName);
            throw new RuntimeException(e);
        }
    }

    /**
     * Maps the response body to a Java object
     * <p>
     * Example: Member currentMember = getResourceFromResponse(response, Member.class);
     *
     * @param response      the Response object
     * @param resourceClass the class of object
     * @param <T>           the type of the object being returned
     * @return an object of the specified type
     */
    public static <T> T getResourceFromResponse(final Response response, final Class<T> resourceClass) {
        return getResourceFromString(response.getBody(), resourceClass);
    }

    /**
     * Maps the source string (json) to a Java object
     * <p>
     * Example: Member currentMember = getResourceFromResponse(source, Member.class);
     *
     * @param source        Json as string representing the given resource
     * @param resourceClass the class of object
     * @param <T>           the type of the object being returned
     * @return an object of the specified type
     */
    public static <T> T getResourceFromString(final String source, final Class<T> resourceClass) {
        Gson gson = new GsonBuilder().disableHtmlEscaping().serializeNulls().create();
        return gson.fromJson(source, resourceClass);
    }

    public static <T> T getResourceFromString(final String source, final String jsonPath, final Class<T> resourceClass) {
        try {
            final Object resourceObject = JsonPath.read(source, jsonPath);
            final String jsonStringFromObject = getJsonStringFromObject(resourceObject);

            return getResourceFromString(jsonStringFromObject, resourceClass);
        } catch (Exception e) {
            logger.error("Exception occurred while trying to read resource from string: {},{}, {}"
                    , source, jsonPath, resourceClass.getSimpleName());
            throw e;
        }

    }

    public static String getJsonStringFromObject(final Object object) {
        return new Gson().toJson(object);
    }

    /**
     * Get the base uri from the "env" property
     *
     * @return the base uri
     */
    public static String getBaseUri() {
        return System.getProperty("env");
    }


    /**
     * Formats the json string for printing
     *
     * @param json the json to be formatted
     * @return a String containing the formatted json
     */
    public static String prettyPrintJson(String json) {
        Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().serializeNulls().setLenient().create();
        JsonElement je = JsonParser.parseString(json);

        return gson.toJson(je);
    }
}