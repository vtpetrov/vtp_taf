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

    public static final String fs = File.separator;
    private static final String workingDir = System.getProperty("user.dir");

    public static String findAResourceFilePathFromFileName(final String fileName) {

        List<String> listOfFilePaths = getListOfFilePaths(getBaseTestResourcePath(), fileName);

        if (listOfFilePaths.size() == 1) {
            logger.debug("File path found: " + listOfFilePaths.get(0));
            return listOfFilePaths.get(0);
        } else {
            logger.error("0 or more than 1 file found for filename '{}' in directory '{}'"
                    , fileName, getBaseTestResourcePath());
            return null;
        }
    }

    /**
     * Find files located in given target directory
     *
     * @param targetDirectory   The target directory to look at
     * @param fileNameToContain The text that need to be contained in the file path (including file name)
     *                          <br/> Pass <b>null</b> if we don't want to use it and want to load ALL files from target dir.
     * @return List of all file paths that match the search
     */
    public static List<String> getListOfFilePaths(String targetDirectory, String fileNameToContain) {

        List<String> listOfFilePaths = new ArrayList<>();

        try (Stream<Path> walk = Files.walk(Paths.get(targetDirectory))) {

            Stream<String> tempStream = walk.filter(Files::isRegularFile).map(Path::toString);
            Stream<String> finalStream;
            if (fileNameToContain != null) {
                finalStream = tempStream.filter(p -> p.contains(fileNameToContain));
            } else {
                finalStream = tempStream;
            }

            listOfFilePaths = finalStream.collect(Collectors.toList());

        } catch (IOException e) {
            e.printStackTrace();
        }

        return listOfFilePaths;
    }

    /**
     * @return The base path to <i>/src/test/resources</i> with platform specific file path separators
     */
    public static String getBaseTestResourcePath() {
        return Path.of(workingDir, "src", "test", "resources").toString();
//        String pathBuilder = workingDir + fs + "src" +
//                fs + "test" +
//                fs + "resources" + fs;
//        return pathBuilder;
    }

}
