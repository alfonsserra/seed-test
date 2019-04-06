package com.systelab;

import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class PatientResourceTest extends RESTResourceTest {

    @Test
    public void testGetPatientList() {

        given()
                .when().get("/patients")
                .then().assertThat().statusCode(200);
    }

}