package com.systelab.seed.user;

import com.systelab.seed.RESTResourceTest;
import com.systelab.seed.model.User;
import com.systelab.seed.model.UserRole;
import com.systelab.seed.model.UsersPage;
import com.systelab.seed.utils.TestUtil;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.TmsLink;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.logging.Logger;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.is;

@TmsLink("TC0002_LoginManagement_IntegrationTest")
@Feature("User Test Suite.\n\nGoal:\nThis test case is intended to verify the correct ....\n\nEnvironment:\n...\nPreconditions:\nN/A.")
public class UserResourceTest extends RESTResourceTest {
    private static final Logger logger = Logger.getLogger(UserResourceTest.class.getName());

    private User getUserData(String name, String surname, String login, String password) {
        User user = new User();
        user.name=name;
        user.surname=surname;
        user.login=login;
        user.password=password;
        user.role=UserRole.USER;
        return user;
    }

    @Description("Get the User list")
    @Test
    public void testGetUserList() {
        UsersPage users = given()
                .when().get("/users")
                .then().assertThat().statusCode(200)
                .extract().as(UsersPage.class);
        users.content.stream().forEach((user) -> logger.info(user.surname));
        TestUtil.checkObjectIsNotNull("Users", users);
    }

    @Description("Create a User with name, login and password")
    @Test
    public void testCreateUser() {
        User user = new User();
        user.login="agoncalves";
        user.password="agoncalves";
        user.name="Antonio";
        user.surname="Goncalves";
        user.role=UserRole.ADMIN;

        User userCreated = given().body(user)
                .when().post("/users/user")
                .then().assertThat().statusCode(anyOf(is(200), is(201)))
                .extract().as(User.class);
        TestUtil.checkObjectIsNotNull("User", userCreated);
        TestUtil.checkField("Name", "Antonio", userCreated.name);
        TestUtil.checkField("Surname", "Goncalves", userCreated.surname);
    }

    @Description("Delete a User by id")
    @Test
    public void testDeleteUser() {
        User user = new User(null, "TestUserName", "TestUserSurname", "testUser", "testUser");

        User userCreated = given().body(user)
            .when().post("/users/user")
            .then().assertThat().statusCode(anyOf(is(200), is(201)))
            .extract().as(User.class);
        Assertions.assertNotNull(userCreated);
        given()
            .when().delete("/users/" + userCreated.id)
            .then().assertThat().statusCode(anyOf(is(200), is(201), is(202), is(203), is(204)));

        int statusCode = given()
            .when().get("/users/" + userCreated.id)
            .then()
            .extract().statusCode();
        TestUtil.checkField("Status Code after a GET", 404, statusCode);
    }

    private void testCreateInvalidUser(User user) {
        int statusCode = given().body(user)
            .when().post("/users/user")
            .then()
            .extract().statusCode();
        TestUtil.checkField("Status Code", 400, statusCode);
    }

    @Description("Create a user with invalid data: empty mandatory fields (name, surname, login, password)")
    @Test
    public void testCreateInvalidUserEmptyMandatoryFields() {
        testCreateInvalidUser(getUserData("", "Jones", "jjones", "passJJones"));
        testCreateInvalidUser(getUserData("Jude", "", "jjones", "passJJones"));
        testCreateInvalidUser(getUserData("Jude", "Jones", "", "passJJones"));
        testCreateInvalidUser(getUserData("Jude", "Jones", "jjones", ""));
    }

    @Description("Create a user with invalid data: text fields too long (name, surname, login, password)")
    @Test
    public void testCreateInvalidUserTooLongText() {
        String tooLongString = "thisStringIsIntendedToCauseAnExceptionBecauseOfItsExcessiveLengthTheMostLongStringAllowedMustHaveLessThanTeoHundredAndFiftyFiveCharactersThisShouldBeVerifiedInEveryTextFieldToEnsureTheLimitationIsWorkingProperlyThisStringOnlyHasEnglishLettersButMoreScenarios";
        String tooLongLogin = "12345678901";

        testCreateInvalidUser(getUserData(tooLongString, "Jones", "jjones", "passJJones"));
        testCreateInvalidUser(getUserData("Jude", tooLongString, "jjones", "passJJones"));
        testCreateInvalidUser(getUserData("Jude", "Jones", tooLongLogin, "passJJones"));
        testCreateInvalidUser(getUserData("Jude", "Jones", "jjones", tooLongString));
    }

    @Description("Get User by id")
    @Test
    public void testGetUser() {
        User user = new User(null, "TestUserName2", "TestUserSurname2", "testUser2", "testUser2");
        User userCreated = given().body(user)
            .when().post("/users/user")
            .then().assertThat().statusCode(anyOf(is(200), is(201)))
            .extract().as(User.class);
        Assertions.assertNotNull(userCreated);
        User userRetrieved = given()
            .when().get("/users/" + userCreated.id)
            .then().assertThat().statusCode(anyOf(is(200), is(201)))
            .extract().as(User.class);
        TestUtil.checkObjectIsNotNull("User ID " + userRetrieved.id, userRetrieved.id);
        if (userRetrieved != null) {
            TestUtil.checkField("Name", userCreated.name, userRetrieved.name);
            TestUtil.checkField("Suname", userCreated.surname, userRetrieved.surname);
            TestUtil.checkField("Login", userCreated.login, userRetrieved.login);
            TestUtil.checkField("Password", userCreated.password, userRetrieved.password);
            TestUtil.checkField("Role", userCreated.role.toString(), userRetrieved.role.toString());
        }
    }

    @Description("Login - Successful")
    @Test
    public void testLoginOK() {
        String login = "Systelab";
        String password = "Systelab";
        String auth = given().contentType("application/x-www-form-urlencoded").formParam("login", login).formParam("password", password).
            when().post("/users/login").getHeader(AUTHORIZATION_HEADER);

        TestUtil.checkObjectIsNotNull("Auth. ", auth);
    }

    @Description("Login - Unsuccessful")
    @Test
    public void testLoginKO() {
        String login = "fakeUser";
        String password = "noPass";
        String auth = given().contentType("application/x-www-form-urlencoded").formParam("login", login).formParam("password", password).
            when().post("/users/login").getHeader(AUTHORIZATION_HEADER);
        TestUtil.checkObjectIsNull("Auth. ", auth);
    }
}
