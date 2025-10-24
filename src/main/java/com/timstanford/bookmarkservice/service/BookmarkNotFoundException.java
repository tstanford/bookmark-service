package com.timstanford.bookmarkservice.service;

public class BookmarkNotFoundException extends RuntimeException {
    public BookmarkNotFoundException(int id) {
        super("Bookmark not found by id "+id);
    }
}
