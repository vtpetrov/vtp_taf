package utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.Map.Entry;

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
     * @return the element found or null if elem not found
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
        final Map<Integer, ?> map = listToPrint.stream().collect(Collectors.toMap(n -> i.incrementAndGet(), element -> element));

        return prettyPrintMap(map);
    }

    public static String prettyPrintObject(final Object obj) {
        ObjectMapper oMapper = new ObjectMapper();
        var map = oMapper.convertValue(obj, Map.class);
        return prettyPrintMap(map);
    }

    /**
     * Encodes Map keys and values using UTF-8 encoding.
     *
     * @param mapToEncode the map to encode
     * @return The Map with encoded keys and values.
     */
    public static Map<String, Object> encodeMap(Map<String, Object> mapToEncode) {
        Map<String, Object> objectHashMap = new HashMap<>();

        mapToEncode.forEach((key, value) -> {
            try {
                objectHashMap.put(encodeUTF8(key), encodeUTF8(value.toString()));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        return objectHashMap;
    }

    /**
     * UTF8 encodes a string
     *
     * @param stringToEncode the string that will be encoded
     * @return the encoded string
     */
    public static String encodeUTF8(String stringToEncode) throws Exception {
        String encodedString;

        encodedString = URLEncoder.encode(stringToEncode, StandardCharsets.UTF_8);

        return encodedString;
    }

    /**
     * Decode a UTF8 encoded string
     *
     * @param stringToDecode the string to be decoded
     * @return the decoded string
     */
    public static String decodeUTF8(String stringToDecode) throws UnsupportedEncodingException {
        String decodedString;

        decodedString = URLDecoder.decode(stringToDecode, StandardCharsets.UTF_8);

        return decodedString;
    }

    /**
     * Encodes the values from the Map using UTF-8.
     *
     * @param map The Map that will be converted.
     * @return String representation of the map in format:
     * key1=value1&key2=value2&key3=value3
     */
    public static String mapToUrlEncodedString(Map<?, ?> map) {

        StringBuilder sb = new StringBuilder();
        String urlencodedPairs; // store all params in one string (appended with &); values are Encoded. keys are NOT for now.

        for (Entry currentEntry : map.entrySet()) {
            try {
                sb.append("&").append(currentEntry.getKey()).append("=").append(encodeUTF8(currentEntry.getValue().toString()));
            } catch (Exception e) {
                logger.error("Error occurred: {}", e.getMessage());
                throw new RuntimeException(e);
            }
        }

        urlencodedPairs = sb.substring(1); //remove the 1st character '&'

        return urlencodedPairs;
    }

    /**
     * Converts an URL encoded (UTF-8) key value pairs String into a Map with keys and values (Decoded).
     *
     * @param keyValuePairsAsEncodedString The string that has to be converted to map. E.g.
     *                                     key1=value1&key2=value2&email=mail%40example.com&key4=value+4
     * @return Map with decoded entries. E.g.:
     * <br/> key1 : value1
     * <br/> key2 : value2
     * <br/> email : mail@example.com
     * <br/> key4 : value 4
     */
    public static Map<String, String> urlEncodedStringToMap(String keyValuePairsAsEncodedString) {
        Map<String, String> keyValueMap = new HashMap<>();


        List<NameValuePair> listOfNameValuePairs = URLEncodedUtils.parse(keyValuePairsAsEncodedString, StandardCharsets.UTF_8);
        for (NameValuePair pair : listOfNameValuePairs) {
            keyValueMap.put(pair.getName(), pair.getValue());
        }


        return keyValueMap;
    }

    /**
     * Takes the Query param part from the URL, decodes it using UTF-8 and converts it into a map which contains the key/value paris.
     *
     * @param url String representation of valid URL. E.g. "http://domain.com/error?error=E02&detail=pr_email%3dblank%26pr_dob_yyyy%3dinvalid&key+3=value+3"
     * @return Map with decoded entries. E.g.:
     * <br/> error : E02
     * <br/> detail : pr_email=blank&pr_dob_yyyy=invalid
     * <br/> key 3 : value 3
     * @throws URISyntaxException exception
     */
    public static Map<String, String> urlQueryParamsToMap(String url) throws URISyntaxException {
        Map<String, String> keyValueMap = new HashMap<>();

        URI uri = new URI(url);

        List<NameValuePair> listOfNameValuePairs = URLEncodedUtils.parse(uri, StandardCharsets.UTF_8);

        for (NameValuePair pair : listOfNameValuePairs) {
            keyValueMap.put(pair.getName(), pair.getValue());
        }

        return keyValueMap;
    }

    /**
     * Randomizes all Alphanumeric characters inside the <i>inputString</i>, while keeping the places of all other characters.
     * Numbers gets substituted with random numeric values.
     * Characters get substituted with random alphabetic values.
     * <p/>
     * <br/> E.g.:
     * <table>
     * <col width="25%"/>
     * <col width="75%"/>
     * <head>
     * <tr><th></th><th>Result</th></tr>
     * <head>
     * <body>
     * <tr><td>originalString</td><td>21som...examp-l@t123.s34t#aaaaaa$bbbb space(tri_get</td></tr>
     * <tr><td>generatedString</td><td>79MUp...KvAwB-g@T485.b21D#APRzar$DDmK Diaap(vka_nDZ</td></tr>
     * </body>
     * </table>
     *
     * @param inputString The string that will be used as a base.
     * @return Returns the resulting string.
     */
    public static String retainSpecialCharsAndRandomizeAlphanumeric(String inputString) {
        String generatedString;

        int formatLength = inputString.length();
        String[] specialChars = new String[formatLength];
        int[] specialCharsPositions = new int[formatLength];
        String[] regularChars = new String[formatLength];
        int[] regularCharsPositions = new int[formatLength];

        Pattern pSpecialChar = Pattern.compile("[^A-Za-z0-9]");
        Matcher mSpecialChar = pSpecialChar.matcher(inputString);

        Pattern pRegular = Pattern.compile("[A-Za-z0-9]");
        Matcher mRegular = pRegular.matcher(inputString);

        for (int i = 0; i < formatLength; i++) {
            if (mSpecialChar.find()) {
                specialCharsPositions[i] = mSpecialChar.start();
                specialChars[i] = mSpecialChar.group();
            }
            if (mRegular.find()) {
                regularCharsPositions[i] = mRegular.start();
                regularChars[i] = mRegular.group();
            }
        }

        StringBuilder sB = new StringBuilder(formatLength);

        for (int k = 0; k < formatLength; k++) {
            for (int m = 0; m < formatLength; m++) {

                if (specialCharsPositions[m] == k && specialChars[m] != null) {
                    sB.append(specialChars[m]);
                } else if (regularCharsPositions[m] == k && regularChars[m] != null) {

                    boolean isAlpha = false;
                    try {
                        Integer.parseInt(regularChars[m]);
                    } catch (NumberFormatException e) {
                        isAlpha = true;
                    }

                    if (isAlpha) {
                        sB.append(RandomStringUtils.randomAlphabetic(1));
                    } else {
                        sB.append(RandomStringUtils.randomNumeric(1));
                    }
                }
            }
        }

        generatedString = sB.toString();

        return generatedString;
    }

}
