package com.systelab.seed.patient;

import com.systelab.seed.RESTResourceTest;
import com.systelab.seed.model.Address;
import com.systelab.seed.model.Patient;
import com.systelab.seed.model.PatientsPage;
import com.systelab.seed.utils.FakeNameGenerator;
import com.systelab.seed.utils.TestUtil;
import io.qameta.allure.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static io.restassured.RestAssured.given;
import static java.util.stream.Collectors.joining;

@TmsLink("TC0001_PatientManagement_IntegrationTest")
@Feature("Patient Test Suite.\n\nGoal:\nThe goal of this TC is to verify that the Patient management actions (CRUD) behave as expected according the specifications and the input values.\n\nEnvironment:\n...\nPreconditions:\nN/A.")
public class PatientResourceTest extends RESTResourceTest {

    private Patient getPatientData() {
        Patient patient = new Patient();
        patient.name="Ralph"; // Not NULL
        patient.surname="Burrows"; // Not NULL
        patient.email="rburrows@gmail.com"; // Format validated
        patient.medicalNumber="123456";
        patient.dob=LocalDate.of(1948, 8, 11);
        Address address = new Address();
        address.street="E-Street, 90";
        address.city="Barcelona";
        address.zip="08021";
        patient.address=address;
        return patient;
    }

    private Patient getPatientData(String name, String surname, String email) {
        Patient patient = getPatientData();
        patient.name=name;
        patient.surname=surname;
        patient.email=email;
        return patient;
    }

    private Patient getPatientData(String name, String surname, String email, String medicalNumber) {
        Patient patient = getPatientData();
        patient.name=name;
        patient.surname=surname;
        patient.email=email;
        patient.medicalNumber=medicalNumber;
        return patient;
    }

    private Patient getPatientData(String name, String surname, String email, String medicalNumber, LocalDate dob, String street, String city, String zip) {
        Patient patient = getPatientData(name, surname, email, medicalNumber);
        patient.dob=dob;
        patient.address.street=street;
        patient.address.city=city;
        patient.address.zip=zip;
        return patient;
    }

    @Description("Create a new patient with name, surname, email and medical number")
    @Test
    public void testCreatePatient() {
        String expectedName = "John";
        String expectedSurname = "Burrows";
        String expectedEmail = "jburrows@werfen.com";
        String expectedMedNumber = "112233";
        LocalDate expectedDob = LocalDate.of(1948, 8, 11);
        String expectedStreet = "E-Street, 90";
        String expectedCity = "Barcelona";
        String expectedZip = "08021";
        Patient patient = getPatientData(expectedName, expectedSurname, expectedEmail, expectedMedNumber);
        Patient patientCreated = given().body(patient)
                .when().post("/patients/patient")
                .then().assertThat().statusCode(200)
                .extract().as(Patient.class);
        TestUtil.checkObjectIsNotNull("Patient", patientCreated);
        TestUtil.checkField("Name", expectedName, patientCreated.name);
        TestUtil.checkField("Surname", expectedSurname, patientCreated.surname);
        TestUtil.checkField("Email", expectedEmail, patientCreated.email);
        TestUtil.checkField("Medical Number", expectedMedNumber, patientCreated.medicalNumber);
        TestUtil.checkField("DOB", expectedDob, patientCreated.dob);
        TestUtil.checkField("Street", expectedStreet, patientCreated.address.street);
        TestUtil.checkField("City", expectedCity, patientCreated.address.city);
        TestUtil.checkField("Zip", expectedZip, patientCreated.address.zip);
    }

    @Description("Create a new patient with name, surname, email, medical number, dob, and complete address")
    @Test
    public void testCreatePatientWithAllInfo() {
        String expectedName = "Jane";
        String expectedSurname = "Senfield";
        String expectedEmail = "jsenfield@example.com";
        String expectedMedNumber = "123123";
        LocalDate expectedDob = LocalDate.of(1948, 8, 12);
        String expectedStreet = "5 Elm St.";
        String expectedCity = "Madrid";
        String expectedZip = "28084";
        Patient patient = getPatientData(expectedName, expectedSurname, expectedEmail, expectedMedNumber, expectedDob, expectedStreet, expectedCity, expectedZip);
        Patient patientCreated = given().body(patient)
                .when().post("/patients/patient")
                .then().assertThat().statusCode(200)
                .extract().as(Patient.class);
        TestUtil.checkObjectIsNotNull("Patient", patientCreated);
        TestUtil.checkField("Name", expectedName, patientCreated.name);
        TestUtil.checkField("Surname", expectedSurname, patientCreated.surname);
        TestUtil.checkField("Email", expectedEmail, patientCreated.email);
        TestUtil.checkField("Medical Number", expectedMedNumber, patientCreated.medicalNumber);
        TestUtil.checkField("DoB", expectedDob, patientCreated.dob);
        TestUtil.checkField("Street", expectedStreet, patientCreated.address.street);
        TestUtil.checkField("City", expectedCity, patientCreated.address.city);
        TestUtil.checkField("Zip", expectedZip, patientCreated.address.zip);
    }

