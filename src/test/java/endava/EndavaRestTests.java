package endava;

import api.rest.Request;
import api.rest.Response;
import api.rest.RestClient;
import base.ApiBaseTestStep;
import endava.api.models.User;
import endava.api.models.UserSingle;
import endava.api.models.UsersList;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.JsonUtils;
import utils.Misc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Task description -> <a href="https://github.com/Endava-Sofia/endava-testing-challenge/blob/master/REST_API_Task.md">link</a>
 * <br/> Use <a href="https://reqres.in/">the following</a> public REST API to complete scenario steps.
 * <pre>
 *  1.List available users
 *      GET /api/users?page=1
 *      Execute one or many JSON Response Assertions
 *      Extract single user details (Id, Email)
 *      (Optional) Extract all users, sort them by First Name alphabetically. Print sorted collection.
 *  2.Get extracted user details
 *      GET /api/users/{USER_ID}
 *      Execute one or many JSON Response Assertions
 *  3.Try to get details of user that doesn't exist
 *      GET /api/users/{USER_ID}
 *      Execute one or many Assertions
 *  4.Create UNIQUE new user
 *      POST /api/users
 *      Execute one or many JSON Response Assertions
 *  5.Delete newly created user
 *      DELETE /api/users/{USER_ID}
 *      Execute one or many Assertions
 *  6.Parameterize base URL
 * </pre>
 */
public class EndavaRestTests extends ApiBaseTestStep {

    private static final Logger logger = LoggerFactory.getLogger(EndavaRestTests.class.getSimpleName());
    public static final String CONTENT_TYPE_JSON = "application/json";
    private User extractedUser;
    private Request currentRequest;
    private UsersList grossExpectedUsersPage;

    public EndavaRestTests() {
        logger.info("Invoking [EndavaRestTests] default constructor....");
    }

    @Test
    @Tags({@Tag("endava"), @Tag("api")})
    public void apiScenarioOne() {
        String scenarioDescr = """
                Scenario 1
                 *  1.List available users
                 *      GET /api/users?page=1
                 *      Execute one or many JSON Response Assertions
                 *      Extract single user details (Id, Email)
                 *      (Optional) Extract all users, sort them by First Name alphabetically. Print sorted collection.
                """;
        logger.info("Scenario 1, description: \n{}", scenarioDescr);

        currentRequest = new Request();
        currentRequest.setContentType(CONTENT_TYPE_JSON);
//        *  1.List available users
        logger.info("1.List available users via GET /users?page=1");
//                *      GET /api/users?page=1
        currentRequest.setPath("/users");
        Map<String, String> sc1QueryParams = new HashMap<>();
        sc1QueryParams.put("page", "1");
        currentRequest.setQueryParams(sc1QueryParams);

        Response userPag1Response = RestClient.get(currentRequest);
//*     Execute one or many JSON Response Assertions
        UsersList actualUsersPage1 = JsonUtils.getResourceFromResponse(userPag1Response, UsersList.class);
        String expectedDataJson = JsonUtils.getProfileFromJson("endava/expectedResponses/expectedUsers.json", "users_page_1");
        grossExpectedUsersPage = JsonUtils.getResourceFromString(expectedDataJson, UsersList.class);

        logger.info("Asserting expected vs. actual users on page1:");
        logger.info("Expected: {}", grossExpectedUsersPage);
        logger.info("Actual  : {}", actualUsersPage1);
        assertionHelper.assertEqualsComplexObjects("List of users page 1 doesn't match expected",
                grossExpectedUsersPage, actualUsersPage1);

        //*     Extract single user details (Id, Email)
        logger.info("*     Extract single user details (Id, Email), for user 3, Emma Wong");
//      user 3, Emma Wong:
        List<User> extractedUsersPage1 = actualUsersPage1.getData();
        extractedUser = Misc.getElementSafe(extractedUsersPage1.stream().filter(u1 -> u1.getId() == 3).collect(Collectors.toList()),
                0);

        //(Optional) Extract all users, sort them by First Name alphabetically. Print sorted collection.
        logger.info("(Optional) Extract all users, sort them by First Name alphabetically. Print sorted collection.");
        extractedUsersPage1.sort(new User.UserComparatorByFirstName());
        logger.info("Sorted users by First name: {}", Misc.prettyPrintList(extractedUsersPage1));

    }

    @Test
    @Tags({@Tag("endava"), @Tag("api")})
    public void apiScenarioTwo() {
        String scenarioDescr = """
                 *  2.Get extracted user details
                 *      GET /api/users/{USER_ID}
                 *      Execute one or many JSON Response Assertions
                """;
        logger.info("Scenario 2, description: \n{}", scenarioDescr);

//        *  2.Get extracted user details
        logger.info("2.Get extracted user details via GET /api/users/{USER_ID}");
        currentRequest.getQueryParams().clear();
        currentRequest.setPath("/users/" + extractedUser.getId());
        Response user3Response = RestClient.get(currentRequest);
//                *      Execute one or many JSON Response Assertions
        logger.info("*      Execute one or many JSON Response Assertions");

        UserSingle actualSingleUser3 = JsonUtils.getResourceFromResponse(user3Response, UserSingle.class);
        User actualUser3 = actualSingleUser3.getData();
        User expectedUser3 = Misc.getElementSafe(grossExpectedUsersPage.getData().stream().filter(u -> u.getId() == 3).collect(Collectors.toList()),
                0);
        //Compare user 3 from expected response to user 3 from actual single response:
        assertionHelper.assertEqualsComplexObjects("Actual single User 3 data doesn't match the one from expected json"
        , expectedUser3
        , actualUser3);
    }


}
