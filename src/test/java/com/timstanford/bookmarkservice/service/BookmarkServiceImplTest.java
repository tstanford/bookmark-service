package com.timstanford.bookmarkservice.service;

import com.timstanford.bookmarkservice.data.Bookmark;
import com.timstanford.bookmarkservice.data.BookmarksRepository;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookmarkServiceImplTest {

    private BookmarksRepository repo = mock(BookmarksRepository.class);
    private FaviconDownloader downloader = mock(FaviconDownloader.class);
    private BookmarkService service = new BookmarkServiceImpl(repo, null, null, downloader, null, null);

    @Test
    void refreshFavicon() {
        int id = 1;
        Bookmark bookmark = new Bookmark();
        bookmark.setUrl("http://www.google.com");
        when(repo.findById(id)).thenReturn(Optional.of(bookmark));

        service.refreshFavicon(id);

        verify(downloader, times(1)).updateFavicon(id, "http://www.google.com");
    }
}