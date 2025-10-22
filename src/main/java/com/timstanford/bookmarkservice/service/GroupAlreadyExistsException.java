package com.timstanford.bookmarkservice.service;

public class GroupAlreadyExistsException extends RuntimeException {
    public GroupAlreadyExistsException() {
        super("Group with that name already exists");
    }
}
