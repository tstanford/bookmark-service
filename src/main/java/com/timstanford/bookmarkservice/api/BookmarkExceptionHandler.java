package com.timstanford.bookmarkservice.api;

import com.timstanford.bookmarkservice.api.exceptions.BookmarkNotFoundException;
import com.timstanford.bookmarkservice.api.exceptions.GroupAlreadyExistsException;
import com.timstanford.bookmarkservice.api.exceptions.GroupNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class BookmarkExceptionHandler {

    @ExceptionHandler(GroupNotFoundException.class)
    public ResponseEntity<String> handleNotFoundException(GroupNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(BookmarkNotFoundException.class)
    public ResponseEntity<String> handleNotFoundException(BookmarkNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(GroupAlreadyExistsException.class)
    public ResponseEntity<String> handleGroupAlreadyExistsException(GroupAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
    }

}
