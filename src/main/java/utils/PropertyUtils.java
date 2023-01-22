package utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

public class PropertyUtils {
    private static final Logger logger = LoggerFactory.getLogger(PropertyUtils.class.getSimpleName());
    private static final Properties props = new Properties();
    public static final String CONFIGS = "configs";
    private static final String configsPath = ResourceFileFinder.getBasePath() + CONFIGS;


    public static Properties getAllProperties() {
        return props;
    }

    /**
     * Load all *.properties files located in {@code src/test/resources/configs} .
     * <div/>
     * <br/> All loaded properties are then available through the global var {@link utils.PropertyUtils#props}
     */
    public static void loadAllConfigs() {
        List<String> allConfigFilePaths = ResourceFileFinder.getListOfFilePaths(configsPath);
        for (String configFilePath : allConfigFilePaths) {
            loadPropertyFile(configFilePath);
        }
    }

    public static void copyPropsIntoSystemAndViceVersa() {
        Properties systemProps = System.getProperties();
        systemProps.putAll(props);
        System.setProperties(systemProps);
        props.putAll(systemProps);
    }

    public static void loadPropertyFile(final String propertyFilePath) {
        try (FileInputStream fis = new FileInputStream(propertyFilePath)) {
            props.load(fis);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Get a specific property from the currently loaded properties
     * @param propertyKey the key of the property to look for
     * @return Property value if such key is found. Empty string ("") otherwise.
     */
    public static String getProperty(final String propertyKey) {
        logger.info("Getting property with key '{}'", propertyKey);
        String propertyValue = props.getProperty(propertyKey.trim());
        return propertyValue == null ? "" : propertyValue;
    }

    /**
     * Check for such a property and if not found return default value
     * @param propertyKey the key of the property to search for
     * @param defaultValue the default value to be returned if property not found
     * @return the found prop value or default
     */
    public static String getProperty(final String propertyKey, final String defaultValue) {
        logger.info("Getting property with key '{}' OR default to '{}' if not found", propertyKey, defaultValue);
        final String prop = getProperty(propertyKey);
        return prop.isEmpty() ? defaultValue : prop;
    }

}
