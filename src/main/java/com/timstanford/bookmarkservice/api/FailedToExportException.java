package com.timstanford.bookmarkservice.api;

import com.fasterxml.jackson.core.JsonProcessingException;

public class FailedToExportException extends RuntimeException {
    public FailedToExportException(JsonProcessingException e) {
        super("Failed to export to yaml file", e);
    }
}
