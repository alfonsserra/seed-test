package com.systelab.seed.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.UUID;


public class Patient implements Serializable {

    public UUID id;
    public Timestamp creationTime;
    public Timestamp updateTime;
    public String surname;
    public String name;
    public String medicalNumber;
    public String email;
    public LocalDate dob;
    public Address address;

}