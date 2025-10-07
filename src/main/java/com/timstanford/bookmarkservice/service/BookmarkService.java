package com.timstanford.bookmarkservice.service;

import com.timstanford.bookmarkservice.api.BookmarkRequest;

import java.util.List;

public interface BookmarkService {
    List<GroupResponse> getAllBookmarks();

    BookmarkResponse addBookmark(BookmarkRequest bookmarkRequest);

    void deleteBookmark(int id);

    void deleteAll();
}
