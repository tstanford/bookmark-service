package com.timstanford.bookmarkservice;

import com.timstanford.bookmarkservice.data.BookmarksRepository;
import com.timstanford.bookmarkservice.data.GroupRepository;
import com.timstanford.bookmarkservice.service.BookmarkService;
import org.junit.Assert;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

    @AfterEach
    public void afterEach(){
        deleteAllData();
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
    public void willDeleteAll() throws Exception {
        service.addBookmark(BookmarkTestData.createBookmarkTestData("one", "http://www.alpha.com", "Alpha Site"));
        service.addBookmark(BookmarkTestData.createBookmarkTestData("one", "http://www.bravo.com", "Bravo Site"));
        service.addBookmark(BookmarkTestData.createBookmarkTestData("two", "http://www.charlie.com", "Charlie Site"));
        service.addBookmark(BookmarkTestData.createBookmarkTestData("two", "http://www.delta.com", "Delta Site"));

        mockMvc.perform(delete("/bookmarks/all"))
                .andExpect(status().isOk());

        String contentAsString = mockMvc.perform(get("/bookmarks"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Assert.assertEquals("[{\"id\":null,\"name\":\"No Group\",\"bookmarks\":[]}]", contentAsString);

    }

    @Test
    public void willGetAllBookmarks() throws Exception {
        service.addBookmark(BookmarkTestData.createBookmarkTestData("one", "http://www.alpha.com", "Alpha Site"));
        service.addBookmark(BookmarkTestData.createBookmarkTestData("one", "http://www.bravo.com", "Bravo Site"));
        service.addBookmark(BookmarkTestData.createBookmarkTestData("two", "http://www.charlie.com", "Charlie Site"));
        service.addBookmark(BookmarkTestData.createBookmarkTestData("two", "http://www.delta.com", "Delta Site"));

        String contentAsString = mockMvc.perform(get("/bookmarks"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Assert.assertEquals("[{\"id\":5,\"name\":\"one\",\"bookmarks\":[{\"id\":5,\"title\":\"Alpha Site\",\"url\":\"http://www.alpha.com\",\"favicon\":null},{\"id\":6,\"title\":\"Bravo Site\",\"url\":\"http://www.bravo.com\",\"favicon\":null}]},{\"id\":7,\"name\":\"two\",\"bookmarks\":[{\"id\":7,\"title\":\"Charlie Site\",\"url\":\"http://www.charlie.com\",\"favicon\":null},{\"id\":8,\"title\":\"Delta Site\",\"url\":\"http://www.delta.com\",\"favicon\":null}]},{\"id\":null,\"name\":\"No Group\",\"bookmarks\":[]}]", contentAsString);

    }

    private void deleteAllData(){
        bookmarksRepository.deleteAll();
        groupRepository.deleteAll();
    }

}