    private void testCreateInvalidPatient(Patient patient) {

        int statusCode = given().body(patient)
                .when().post("/patients/patient")
                .then()
                .extract().statusCode();
        TestUtil.checkField("Status Code", 400, statusCode);
    }

    @Description("Create a Patient with invalid data: mandatory fields empty (name, surname)")
    @Test
    public void testCreateInvalidPatientMandatoryFieldsEmpty() {
        testCreateInvalidPatient(getPatientData("", "", "", ""));
        testCreateInvalidPatient(getPatientData("", "Jameson", "jj@.", "333"));
        testCreateInvalidPatient(getPatientData("John", "", "jburrows@test,com", "222"));
    }

    @Description("Create a Patient with invalid data: email format")
    @Test
    public void testCreateInvalidPatientEmailFormat() {
        testCreateInvalidPatient(getPatientData("John", "Jameson", "@test.com", "222"));
        testCreateInvalidPatient(getPatientData("John", "Jameson", "user@", "222"));
        testCreateInvalidPatient(getPatientData("John", "Jameson", "@.com", "222"));
        testCreateInvalidPatient(getPatientData("John", "Jameson", "@.", "222"));
        testCreateInvalidPatient(getPatientData("John", "Jameson", ".", "222"));
        testCreateInvalidPatient(getPatientData("John", "Jameson", "@", "222"));
    }

    @Description("Create a Patient with invalid data: name, surname or medicalNumber field too long")
    @Test
    public void testCreateInvalidPatientTooLongText() {
        String tooLongString = "thisStringIsIntendedToCauseAnExceptionBecauseOfItsExcessiveLengthTheMostLongStringAllowedMustHaveLessThanTeoHundredAndFiftyFiveCharactersThisShouldBeVerifiedInEveryTextFieldToEnsureTheLimitationIsWorkingProperlyThisStringOnlyHasEnglishLettersButMoreScenarios";

        testCreateInvalidPatient(getPatientData(tooLongString, "Jameson", "jj@test.com", "123"));
        testCreateInvalidPatient(getPatientData("John", tooLongString, "jj@test.com", "123"));
        testCreateInvalidPatient(getPatientData("John", "Jameson", "jj@test.com", tooLongString));
        // use it in email field too?
    }

    @Attachment(value = "Patients Database")
    private String savePatientsDatabase(List<Patient> patients) {
        return patients.stream().map((patient) -> patient.surname + ", " + patient.name + "\t" + patient.email).collect(joining("\n"));
    }

    @Step("Action: Create {0} patients")
    private void createSomePatients(int numberOfPatients) {
        FakeNameGenerator aFakeNameGenerator = new FakeNameGenerator();
        for (int i = 0; i < numberOfPatients; i++) {
            Patient patient = getPatientData(aFakeNameGenerator.generateName(true), aFakeNameGenerator.generateName(true), aFakeNameGenerator.generateName(false) + "@werfen.com");
            Patient patientCreated = given().body(patient)
                    .when().post("/patients/patient")
                    .then().assertThat().statusCode(200)
                    .extract().as(Patient.class);

            Assertions.assertNotNull(patientCreated.id, "Patient not created");
        }
    }

    @Description("Get a list of patients")
    @Test
    public void testGetPatientList() {

        int numberOfPatients = 5;

        PatientsPage patientsBefore = given()
                .when().get("/patients")
                .then().assertThat().statusCode(200)
                .extract().as(PatientsPage.class);
        long initialSize = patientsBefore.totalElements;
        savePatientsDatabase(patientsBefore.content);

        createSomePatients(numberOfPatients);

        PatientsPage patientsAfter = given()
                .when().get("/patients")
                .then().assertThat().statusCode(200)
                .extract().as(PatientsPage.class);
        long finalSize = patientsAfter.totalElements;
        savePatientsDatabase(patientsAfter.content);

        TestUtil.checkANumber("List size", initialSize + numberOfPatients, finalSize);
    }

