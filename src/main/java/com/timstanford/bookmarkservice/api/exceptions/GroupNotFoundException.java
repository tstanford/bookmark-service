package com.timstanford.bookmarkservice.api.exceptions;

public class GroupNotFoundException extends RuntimeException {
    public GroupNotFoundException(int id) {
        super("Group not found by id "+id);
    }
}
