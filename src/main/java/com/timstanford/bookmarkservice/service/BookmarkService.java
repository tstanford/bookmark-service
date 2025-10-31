package com.timstanford.bookmarkservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.timstanford.bookmarkservice.api.dtos.BookmarkEditRequest;
import com.timstanford.bookmarkservice.api.dtos.BookmarkRequest;
import com.timstanford.bookmarkservice.data.Group;

import java.util.List;

public interface BookmarkService {
    List<GroupResponse> getAllBookmarks();

    BookmarkResponse addBookmark(BookmarkRequest bookmarkRequest);

    void deleteBookmark(int id);

    void deleteAll();

    void importFromYaml(String yaml) throws JsonProcessingException;

    Group addNewGroup(String title);

    void moveBookmark(int bookmarkId, String groupName);

    void deleteGroup(int id);

    void renameGroup(int id, String newGroupName);

    String export() throws JsonProcessingException;

    void editBookmark(int id, BookmarkEditRequest request);

    void moveGroup(int sourceGroupId, int targetGroupId);
}
