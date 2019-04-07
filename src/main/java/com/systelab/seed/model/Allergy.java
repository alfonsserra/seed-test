package com.systelab.seed.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.UUID;


@Data
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Allergy implements Serializable {

    protected UUID id;
    protected Timestamp creationTime;
    protected Timestamp updateTime;
    public String name;
    public String signs;
    public String symptoms;

}