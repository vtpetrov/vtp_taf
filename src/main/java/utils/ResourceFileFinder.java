package utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ResourceFileFinder {

    private static final Logger logger = LoggerFactory.getLogger(ResourceFileFinder.class.getSimpleName());

    protected static final String fs = File.separator;
    private static final String workingDir = System.getProperty("user.dir");
//    private static final String basePath = getBasePath();

    public static String findAResourceFilePathFromFileName(final String fileName) {

        List<String> listOfFilePaths = getListOfFilePaths(getBasePath());

        for (String filePath : listOfFilePaths) {
            if (filePath.endsWith(fs + fileName)) {
                logger.debug("File path found: " + filePath);
                return filePath;
            }
        }
        logger.error("No file found for filename: '" + fileName + "' in directory " + getBasePath());
        return null;
    }

    /**
     * Find all files located in given target directory
     *
     * @return List of all file paths
     */
    public static List<String> getListOfFilePaths(String targetDirectory) {

        List<String> listOfFilePaths = new ArrayList<>();

        try (Stream<Path> walk = Files.walk(Paths.get(targetDirectory))) {

            listOfFilePaths = walk.filter(Files::isRegularFile)
                    .map(Path::toString).collect(Collectors.toList());

        } catch (IOException e) {
            e.printStackTrace();
        }

        return listOfFilePaths;
    }

    /**
     * @return The base path to <i>/src/test/resources</i> with platform specific file path separators
     */
    public static String getBasePath() {
        String pathBuilder = workingDir + fs + "src" +
                fs + "test" +
                fs + "resources" + fs;
        return pathBuilder;
    }

}
