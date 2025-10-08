package com.timstanford.bookmarkservice.api;

import com.fasterxml.jackson.core.JsonProcessingException;
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
    private final BookmarkMapper bookmarkMapper;

    public BookmarksControllerImpl(BookmarkService bookmarkService, BookmarkMapper bookmarkMapper){
        this.bookmarkService = bookmarkService;
        this.bookmarkMapper = bookmarkMapper;
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

    @Override
    public void deleteAll(){
        bookmarkService.deleteAll();
    }

    @Override
    public void importFromYaml(@RequestBody String yaml) throws JsonProcessingException {
        bookmarkService.importFromYaml(yaml);
    }

}
