package com.systelab;

import io.restassured.http.ContentType;
import org.junit.Test;
import org.testcontainers.junit.jupiter.Testcontainers;

import static io.restassured.RestAssured.given;

@Testcontainers
public class HealthResourceTest extends RESTResourceTest {

    @Test
    public void testHealth() {
        System.out.println(seed.getContainerIpAddress());
        given().contentType(ContentType.TEXT)
                .when().accept(ContentType.TEXT).get("/health")
                .then().assertThat().statusCode(200);
    }

}
