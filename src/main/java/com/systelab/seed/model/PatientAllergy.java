package com.systelab.seed.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class PatientAllergy implements Serializable {


    private Patient patient;
    private Allergy allergy;

    private LocalDate lastOccurrence;
    private LocalDate assertedDate;

    private String note;

    public PatientAllergy(Patient patient, Allergy allergy) {
        this.patient = patient;
        this.allergy = allergy;
    }

}
