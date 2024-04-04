package utils;

import org.junit.jupiter.api.Assertions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

public class AssertionHelper {

    private static final Logger logger = LoggerFactory.getLogger(AssertionHelper.class.getSimpleName());


    /**
     * Get the softAssert parameter from the Scenario params (if exists)
     *
     * @return the obtained value if found. Default value= FALSE.
     */
    public static Boolean isSoftAssert() {
        //TODO: implement global flag and per test config for soft assert. For now return false.
        Boolean scenarioSoftAssert = false;
        return scenarioSoftAssert;
    }


    /**
     * Helper method to assert any passed object is not null.
     * <br/> If it is null, junit Assert.fail(msg)
     *
     * @param resource the object to be checked
     * @param name     the name of the variable that is passed to be used for logging
     */
    public void assertResourceNotNull(Object resource, String name) {

        if (resource == null) {
            fail(String.format("Resource [%s] is null", name));
        }

    }


    /**
     * Soft assert equals. If values don't match, record an error entry in the global `errors` list.
     * <br/> This will be evaluated at the end of scenario execution (in the @After hook)
     * <br/> and scenario will be failed along with data written to Cucumber report
     *
     * @param errorMsg      the error message to print
     * @param expectedValue the expected value
     * @param actualValue   the actual value
     */
    public void softAssertEquals(final String errorMsg, final Object expectedValue, final Object actualValue) {
        try {
            Assertions.assertEquals(expectedValue, actualValue, errorMsg);
        } catch (AssertionError e) {
            logger.error(e.getMessage());
        }

    }

    /**
     * NEW implementation
     * SOFT or HARD assert as per the scenario parametrization
     *
     * @param errMessage    The error message that will be printed in case of assert failure
     * @param expectedValue the expected value
     * @param actualValue   the actual value
     */
    public void assertEquals(final String errMessage, final Object expectedValue, final Object actualValue) {
        try {
            Assertions.assertEquals(expectedValue, actualValue, errMessage);
        } catch (AssertionError e) {
            if (isSoftAssert()) {
                logger.error(e.getMessage());
            } else {
                throw e;
            }
        }
    }


    /**
     * SOFT or HARD assert as per the scenario parametrization
     *
     * @param errMessage  The error message that will be printed in case of assert failure (object is null)
     * @param actualValue the actual value
     */
    public void assertNotNull(final String errMessage, final Object actualValue) {

        try {
            assertThat(actualValue).as(errMessage).isNotNull();
        } catch (AssertionError e) {
            if (isSoftAssert()) {
                logger.error(e.getMessage());
            } else {
                throw e;
            }
        }
    }

    /**
     * SOFT or HARD assert as per the scenario parametrization
     *
     * @param errMessage  The error message that will be printed in case of assert failure (object is not null)
     * @param actualValue the actual value
     */
    public void assertNull(final String errMessage, final Object actualValue) {
        try {
            assertThat(actualValue).as(errMessage).isNull();
        } catch (AssertionError e) {
            if (isSoftAssert()) {
                logger.error(e.getMessage());
            } else {
                throw e;
            }
        }
    }

    /**
     * SOFT or HARD assert as per the scenario parametrization
     *
     * @param errMessage the error message to print
     * @param unexpected the value that is not expected
     * @param actual     the actual value to check against unexpected
     */
    public void assertNotEquals(final String errMessage, final Object unexpected, final Object actual) {
        try {
            Assertions.assertNotEquals(unexpected, actual, errMessage);
        } catch (AssertionError e) {
            if (isSoftAssert()) {
                logger.error(e.getMessage());
            } else {
                throw e;
            }
        }
    }

    /**
     * Asserts that the number of elements in the passed collection match expected.
     * If not, fails HARDLY or SOFTLY depending on scenario parametrization.
     *
     * @param errMsg            The error message to use with the assert in case of failure
     * @param collectionToCheck The collection to check upon
     * @param expectedCount     expected size of the collection
     */
    public void assertNumberOfElements(final String errMsg, final Iterable<?> collectionToCheck, final int expectedCount) {
        try {
            // Assertj
            assertThat(collectionToCheck).as(errMsg).hasSize(expectedCount);
        } catch (AssertionError error) {
            if (isSoftAssert()) {
                logger.error(error.getMessage());
            } else {
                throw error;
            }
        }
    }

    /**
     * Compare collections of base types (String, Integer, etc) IGNRORING order of elements.
     *
     * @param errMsg       The error message if assertion fails
     * @param expectedList The collection of expected elements
     * @param actualList   The collection of actual elements
     * @param <T>          the type of elements in the collection
     */
    public <T> void assertListOfSimpleObjectsEquals(final String errMsg, final Collection<T> expectedList, final Collection<T> actualList) {
        try {
            assertThat(actualList).as(errMsg)
                    .containsExactlyInAnyOrderElementsOf(expectedList);
        } catch (AssertionError e) {
            if (isSoftAssert()) {
                logger.error(e.getMessage());
            } else {
                throw e;
            }
        }
    }

