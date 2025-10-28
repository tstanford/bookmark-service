package com.timstanford.bookmarkservice.api.exceptions;

public class GroupAlreadyExistsException extends RuntimeException {
    public GroupAlreadyExistsException() {
        super("Group with that name already exists");
    }
}
