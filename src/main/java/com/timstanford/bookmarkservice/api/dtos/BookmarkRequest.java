package com.timstanford.bookmarkservice.api.dtos;


import jakarta.validation.constraints.NotBlank;

public class BookmarkRequest {

    @NotBlank(message = "title cant be empty")
    private String title;

    private String url;

    private String favicon;
    private String faviconUrl;

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    private String groupName;

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

    public String getFavicon() {
        return favicon;
    }

    public void setFavicon(String favicon) {
        this.favicon = favicon;
    }

    public void setFaviconUrl(String faviconUrl) {
        this.faviconUrl = faviconUrl;
    }

    public String getFaviconUrl() {
        return faviconUrl;
    }
}
