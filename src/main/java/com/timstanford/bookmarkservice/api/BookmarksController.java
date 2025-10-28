package com.timstanford.bookmarkservice.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.timstanford.bookmarkservice.service.BookmarkResponse;
import com.timstanford.bookmarkservice.service.GroupResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@CrossOrigin(origins = "*")
public interface BookmarksController {

    @GetMapping("/bookmarks")
    List<GroupResponse> getBookmarks();

    @PostMapping("/bookmarks")
    BookmarkResponse addNewBookmark(@Validated @RequestBody BookmarkRequest bookmarkRequest);

    @DeleteMapping("/bookmarks/{id}")
    void deleteBookmark(@PathVariable int id);

    @PostMapping("/group")
    void addNewGroup(@RequestBody String title);

    @DeleteMapping("/group/{id}")
    void deleteGroup(@PathVariable int id);

    @DeleteMapping("/all")
    void deleteAll();

    @PutMapping("/bookmarks")
    void editBookmark(@RequestBody BookmarkMoveRequest request);

    @PutMapping("/bookmark/{id}")
    void editBookmark(@PathVariable int id, @RequestBody BookmarkEditRequest request);

    @PutMapping("/group/{id}")
    void renameGroupName(@PathVariable int id, @RequestBody String newGroupName);

    @PostMapping("/import")
    void importFromYaml(@RequestBody String yaml) throws JsonProcessingException;

    @GetMapping(value = "/export")
    ResponseEntity<String> exportToYaml();

}
