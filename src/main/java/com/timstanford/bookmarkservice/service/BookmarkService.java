package com.timstanford.bookmarkservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.timstanford.bookmarkservice.api.BookmarkRequest;
import com.timstanford.bookmarkservice.data.Group;

import java.util.List;

public interface BookmarkService {
    List<GroupResponse> getAllBookmarks();

    BookmarkResponse addBookmark(BookmarkRequest bookmarkRequest);

    void deleteBookmark(int id);

    void deleteAll();

    void importFromYaml(String yaml) throws JsonProcessingException;

    Group addNewGroup(String title);
}
