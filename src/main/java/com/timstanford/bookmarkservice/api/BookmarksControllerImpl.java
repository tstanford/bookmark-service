package com.timstanford.bookmarkservice.api;

import com.timstanford.bookmarkservice.service.BookmarkResponse;
import com.timstanford.bookmarkservice.service.BookmarkService;
import com.timstanford.bookmarkservice.service.GroupResponse;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bookmarks")
public class BookmarksControllerImpl implements BookmarksController {
    private final BookmarkService bookmarkService;

    public BookmarksControllerImpl(BookmarkService bookmarkService){
        this.bookmarkService = bookmarkService;
    }

    @Override
    public List<GroupResponse> getBookmarks(){
        return bookmarkService.getAllBookmarks();
    }

    @Override
    public BookmarkResponse addNewBookmark(@Validated @RequestBody BookmarkRequest bookmarkRequest) {
        return bookmarkService.addBookmark(bookmarkRequest);
    }

    @Override
    public void deleteBookmark(int id) {
        bookmarkService.deleteBookmark(id);
    }

}
