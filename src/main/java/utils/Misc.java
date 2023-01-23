package utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Misc {

    private static final Logger logger = LoggerFactory.getLogger(Misc.class.getSimpleName());

    public static void sleepSeconds(final int sleepTimeSeconds) {
        logger.info("Sleeping for '{}' seconds...", sleepTimeSeconds);
        try {
            Thread.sleep(sleepTimeSeconds * 1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns an element from a Collection at a specified position.
     * It returns null if the Collection is empty.
     *
     * @param actualCollection the Collection that is expected for an element if present
     * @param indexAt          the position at which is the searched element
     * @return the item found or null if elem not found
     */
    public static <T> T getElementSafe(final Collection<T> actualCollection, final int indexAt) {
        ArrayList<T> actualCollectionOfElements = new ArrayList<>(actualCollection);
        try {
            return actualCollectionOfElements.get(indexAt);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    /**
     * Formats the map for printing
     *
     * @param mapToPrint the map to format
     * @return a String containing a line for each key-value
     */
    public static String prettyPrintMap(final Map<?, ?> mapToPrint) {
        if (mapToPrint.isEmpty()) {
            return "none";
        }
        StringBuilder sb = new StringBuilder();
        mapToPrint.forEach((key, value) -> sb.append(String.format("\n %s: %s", key, value)));
        return sb.toString();
    }

    public static String prettyPrintList(final Collection<?> listToPrint) {
        if (listToPrint == null || listToPrint.isEmpty()) {
            return "[null or empty]";
        }
        // convert the list to Map, then use our prettyPrintMap() method:
        AtomicInteger i = new AtomicInteger(0);
        final Map<Integer, ?> map = listToPrint.stream()
                .collect(Collectors.toMap(n -> i.incrementAndGet(), element -> element));

        return prettyPrintMap(map);
    }

    public static String prettyPrintObject(final Object obj) {
        ObjectMapper oMapper = new ObjectMapper();
        var map = oMapper.convertValue(obj, Map.class);
        return prettyPrintMap(map);
    }
}
