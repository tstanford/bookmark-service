package com.timstanford.bookmarkservice.api;

import com.timstanford.bookmarkservice.api.dtos.BookmarkRequest;
import com.timstanford.bookmarkservice.data.Bookmark;
import com.timstanford.bookmarkservice.service.BookmarkResponse;
import com.timstanford.bookmarkservice.service.GroupResponse;
import com.timstanford.bookmarkservice.service.SimpleBookmark;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BookmarkMapper {
    public Bookmark mapToBookmark(BookmarkRequest bookmarkRequest){
        Bookmark bookmark = new Bookmark();
        bookmark.setTitle(bookmarkRequest.getTitle());
        bookmark.setUrl(bookmarkRequest.getUrl());
        if (bookmarkRequest.getFavicon() != null) {
            bookmark.setFavicon(bookmarkRequest.getFavicon());
        }
        return bookmark;
    }

    public BookmarkResponse mapToBookmarkResponse(Bookmark bookmark) {
        BookmarkResponse bookmarkResponse = new BookmarkResponse();
        bookmarkResponse.setId(bookmark.getId());
        bookmarkResponse.setTitle(bookmark.getTitle());
        bookmarkResponse.setUrl(bookmark.getUrl());
        bookmarkResponse.setFavicon(bookmark.getFavicon());
        return bookmarkResponse;
    }

    public List<SimpleBookmark> mapToListOfSimpleBookmarks(GroupResponse groupResponse) {
        return groupResponse.getBookmarks()
                .stream()
                .map(this::mapToSimpleBookmark).toList();
    }

    public SimpleBookmark mapToSimpleBookmark(BookmarkResponse bookmarkResponse) {
        var simpleBookmark = new SimpleBookmark();
        simpleBookmark.setUrl(bookmarkResponse.getUrl());
        simpleBookmark.setTitle(bookmarkResponse.getTitle());
        return simpleBookmark;
    }
}
