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
import java.util.Optional;

@RequestMapping("/api")
@CrossOrigin(origins = "*")
public interface BookmarksController {

    @GetMapping("/bookmarks")
    List<GroupResponse> getBookmarks();

    @PostMapping("/bookmarks")
    BookmarkResponse addNewBookmark(@Validated @RequestBody BookmarkRequest bookmarkRequest);

    @DeleteMapping("/bookmarks/{id}")
    void deleteBookmark(@PathVariable int id);

    @PutMapping("/bookmarks/icon/{id}")
    void updateIcon(@PathVariable int id, @RequestBody Optional<String> data);

    @PostMapping("/group")
    void addNewGroup(@RequestBody String title);

    @PutMapping("/group")
    void addNewGroup(@RequestBody GroupMoveRequest request);

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
