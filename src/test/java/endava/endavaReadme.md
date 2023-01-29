Both UI and API tasks are done.

Test scenarios are written as Junit 5 tests and can be grouped into sub-sets using Junit5 @Tag annotation.

### Existing tags:
- `endava` - ALL Endava task related tests
- `ui` - only UI tests
- `api` - only Rest API tests
<br/> See [README.md](..%2F..%2F..%2F..%2FREADME.md) on how to pass the values to the build via command line.
  (use `-Dinclude.tags=` build parameter /defaults to empty, which will run ALL tests/)

### Switching between different environments:
The ability to provide specific set of configuration per environment is done via passing the `env` build parameter.
Using `mvn test -Denv=xxxx`, where `xxxx` can be one of:
- `dev`
- `test`
- `stage`
<br/> Which will respectively load on of the three config files  in `configs/specific`:
- [endava-dev.properties](..%2F..%2Fresources%2Fconfigs%2Fspecific%2Fendava-dev.properties)
- [endava-test.properties](..%2F..%2Fresources%2Fconfigs%2Fspecific%2Fendava-test.properties)
- [endava-stage.properties](..%2F..%2Fresources%2Fconfigs%2Fspecific%2Fendava-stage.properties)
<br/> **P.S.** At the moment these files contain **dummy** values, they can be replaced if needed.
<br/> **The real values** for testing which currently work are located inside [endava.properties](..%2F..%2Fresources%2Fconfigs%2Fcommon%2Fendava.properties) file.


----

# Selenium UI Task

## Description

Use the [following](https://www.saucedemo.com/) public website for your automation task.

The following tools/libraries can be used:
* [Selenium](https://www.selenium.dev/)
* Language of your choice that supports Selenium

## Version 1
#### Scenario 1 [DONE]
Use the standard user and password (they are prone to change, think how to obtain them)
- Log in with the standard user
- Add the first and the last item in the cart, verify the correct items are added
- Remove the first item and add previous to the last item to the cart, verify the content again
- Go to checkout
- Finish the order
- Verify order is placed
- Verify cart is empty
- Logout from the system

#### Scenario 2 [DONE]

- Log in with the standard user
- Verify when for sorting it is selected "Price (high to low)"
- Then the items are sorted in the correct manner
- Logout from the system

## Version 2
Implement the tasks written in **Version 1** and do the following as well
- Add an ability to filter tests for the test execution [DONE, via tags]
- Add custom HTML report for the test execution [TODO]
- Tests will be executed on multiple environments (dev, testing, staging, etc..), add necessary configurations. [DONE]
- :heavy_check_mark: Chrome and Firefox should be supported browsers [DONE]

## As a bonus, per your choice
- Support different browser resolutions [TODO]

----

# REST API Task

## Description

Use the [following](https://reqres.in/) public REST API to complete scenario steps.

The following tools/libraries **ARE** used:
* Pure code implementation for testing REST services (REST-Assured for Java)

## Scenario [ALL DONE]:
1. List available users
    - GET */api/users?page=1*
    - Execute one or many JSON Response Assertions
    - Extract single user details (Id, Email)
    - (Optional) Extract all users, sort them by First Name alphabetically. Print sorted collection.
2. Get extracted user details
    - GET */api/users/{USER_ID}*
    - Execute one or many JSON Response Assertions
3. Try to get details of user that doesn't exist
    - GET */api/users/{USER_ID}*
    - Execute one or many Assertions
4. Create UNIQUE new user
    - POST */api/users*
    - Execute one or many JSON Response Assertions
5. Delete newly created user
    - DELETE */api/users/{USER_ID}*
    - Execute one or many Assertions
6. Parameterize base URL