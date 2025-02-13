package com.dyma.tennisApp.service;

public class PlayerAlreadyExistsException extends RuntimeException {
    public PlayerAlreadyExistsException(String lastName) {
        super("Player Entity with last name " + lastName + " already exists");
    }
}
