package com.systelab.seed;

import com.fasterxml.jackson.databind.DeserializationFeature;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.http.ContentType;
import io.restassured.mapper.factory.Jackson2ObjectMapperFactory;
import io.restassured.parsing.Parser;
import org.testcontainers.containers.GenericContainer;

import java.lang.reflect.Type;

import static io.restassured.RestAssured.given;
import static io.restassured.config.SSLConfig.sslConfig;

public class RESTResourceTest {
    protected static final GenericContainer seed;
    protected static final String AUTHORIZATION_HEADER = "Authorization";
    private static RequestSpecBuilder requestSpecBuilder;


    private static String login(String username, String password) {
        return given().contentType("application/x-www-form-urlencoded").formParam("login", username).formParam("password", password).
                when().post("/users/login").getHeader(AUTHORIZATION_HEADER);
    }

    static {
        seed = new GenericContainer<>("systelab/seed-springboot:latest").withExposedPorts(8080);
        seed.start();


        RestAssured.port = seed.getMappedPort(8080);
        RestAssured.basePath = "/seed/v1/";
        RestAssured.baseURI = "http://" + seed.getContainerIpAddress();
        RestAssured.defaultParser = Parser.JSON;
        RestAssured.config = RestAssured.config().sslConfig(sslConfig().relaxedHTTPSValidation())
                .and().objectMapperConfig((new ObjectMapperConfig().jackson2ObjectMapperFactory(
                        new Jackson2ObjectMapperFactory() {
                            @Override
                            public com.fasterxml.jackson.databind.ObjectMapper create(Type type, String s) {
                                com.fasterxml.jackson.databind.ObjectMapper objectMapper = new com.fasterxml.jackson.databind.ObjectMapper();
                                objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
                                return objectMapper;
                            }
                        })));

        if (requestSpecBuilder == null) {
            String bearer = login("Systelab", "Systelab");
            requestSpecBuilder = new RequestSpecBuilder().addHeader(AUTHORIZATION_HEADER, bearer)
                    .setAccept(ContentType.JSON).setContentType(ContentType.JSON);
            RestAssured.requestSpecification = requestSpecBuilder.build();
        }
    }
}
