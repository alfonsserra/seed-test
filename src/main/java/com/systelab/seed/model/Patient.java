package com.systelab.seed.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.UUID;


@Data
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Patient  implements Serializable {

    protected UUID id;
    protected Timestamp creationTime;
    protected Timestamp updateTime;
    private String surname;
    private String name;
    private String medicalNumber;
    private String email;
    private LocalDate dob;
    private Address address;

}