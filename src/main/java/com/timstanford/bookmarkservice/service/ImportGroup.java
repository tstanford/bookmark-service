package com.timstanford.bookmarkservice.service;

import java.util.ArrayList;
import java.util.List;

public class ImportGroup {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<SimpleBookmark> getBookmarks() {
        return bookmarks;
    }

    public void setBookmarks(List<SimpleBookmark> bookmarks) {
        this.bookmarks = bookmarks;
    }

    private String name;
    private List<SimpleBookmark> bookmarks;

    public ImportGroup(){

    }

    public ImportGroup(String name) {
        this.name = name;
        this.bookmarks = new ArrayList<>();
    }
}
