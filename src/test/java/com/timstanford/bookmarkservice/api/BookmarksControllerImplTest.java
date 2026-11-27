package com.timstanford.bookmarkservice.api;

import com.timstanford.bookmarkservice.service.BookmarkService;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

class BookmarksControllerImplTest {

    private final BookmarkService service = mock(BookmarkService.class);
    private final BookmarkMapper mapper = mock(BookmarkMapper.class);
    private BookmarksControllerImpl controller = new BookmarksControllerImpl(service, mapper);

    @Test
    void refreshFavicon() {
        controller.refreshFavicon(1);
        verify(service).refreshFavicon(1);
        verifyNoMoreInteractions(service);
    }

    @Test
    void testRefreshFavicon() {
        controller.refreshFavicon(List.of(1,2,3));
        
        verify(service).refreshFavicon(1);
        verify(service).refreshFavicon(2);
        verify(service).refreshFavicon(3);
        verify(service, times(3)).refreshFavicon(anyInt());
    }
}