    @Description("Get a Patient by id")
    @Test
    public void testGetPatient() {

        Patient patient = getPatientData("John", "Burrows", "jburrows@werfen.com");
        Patient patientCreated = given().body(patient)
                .when().post("/patients/patient")
                .then().assertThat().statusCode(200)
                .extract().as(Patient.class);

        TestUtil.checkObjectIsNotNull("Patient ID " + patientCreated.id, patientCreated.id);
        Patient patientRetrieved = given()
                .when().get("/patients/" + patientCreated.id)
                .then().assertThat().statusCode(200)
                .extract().as(Patient.class);
        Assertions.assertNotNull(patientRetrieved, "Patient not retrieved");
        if (patientRetrieved != null) {
            TestUtil.checkField("Name", "John", patientRetrieved.name);
            TestUtil.checkField("Surname", "Burrows", patientRetrieved.surname);
            TestUtil.checkField("Email", "jburrows@werfen.com", patientRetrieved.email);

            TestUtil.checkField("Medical Number", "123456", patientRetrieved.medicalNumber);
            TestUtil.checkField("DOB", LocalDate.of(1948, 8, 11), patientRetrieved.dob);
        }
    }

    @Description("Get a patient with an non-existing id")
    @Test
    public void testGetUnexistingPatient() {
        int statusCode = given()
                .when().get("/patients/38400000-8cf0-11bd-b23e-10b96e4ef00d")
                .then()
                .extract().statusCode();
        TestUtil.checkField("Status Code after a GET", 404, statusCode);
    }

    @Description("Delete a patient by id")
    @Test
    public void testDeletePatient() {
        Patient patient = getPatientData("John", "Burrows", "jburrows@werfen.com");
        Patient patientCreated = given().body(patient)
                .when().post("/patients/patient")
                .then().assertThat().statusCode(200)
                .extract().as(Patient.class);

        Assertions.assertNotNull(patientCreated, "Patient not created");
        given()
                .when().delete("/patients/" + patientCreated.id)
                .then().assertThat().statusCode(200);

        int statusCode = given()
                .when().get("/patients/" + patientCreated.id)
                .then()
                .extract().statusCode();
        TestUtil.checkField("Status Code after a GET", 404, statusCode);
    }

    @Description("Delete non-existing Patient")
    @Test
    public void testDeleteUnexistingPatient() {
        int statusCode = given()
                .when().delete("/patients/38400000-8cf0-11bd-b23e-10b96e4ef00d")
                .then()
                .extract().statusCode();
        TestUtil.checkField("Status Code", 404, statusCode);
    }

    @Description("Update a patient by id")
    @Test
    public void testUpdatePatient() {
        Patient patient = getPatientData("John", "Burrows", "jburrows@werfen.com");
        Patient patientCreated = given().body(patient)
                .when().post("/patients/patient")
                .then().assertThat().statusCode(200)
                .extract().as(Patient.class);
        Assertions.assertNotNull(patientCreated, "Patient not created");
        patientCreated.email="new@emailchanged.com";
        Patient patientUpdated = given().body(patientCreated)
                .when().put("/patients/" + patientCreated.id)
                .then().assertThat().statusCode(200)
                .extract().as(Patient.class);
        Assertions.assertNotNull(patientUpdated, "Patient not updated");

        TestUtil.checkField("Email", patientCreated.email, patientUpdated.email);
        TestUtil.checkField("Name", patientCreated.name, patientUpdated.name);
        TestUtil.checkField("Surname", patientCreated.surname, patientUpdated.surname);
        TestUtil.checkField("Medical Number", patientCreated.medicalNumber, patientUpdated.medicalNumber);
        TestUtil.checkField("DoB", patientCreated.dob, patientUpdated.dob);
        TestUtil.checkField("Street", patientCreated.address.street, patientUpdated.address.street);
        TestUtil.checkField("City", patientCreated.address.city, patientUpdated.address.city);
        TestUtil.checkField("Zip", patientCreated.address.zip, patientUpdated.address.zip);
    }

    @Description("Update non-existent patient, that is Create new Patient")
    @Test
    public void testUpdateUnexistingPatient() {
        Patient patient = getPatientData("John", "Burrows", "jburrows@werfen.com");
        Patient patientCreated = given().body(patient)
                .when().put("/patients/38400000-8cf0-11bd-b23e-10b96e4ef00d")
                .then()
                .extract().as(Patient.class);
        Assertions.assertNotNull(patientCreated, "Patient not created");

        TestUtil.checkField("Email", patientCreated.email, patient.email);
        TestUtil.checkField("Name", patientCreated.name, patient.name);
        TestUtil.checkField("Surname", patientCreated.surname, patient.surname);
        TestUtil.checkField("Medical Number", patientCreated.medicalNumber, patient.medicalNumber);
        TestUtil.checkField("DoB", patientCreated.dob, patient.dob);
        TestUtil.checkField("Street", patientCreated.address.street, patient.address.street);
        TestUtil.checkField("City", patientCreated.address.city, patient.address.city);
        TestUtil.checkField("Zip", patientCreated.address.zip, patient.address.zip);
    }
}