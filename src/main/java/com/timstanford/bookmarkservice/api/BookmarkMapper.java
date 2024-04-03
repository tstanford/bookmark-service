package com.timstanford.bookmarkservice.api;

import com.timstanford.bookmarkservice.data.Bookmark;
import com.timstanford.bookmarkservice.service.BookmarkResponse;
import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
public class BookmarkMapper {
    public Bookmark mapToBookmark(BookmarkRequest bookmarkRequest){
        Bookmark bookmark = new Bookmark();
        bookmark.setTitle(bookmarkRequest.getTitle());
        bookmark.setUrl(bookmarkRequest.getUrl());
        if (bookmarkRequest.getFavicon() != null) {
            bookmark.setFavicon(Base64.getDecoder().decode(bookmarkRequest.getFavicon()));
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
}
