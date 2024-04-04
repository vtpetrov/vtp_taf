package endava;

import api.rest.Request;
import api.rest.Response;
import api.rest.RestClient;
import base.api.ApiBaseTestStep;
import base.api.SchemaValidationSteps;
import endava.api.models.UserCreate;
import endava.api.models.UserGet;
import endava.api.models.UserSingle;
import endava.api.models.UsersList;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.JsonUtils;
import utils.Misc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static io.netty.util.internal.StringUtil.EMPTY_STRING;

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
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class EndavaRestTests extends ApiBaseTestStep {

    private static final Logger logger = LoggerFactory.getLogger(EndavaRestTests.class.getSimpleName());
    public static final String CONTENT_TYPE_JSON = "application/json";

    private UserGet extractedUser;
    private final Request currentRequest = new Request();
    private final UsersList grossExpectedUsersPage;

    private static final int RESPONSE_STATUS_200 = 200;
    private static final int RESPONSE_STATUS_201 = 201;
    private static final int RESPONSE_STATUS_204 = 204;
    private static final int RESPONSE_STATUS_404 = 404;
    private static final String EMPTY_JSON_RESPONSE_BODY = "{}";

    public EndavaRestTests() {
        logger.info("Invoking [EndavaRestTests] default constructor....");
        currentRequest.setContentType(CONTENT_TYPE_JSON);
        String expectedDataJson = JsonUtils.getProfileFromJson("endava/expectedResponses/expectedUsers.json", "users_page_1");
        grossExpectedUsersPage = JsonUtils.getResourceFromString(expectedDataJson, UsersList.class);
    }

    @Test
    @Order(1)
    @Tags({@Tag("endava"), @Tag("api")})
    @DisplayName("Scenario 001")
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

//        *  1.List available users
        logger.info("1.List available users via GET /users?page=1");
//                *      GET /api/users?page=1
        currentRequest.setPath("/users");
        Map<String, String> sc1QueryParams = new HashMap<>();
        sc1QueryParams.put("page", "1");
        currentRequest.setQueryParams(sc1QueryParams);

        Response usersPage1Response = RestClient.get(currentRequest);
//*     Execute one or many JSON Response Assertions
        Assertions.assertEquals(RESPONSE_STATUS_200, usersPage1Response.getStatusCode()
                , "Actual response status code doesn't match expected!");
        UsersList actualUsersPage1 = JsonUtils.getResourceFromResponse(usersPage1Response, UsersList.class);

        logger.info("Asserting expected vs. actual users on page1:");
        logger.info("Expected: {}", grossExpectedUsersPage);
        logger.info("Actual  : {}", actualUsersPage1);
        assertionHelper.assertEqualsComplexObjects("List of users page 1 doesn't match expected",
                grossExpectedUsersPage, actualUsersPage1);

        //*     Extract single user details (Id, Email)
        logger.info("*     Extract single user details (Id, Email), for user 3, Emma Wong");
//      user 3, Emma Wong:
        List<UserGet> extractedUsersPage1 = actualUsersPage1.getData();
        extractedUser = Misc.getElementSafe(extractedUsersPage1.stream().filter(u1 -> u1.getId() == 3).collect(Collectors.toList()),
                0);

//(Optional) Extract all users, sort them by First Name alphabetically. Print sorted collection.
        logger.info("(Optional) Extract all users, sort them by First Name alphabetically. Print sorted collection.");
        extractedUsersPage1.sort(new UserGet.ComparatorByFirstName());
        logger.info("Sorted users by First name: {}", Misc.prettyPrintList(extractedUsersPage1));

    }

    @Test
    @Order(2)
    @Tags({@Tag("endava"), @Tag("api")})
    @DisplayName("Scenario 002")
    public void apiScenarioTwo() {
        String scenarioDescr = """
                 *  2.Get extracted user details
                 *      GET /api/users/{USER_ID}
                 *      Execute one or many JSON Response Assertions
                """;
        logger.info("Scenario 2, description: \n{}", scenarioDescr);

//        *  2.Get extracted user details
        logger.info("2.Get extracted user details via GET /api/users/{USER_ID}");
        currentRequest.clear();
        // get the id of the extracted user from sc.1 (or if it was not executed, set 3 directly)
        currentRequest.setPath("/users/" + Optional.ofNullable(extractedUser).map(UserGet::getId).orElse(3));
        Response user3Response = RestClient.get(currentRequest);
//                *      Execute one or many JSON Response Assertions
        logger.info("*      Execute one or many JSON Response Assertions");

        Assertions.assertEquals(RESPONSE_STATUS_200, user3Response.getStatusCode()
                , "Actual response status code doesn't match expected!");

        UserSingle actualSingleUser3 = JsonUtils.getResourceFromResponse(user3Response, UserSingle.class);
        UserGet actualUser3 = actualSingleUser3.getData();
        UserGet expectedUser3 = Optional.ofNullable(
                Misc.getElementSafe(grossExpectedUsersPage.getData().stream().filter(u -> u.getId() == 3).collect(Collectors.toList()),
                        0)).orElse(new UserGet());
        //Compare user 3 from expected response to user 3 from actual single response:
        assertionHelper.assertEqualsComplexObjects("Actual single User 3 data doesn't match the one from expected json"
                , expectedUser3
                , actualUser3);
    }

    @Test
    @Order(3)
    @Tags({@Tag("endava"), @Tag("api")})
    @DisplayName("Scenario 003")
    public void apiScenarioThree() {
        String scenarioDescr = """
                *  3.Try to get details of user that doesn't exist
                *      GET /api/users/{USER_ID}
                *      Execute one or many Assertions
                   """;
        logger.info("Scenario 3, description: \n{}", scenarioDescr);

        currentRequest.clear();
        currentRequest.setPath("/users/999");

        logger.info("GET /api/users/999");
        Response userNotFoundResponse = RestClient.get(currentRequest);

        logger.info("Execute one or many Assertions");
        Assertions.assertEquals(RESPONSE_STATUS_404, userNotFoundResponse.getStatusCode()
                , "Actual response status code doesn't match expected!");
        Assertions.assertEquals(EMPTY_JSON_RESPONSE_BODY, userNotFoundResponse.getBody(),
                "Actual response body doesn't match expected");
    }


    @Test
    @Order(4)
    @Tags({@Tag("endava"), @Tag("api")})
    @DisplayName("Scenario 004")
    public void apiScenarioFour() {
        String scenarioDescr = """
                 4.Create UNIQUE new user
                  *      POST /api/users
                  *      Execute one or many JSON Response Assertions
                """;
        logger.info("Scenario 4, description: \n{}", scenarioDescr);

        currentRequest.clear();
        currentRequest.setPath("/users");

        logger.info("Creating user via POST /api/users");
        String userName = faker.name().name();
        String userJob = faker.commerce().department();
        String postUserBody = UserCreate.generateUserCreateBody(userName, userJob);
        UserCreate expectedUser = JsonUtils.getResourceFromString(postUserBody, UserCreate.class);
        currentRequest.setBody(postUserBody);
        Response createUserResponse = RestClient.post(currentRequest);
        UserCreate actualUser = JsonUtils.getResourceFromResponse(createUserResponse, UserCreate.class);
        share.dataContainer.put("created_user_id", actualUser.getId());

// *      Execute one or many JSON Response Assertions
        logger.info("*      Execute one or many JSON Response Assertions");
        assertionHelper.assertEquals("Actual response status code doesn't match expected!"
                , RESPONSE_STATUS_201, createUserResponse.getStatusCode());
        // validate response schema:
        SchemaValidationSteps.validateResponseAgainstJsonSchema(createUserResponse.getBody()
                , "/endava/schemas/createUser_schema.json");

        String errMsg = "Actual value doesn't match expected!";
        logger.info("Expected user: {}", expectedUser);
        logger.info("Actual   user: {}", actualUser);
        assertionHelper.assertEquals(errMsg, userName, actualUser.getName());
        assertionHelper.assertEquals(errMsg, userJob, actualUser.getJob());

    }

    @Test
    @Order(5)
    @Tags({@Tag("endava"), @Tag("api")})
    @DisplayName("Scenario 005")
    public void apiScenarioFive() {
        share.dataContainer.put("5", "pet");
        String scenarioDescr = """
                 5. Delete newly created user
                 *   DELETE /api/users/{USER_ID}
                 *   Execute one or many Assertions
                """;
        logger.info("Scenario 5, description: \n{}", scenarioDescr);
        currentRequest.clear();
        logger.info("Deleting user via DELETE /api/users/{USER_ID}");
        String idToDelete = String.valueOf(Optional.ofNullable(share.dataContainer.get("created_user_id"))
                .orElse("null"));
        currentRequest.setPath("/users/" + idToDelete);
        Response deleteUserResponse = RestClient.delete(currentRequest);

        logger.info("Execute one or many Assertions");
        assertionHelper.assertEquals("Actual response status code doesn't match expected!"
                , RESPONSE_STATUS_204, deleteUserResponse.getStatusCode());
        assertionHelper.assertEquals("Actual response body doesn't match expected"
                , EMPTY_STRING, deleteUserResponse.getBody());
    }

}
