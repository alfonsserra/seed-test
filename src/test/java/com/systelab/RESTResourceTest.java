package com.systelab;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import org.testcontainers.containers.GenericContainer;

import static io.restassured.RestAssured.given;

public class RESTResourceTest {
    protected static final GenericContainer seed;

    protected static final String AUTHORIZATION_HEADER = "Authorization";

    private static RequestSpecBuilder requestSpecBuilder;

    private static String login(String username, String password) {
        return given().contentType("application/x-www-form-urlencoded").formParam("login", username).formParam("password", password).
                when().post("/users/login").getHeader(AUTHORIZATION_HEADER);
    }

    static {
        seed = new GenericContainer<>("systelab/seed-springboot:latest")
                .withExposedPorts(8080);
        seed.start();

        RestAssured.port = seed.getMappedPort(8080);
        RestAssured.basePath = "/seed/v1/";
        RestAssured.baseURI = "http://localhost";
        RestAssured.defaultParser = Parser.JSON;

        if (requestSpecBuilder == null) {
            String bearer = login("Systelab", "Systelab");
            requestSpecBuilder = new RequestSpecBuilder().addHeader(AUTHORIZATION_HEADER, bearer)
                    .setAccept(ContentType.JSON).setContentType(ContentType.JSON);
            RestAssured.requestSpecification = requestSpecBuilder.build();
        }

    }
}
