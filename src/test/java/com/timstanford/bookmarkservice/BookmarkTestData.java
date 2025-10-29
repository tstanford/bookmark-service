package com.timstanford.bookmarkservice;

import com.timstanford.bookmarkservice.api.dtos.BookmarkRequest;
import org.jetbrains.annotations.NotNull;

public class BookmarkTestData {
    @NotNull
    protected static BookmarkRequest createBookmarkTestData(String groupName, String url, String title) {
        BookmarkRequest bookmarkRequest = new BookmarkRequest();
        bookmarkRequest.setGroupName(groupName);
        bookmarkRequest.setUrl(url);
        bookmarkRequest.setTitle(title);
        return bookmarkRequest;
    }
}