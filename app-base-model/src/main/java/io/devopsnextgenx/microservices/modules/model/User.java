package io.devopsnextgenx.microservices.modules.model;

import lombok.Data;

@Data
public class User {

    private String firstName;
    private String lastName;
    private String email;

    public User() {
    }

    public User(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return firstName + " " + lastName;
    }

    public boolean isEmpty() {
        return firstName == null && lastName == null;
    }
}
