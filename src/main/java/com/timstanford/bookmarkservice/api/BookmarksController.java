package com.timstanford.bookmarkservice.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.timstanford.bookmarkservice.api.dtos.BookmarkEditRequest;
import com.timstanford.bookmarkservice.api.dtos.BookmarkMoveRequest;
import com.timstanford.bookmarkservice.api.dtos.BookmarkRequest;
import com.timstanford.bookmarkservice.service.BookmarkResponse;
import com.timstanford.bookmarkservice.service.GroupResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
public interface BookmarksController {

    @GetMapping("/api/bookmarks")
    List<GroupResponse> getBookmarks();

    @PostMapping("/api/bookmarks")
    BookmarkResponse addNewBookmark(@Validated @RequestBody BookmarkRequest bookmarkRequest);

    @DeleteMapping("/api/bookmarks/{id}")
    void deleteBookmark(@PathVariable int id);

    @PostMapping("/api/group")
    void addNewGroup(@RequestBody String title);

    @DeleteMapping("/api/group/{id}")
    void deleteGroup(@PathVariable int id);

    @DeleteMapping("/api/all")
    void deleteAll();

    @PutMapping("/api/bookmarks")
    void editBookmark(@RequestBody BookmarkMoveRequest request);

    @PutMapping("/api/bookmark/{id}")
    void editBookmark(@PathVariable int id, @RequestBody BookmarkEditRequest request);

    @PutMapping("/api/group/{id}")
    void renameGroupName(@PathVariable int id, @RequestBody String newGroupName);

    @PostMapping("/api/import")
    void importFromYaml(@RequestBody String yaml) throws JsonProcessingException;

    @GetMapping(value = "/api/export")
    ResponseEntity<String> exportToYaml();

}
