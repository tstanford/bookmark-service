package com.timstanford.bookmarkservice.api;

import com.timstanford.bookmarkservice.data.Bookmark;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

import static org.junit.jupiter.api.Assertions.*;

class BookmarkMapperTest {

    @Test
    void mapToBookmarkResponse() {
        BookmarkMapper mapper = new BookmarkMapper();
        Bookmark bookmark = new Bookmark();

        var result = mapper.mapToBookmarkResponse(bookmark);

        Assert.notNull(result, "result must not be null");
    }
}