package com.systelab.seed.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.UUID;


public class Allergy implements Serializable {

    public UUID id;
    public Timestamp creationTime;
    public Timestamp updateTime;
    public String name;
    public String signs;
    public String symptoms;

}