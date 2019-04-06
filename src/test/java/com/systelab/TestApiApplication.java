package com.systelab;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.testcontainers.containers.GenericContainer;

import static io.restassured.RestAssured.given;

public class TestApiApplication {

    protected static final String AUTHORIZATION_HEADER = "Authorization";

    private static RequestSpecBuilder requestSpecBuilder;

    @BeforeClass
    public static void init() {
        RestAssured.port = 8080;
        RestAssured.basePath = "/seed/v1/";
        RestAssured.baseURI = "http://localhost";
        RestAssured.defaultParser = Parser.JSON;

        if (requestSpecBuilder == null) {
            requestSpecBuilder = new RequestSpecBuilder().addHeader(AUTHORIZATION_HEADER, "Unknown for the moment")
                    .setAccept(ContentType.JSON).setContentType(ContentType.JSON);
            RestAssured.requestSpecification = requestSpecBuilder.build().log().all();
        }

        System.out.println(RestAssured.baseURI + ":" + RestAssured.port + RestAssured.basePath);
    }

    @Rule
    public GenericContainer seed = new GenericContainer<>("systelab/seed-springboot:latest")
            .withExposedPorts(8080);

    @Test
    public void testHealth() {
        given().contentType(ContentType.TEXT)
                .when().accept(ContentType.TEXT).get("/health")
                .then().assertThat().statusCode(200);
    }

}
