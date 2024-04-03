package com.timstanford.bookmarkservice.data;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "bookmarks")
public class Bookmark {

    @Id
    @Column(name = "id", updatable = false)
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @NotNull
    private int id;

    @Column(name = "title")
    private String title;

    @Column(name = "url", length = 4096)
    private String url;

    @Column(name = "favicon")
    private byte[] favicon;

    @Column(name = "group_id")
    private Integer groupId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public byte[] getFavicon() {
        return favicon;
    }

    public void setFavicon(byte[] favicon) {
        this.favicon = favicon;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }
}
