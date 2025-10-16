package com.timstanford.bookmarkservice.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.timstanford.bookmarkservice.service.BookmarkResponse;
import com.timstanford.bookmarkservice.service.BookmarkService;
import com.timstanford.bookmarkservice.service.GroupResponse;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
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
    public BookmarkResponse addNewBookmark(BookmarkRequest bookmarkRequest) {
        return bookmarkService.addBookmark(bookmarkRequest);
    }

    @Override
    public void addNewGroup(String title) {
        bookmarkService.addNewGroup(title);
    }

    @Override
    public void deleteBookmark(@PathVariable int id) {
        bookmarkService.deleteBookmark(id);
    }

    @Override
    public void deleteAll(){
        bookmarkService.deleteAll();
    }

    @Override
    public void moveBookmark(BookmarkMoveRequest request) {
        bookmarkService.moveBookmark(request.getBookmarkId(), request.getGroupName());
    }

    @Override
    public void importFromYaml(String yaml) throws JsonProcessingException {
        bookmarkService.importFromYaml(yaml);
    }

}