    public <T> void assertListOfComplexObjectsEquals(final String errMsg, final Collection<T> expectedList, final Collection<T> actualList) {
        try {
            assertThat(actualList).as(errMsg).usingRecursiveFieldByFieldElementComparator()
                    .isEqualTo(expectedList);
        } catch (AssertionError e) {
            if (isSoftAssert()) {
                logger.error(e.getMessage());
            } else {
                throw e;
            }
        }
    }

    /**
     * SOFT or HARD assert of List being empty
     *
     * @param errMessage  The error message that will be printed in case of assert failure (object is empty)
     * @param actualValue the actual value
     */
    public void assertEmptyList(final String errMessage, final List actualValue) {
        try {
            assertThat(actualValue).as(errMessage).isEmpty();
        } catch (AssertionError e) {
            if (isSoftAssert()) {
                logger.error(e.getMessage());
            } else {
                throw e;
            }
        }

    }

    public <T> void assertEqualsComplexObjects(final String errMsg, final T expectedObject, final T actualObject) {
        assertEqualsComplexObjects(errMsg, expectedObject, actualObject, false);
    }

    /**
     * Assert complex objects field by field using recursive comparison. (AssertJ)
     *
     * @param errMsg              The error message to be thrown in case of failure
     * @param expectedObject      the expected object
     * @param actualObject        the actual object
     * @param ignoreExpectedNulls true if you want to exclude the NULL fields of the expected object from the comparison,
     *                            false otherwise.
     * @param <T>                 the type of the expected and actual objects (have to an objects of the same type)
     */
    public <T> void assertEqualsComplexObjects(final String errMsg, final T expectedObject, final T actualObject
            , final boolean ignoreExpectedNulls) {

        try {
            if (ignoreExpectedNulls) {
                assertThat(actualObject).as(errMsg)
                        .usingRecursiveComparison()
                        .ignoringExpectedNullFields()
                        .isEqualTo(expectedObject);
            } else {
                assertThat(actualObject).as(errMsg)
                        .usingRecursiveComparison()
                        .isEqualTo(expectedObject);
            }
        } catch (AssertionError e) {
            if (isSoftAssert()) {
                logger.error(e.getMessage());
            } else {
                throw e;
            }
        }
    }

    /**
     * To assert that if actual data contains expected data
     *
     * @param errMessage    The error message that will be printed in case of assert failure
     * @param expectedValue the expected value passed as boolean.
     */
    public void assertTrue(final String errMessage, final Boolean expectedValue) {
        try {
            Assertions.assertTrue(expectedValue, errMessage);
        } catch (AssertionError e) {
            if (isSoftAssert()) {
                logger.error(e.getMessage());
            } else {
                throw e;
            }
        }
    }

    public <T> void assertEqualsComplexObjectsExcludingFields(final String errMsg, final T expectedObject, final T actualObject, List<String> listOfFieldsToExclude) {
        try {
            List<String> listOfFieldsWithRegex = new ArrayList<>();
            for (int i = 0; i < listOfFieldsToExclude.size(); i++) {
                String field = listOfFieldsToExclude.get(i);
                listOfFieldsWithRegex.add("^(?i).*" + field + ".*?$");
            }
            String[] strArray = listOfFieldsWithRegex.toArray(new String[0]);
            assertThat(actualObject).as(errMsg)
                    .usingRecursiveComparison().ignoringFieldsMatchingRegexes(strArray)
                    .isEqualTo(expectedObject);
        } catch (AssertionError e) {
            if (isSoftAssert()) {
                logger.error(e.getMessage());
            } else {
                throw e;
            }
        }
    }

    /**
     * Assert all the objects in the expected list by comparing with all the objects in the actual list. (AssertJ)
     * All the objects in the expected list should match in the actual list
     *
     * @param errMsg       The error message to be thrown in case of failure
     * @param expectedList the expected list
     * @param actualList   the actual list
     * @param <T>          the type of the expected and actual objects (have to be objects of the same type)
     */
    public <T> void assertListOfSimpleObjectsContainsExpectedList(final String errMsg, final Collection<T> expectedList, final Collection<T> actualList) {
        try {
            assertThat(actualList).as(errMsg)
                    .containsAll(expectedList);
        } catch (AssertionError e) {
            if (isSoftAssert()) {
                logger.error(e.getMessage());
            } else {
                throw e;
            }
        }
    }
}


