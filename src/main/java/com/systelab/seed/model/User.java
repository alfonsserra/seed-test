package com.systelab.seed.model;

import java.sql.Timestamp;
import java.util.UUID;

public class User {

    public UUID id;
    public Timestamp creationTime;
    public Timestamp updateTime;
    public String surname;

    public String name;
    public String login;
    public String password;
    public UserRole role;

    public User() {
        this.role = UserRole.USER;
    }

    public User(UUID id, String name, String surname, String login, String password) {
        this.id = id;
        this.surname = surname;
        this.name = name;
        this.login = login;
        this.password = password;
        this.role = UserRole.USER;
    }
}
