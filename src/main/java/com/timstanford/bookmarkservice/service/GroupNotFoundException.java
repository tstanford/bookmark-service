package com.timstanford.bookmarkservice.service;

public class GroupNotFoundException extends RuntimeException {
    public GroupNotFoundException(int id) {
        super("Group not found by id "+id);
    }
}
