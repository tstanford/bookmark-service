package com.timstanford.bookmarkservice;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.timstanford.bookmarkservice.data.Bookmark;
import com.timstanford.bookmarkservice.data.BookmarksRepository;
import com.timstanford.bookmarkservice.data.GroupRepository;
import com.timstanford.bookmarkservice.service.BookmarkService;
import com.timstanford.bookmarkservice.service.FaviconDownloader;
import com.timstanford.bookmarkservice.service.GroupResponse;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookmarksControllerITest {

    @MockBean
    FaviconDownloader faviconDownloader;

    @Autowired
    ObjectMapper objectMapper;

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

        mockMvc.perform(delete("/all"))
                .andExpect(status().isOk());

        String contentAsString = mockMvc.perform(get("/bookmarks"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals("[]", contentAsString);

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

        List<GroupResponse> groups = objectMapper.readValue(contentAsString, new TypeReference<>() {});

        assertEquals(2, groups.size());
        assertEquals("one", groups.get(0).getName());
        assertEquals(2, groups.get(0).getBookmarks().size());
        assertEquals("two", groups.get(1).getName());
        assertEquals(2, groups.get(1).getBookmarks().size());
    }

    @Test
    public void importFromYamlFile() throws Exception {
        mockMvc.perform(post("/import")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("---\n" +
                                "groups:\n" +
                                "  - name: Group one\n" +
                                "    bookmarks:\n" +
                                "      - title: Cloudflare\n" +
                                "        url: https://www.cloudflare.com/\n" +
                                "      - title: Jenkins\n" +
                                "        url: https://alpha.com\n" +
                                "      - title: PVE\n" +
                                "        url: https://beta.com\n" +
                                "      - title: Nginx Proxy Manager - Admin\n" +
                                "        url: http://delta.com/\n" +
                                "      - title: Portainer\n" +
                                "        url: https://blah.com\n" +
                                "      - title: Wordpress\n" +
                                "        url: https://someplace.com/\n" +
                                "      - title: K3S Test\n" +
                                "        url: https://testing.com/\n" +
                                "\n" +
                                "  - name: Blogs\n" +
                                "    bookmarks:\n" +
                                "      - title: Baeldung\n" +
                                "        url: https://www.baeldung.com/\n"))
                .andExpect(status().isOk());

        var groups = groupRepository.findAll();
        assertEquals(2, groups.size());
    }

    private void deleteAllData(){
        bookmarksRepository.deleteAll();
        groupRepository.deleteAll();
    }

}
