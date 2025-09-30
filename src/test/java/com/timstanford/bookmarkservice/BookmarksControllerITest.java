package com.timstanford.bookmarkservice;

import com.timstanford.bookmarkservice.api.BookmarkRequest;
import com.timstanford.bookmarkservice.data.BookmarksRepository;
import com.timstanford.bookmarkservice.data.GroupRepository;
import com.timstanford.bookmarkservice.service.BookmarkService;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Ignore
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookmarksControllerITest {

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:15-alpine"
    );

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookmarkService service;

    @Autowired
    private BookmarksRepository bookmarksRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Test
    public void willGetAllBookmarks() throws Exception {
        service.addBookmark(createBookmarkTestData("one", "http://www.alpha.com", "Alpha Site"));
        service.addBookmark(createBookmarkTestData("one", "http://www.bravo.com", "Bravo Site"));
        service.addBookmark(createBookmarkTestData("two", "http://www.charlie.com", "Charlie Site"));
        service.addBookmark(createBookmarkTestData("two", "http://www.delta.com", "Delta Site"));

        String contentAsString = mockMvc.perform(get("/bookmarks"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Assert.assertEquals(contentAsString, "[{\"id\":1,\"name\":\"one\",\"bookmarks\":[{\"id\":1,\"title\":\"Alpha Site\",\"url\":\"http://www.alpha.com\",\"favicon\":null},{\"id\":2,\"title\":\"Bravo Site\",\"url\":\"http://www.bravo.com\",\"favicon\":null}]},{\"id\":3,\"name\":\"two\",\"bookmarks\":[{\"id\":3,\"title\":\"Charlie Site\",\"url\":\"http://www.charlie.com\",\"favicon\":null},{\"id\":4,\"title\":\"Delta Site\",\"url\":\"http://www.delta.com\",\"favicon\":null}]},{\"id\":null,\"name\":\"No Group\",\"bookmarks\":[]}]");

    }

    @NotNull
    private static BookmarkRequest createBookmarkTestData(String groupName, String url, String title) {
        BookmarkRequest bookmarkRequest = new BookmarkRequest();
        bookmarkRequest.setGroupName(groupName);
        bookmarkRequest.setUrl(url);
        bookmarkRequest.setTitle(title);
        return bookmarkRequest;
    }
}
