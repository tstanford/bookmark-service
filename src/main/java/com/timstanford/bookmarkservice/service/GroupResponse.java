package com.timstanford.bookmarkservice.service;

import java.util.ArrayList;
import java.util.List;

public class GroupResponse {
    private Integer id;
    private String name;
    private List<BookmarkResponse> bookmarks;

    private static GroupResponse defaultInstance;

    public static GroupResponse getDefault() {
        if(defaultInstance == null) {
            defaultInstance = new GroupResponse();
            defaultInstance.setBookmarks(new ArrayList<>());
            defaultInstance.setName("No Group");
        }
        return defaultInstance;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<BookmarkResponse> getBookmarks() {
        return bookmarks;
    }

    public void setBookmarks(List<BookmarkResponse> bookmarks) {
        this.bookmarks = bookmarks;
    }
}
