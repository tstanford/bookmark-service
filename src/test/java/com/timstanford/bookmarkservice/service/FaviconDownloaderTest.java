package com.timstanford.bookmarkservice.service;

import com.timstanford.bookmarkservice.data.Bookmark;
import com.timstanford.bookmarkservice.data.BookmarksRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class FaviconDownloaderTest {
    private final BookmarksRepository bookmarksRepository = mock(BookmarksRepository.class);

    @Test
    public void getJenkinsFavicon(){
        Bookmark bookmark = new Bookmark();

        when(bookmarksRepository.findById(1)).thenReturn(Optional.of(bookmark));
        var faviconDownloader = new FaviconDownloader(bookmarksRepository);

        faviconDownloader.updateFavicon(1, "http://jenkins.timcloud.uk/");
    }
}