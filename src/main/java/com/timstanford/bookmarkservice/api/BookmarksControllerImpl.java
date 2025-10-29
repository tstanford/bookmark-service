package com.timstanford.bookmarkservice.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.timstanford.bookmarkservice.api.dtos.BookmarkEditRequest;
import com.timstanford.bookmarkservice.api.dtos.BookmarkMoveRequest;
import com.timstanford.bookmarkservice.api.dtos.BookmarkRequest;
import com.timstanford.bookmarkservice.api.exceptions.FailedToExportException;
import com.timstanford.bookmarkservice.service.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@RestController
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
    public BookmarkResponse addNewBookmark(BookmarkRequest bookmarkRequest) {
        return bookmarkService.addBookmark(bookmarkRequest);
    }

    @Override
    public void addNewGroup(String title) {
        bookmarkService.addNewGroup(title);
    }

    @Override
    public void deleteGroup(int id) {
        bookmarkService.deleteGroup(id);
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
    public void editBookmark(BookmarkMoveRequest request) {
        bookmarkService.moveBookmark(request.getBookmarkId(), request.getGroupName());
    }

    @Override
    public void editBookmark(int id, BookmarkEditRequest request) {
        bookmarkService.editBookmark(id, request);
    }

    @Override
    public void renameGroupName(int id, String newGroupName) {
        bookmarkService.renameGroup(id, newGroupName);
    }

    @Override
    public void importFromYaml(String yaml) throws JsonProcessingException {
        bookmarkService.importFromYaml(yaml);
    }

    @Override
    public ResponseEntity<String> exportToYaml() {
        try {
            var fileContent = bookmarkService.export();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDisposition(ContentDisposition.attachment().filename(String.format("bookmarks_%s.yaml", LocalDateTime.now().toEpochSecond(ZoneOffset.UTC))).build());

            return new ResponseEntity<>(fileContent, headers, HttpStatus.OK);
        } catch (JsonProcessingException e) {
            throw new FailedToExportException(e);
        }
    }
}
