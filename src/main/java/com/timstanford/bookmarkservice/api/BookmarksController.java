package com.timstanford.bookmarkservice.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.timstanford.bookmarkservice.service.BookmarkResponse;
import com.timstanford.bookmarkservice.service.GroupResponse;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
public interface BookmarksController {
    @GetMapping
    List<GroupResponse> getBookmarks();

    @PostMapping
    BookmarkResponse addNewBookmark(@Validated @RequestBody BookmarkRequest bookmarkRequest);

    @DeleteMapping
    void deleteBookmark(int id);

    @DeleteMapping("/all")
    void deleteAll();

    @PostMapping("/import")
    void importFromYaml(@RequestBody String yaml) throws JsonProcessingException;
}
