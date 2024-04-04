package utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import static java.nio.file.Path.of;
import static utils.ResourceFileFinder.getBaseTestResourcePath;

public class PropertyUtils {
    private static final Logger logger = LoggerFactory.getLogger(PropertyUtils.class.getSimpleName());
    private static final Properties props = new Properties();
    private static final String COMMON_CONFIGS_PATH =
            of(getBaseTestResourcePath() ,"configs","common").toString();
    private static final String SPECIFIC_CONFIGS_PATH =
            of(getBaseTestResourcePath() ,"configs","specific").toString();

    public PropertyUtils() {
        logger.info("Invoking [PropertyUtils] default constructor....");
    }

    public static Properties getAllProperties() {
        return props;
    }

    /**
     * Load all *.properties files located in {@code src/test/resources/configs} .
     * <div/>
     * <br/> All loaded properties are then available through the global var {@link utils.PropertyUtils#props}
     */
    public static void loadCommonConfigs() {
        logger.info("Loading common configs {}...", COMMON_CONFIGS_PATH);
        loadConfigsFromPath(COMMON_CONFIGS_PATH, null);
    }

    /**
     * Load the file only if file name contains the value passed as the 'env' param.
     *
     * @param env the value which need to be in the file name to load it
     */
    public static void loadSpecificConfig(String env) {
        logger.info("Loading specific config [{}] '{}'...", env, SPECIFIC_CONFIGS_PATH);
        loadConfigsFromPath(SPECIFIC_CONFIGS_PATH, env);
    }

    /**
     * Load configs from specific target path whose filename contains passed param
     *
     * @param path              the target path to look at
     * @param fileNameToContain string that needs to be in the file name if we want to load it.
     *                          <br/> pass <b>null</b> if want to load all files located in targed dir.
     */
    private static void loadConfigsFromPath(String path, String fileNameToContain) {
        List<String> allConfigFilePaths = ResourceFileFinder.getListOfFilePaths(path, fileNameToContain);
        for (String configFilePath : allConfigFilePaths) {
            loadPropertyFile(configFilePath);
        }

        if (allConfigFilePaths.isEmpty()) {
            logger.warn("Configs for [{}] doesn't exist. Nothing is loaded!", fileNameToContain);
        } else {
            logger.info("Success!!");
        }
    }


    public static void copyPropsIntoSystemAndViceVersa() {
        Properties systemProps = System.getProperties();
        systemProps.putAll(props);
        System.setProperties(systemProps);
        props.putAll(systemProps);
    }

    public static void loadPropertyFile(final String propertyFilePath) {
        logger.info("Loading config file {}", propertyFilePath);
        try (FileInputStream fis = new FileInputStream(propertyFilePath)) {
            props.load(fis);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void loadMavenPropertiesDefinedInPom() {
        InputStream is = PropertyUtils.class.getClassLoader()
                .getResourceAsStream("properties-from-pom.properties");

        try {
            props.load(is);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Get a specific property from the currently loaded properties
     *
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
     *
     * @param propertyKey  the key of the property to search for
     * @param defaultValue the default value to be returned if property not found
     * @return the found prop value or default
     */
    public static String getProperty(final String propertyKey, final String defaultValue) {
        logger.info("Getting property with key '{}' OR default to '{}' if not found", propertyKey, defaultValue);
        final String prop = getProperty(propertyKey);
        return prop.isEmpty() ? defaultValue : prop;
    }

}
