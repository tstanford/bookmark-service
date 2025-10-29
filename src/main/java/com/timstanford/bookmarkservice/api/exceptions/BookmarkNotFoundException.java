package com.timstanford.bookmarkservice.api.exceptions;

public class BookmarkNotFoundException extends RuntimeException {
    public BookmarkNotFoundException(int id) {
        super("Bookmark not found by id "+id);
    }
}
