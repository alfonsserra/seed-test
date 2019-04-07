package com.systelab.seed.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.sql.Timestamp;
import java.util.UUID;

@Data
@EqualsAndHashCode
@AllArgsConstructor

public class User {

    protected UUID id;
    protected Timestamp creationTime;
    protected Timestamp updateTime;
    private String surname;

    private String name;
    private String login;
    private String password;
    private UserRole role;

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
