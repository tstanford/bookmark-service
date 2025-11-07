package com.timstanford.bookmarkservice.api;

public interface UserWithStats {
    int getUserId();

    String getUsername();

    String getEmailAddress();

    long getBookmarkCount();
}