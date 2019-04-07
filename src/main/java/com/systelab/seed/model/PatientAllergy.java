package com.systelab.seed.model;

import java.io.Serializable;
import java.time.LocalDate;

public class PatientAllergy implements Serializable {


    public Patient patient;
    public Allergy allergy;

    public LocalDate lastOccurrence;
    public LocalDate assertedDate;

    public String note;

    public PatientAllergy() {

    }

    public PatientAllergy(Patient patient, Allergy allergy) {
        this.patient = patient;
        this.allergy = allergy;
    }

}
