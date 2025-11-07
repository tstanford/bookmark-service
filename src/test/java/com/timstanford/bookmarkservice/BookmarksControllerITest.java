package com.timstanford.bookmarkservice;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.timstanford.bookmarkservice.data.BookmarksRepository;
import com.timstanford.bookmarkservice.data.GroupRepository;
import com.timstanford.bookmarkservice.data.UserRepository;
import com.timstanford.bookmarkservice.security.UserService;
import com.timstanford.bookmarkservice.service.BookmarkService;
import com.timstanford.bookmarkservice.service.FaviconDownloader;
import com.timstanford.bookmarkservice.service.GroupResponse;
import com.timstanford.bookmarkservice.utils.WebClientHelper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookmarksControllerITest {

    static final String USERNAME = "tim";
    static final String PASSWORD = "password";
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");
    
    @LocalServerPort
    int portNumber; 
    
    @MockBean
    FaviconDownloader faviconDownloader;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private BookmarkService service;

    @Autowired
    private BookmarksRepository bookmarksRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    private WebClientHelper webClientHelper;

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

    @AfterEach
    public void afterEach() {
        deleteAllData();
    }

    @BeforeEach
    public void beforeEach() throws Exception {
        webClientHelper = new WebClientHelper("http://localhost:"+portNumber);
        userService.registerUser(USERNAME, PASSWORD);

        String payload = """
                {
                    "username": "%s",
                    "password": "%s"
                }
                """.formatted(USERNAME, PASSWORD);

        ResponseEntity<String> result = webClientHelper.postString("/api/login", payload);
        assertNotNull(result);

        webClientHelper.setToken(result.getBody());
    }

    @Test
    public void doNothing(){
        System.out.println("hello");
    }

    private void addBookmark(String title, String url, String groupName){
        var payload = """
        {
            "title": "%s",
            "url": "%s",
            "groupName": "%s"
        }
        """.formatted(title, url, groupName);

        webClientHelper.postStringWithAuth("/api/bookmarks", payload);
    }

    @Test
    public void willDeleteAll() throws Exception {
        addBookmark("Alpha Site", "http://www.alpha.com", "one");
        addBookmark("Bravo Site", "http://www.bravo.com", "one");
        addBookmark("Charlie Site", "http://www.charlie.com", "two");
        addBookmark("Delta Site", "http://www.delta.com", "two");

        var deleteResult = webClientHelper.delete("/api/all");

        assertTrue(deleteResult.getStatusCode().is2xxSuccessful());

        ResponseEntity<String> getResults = webClientHelper.getString("/api/bookmarks");
        assertTrue(getResults.getStatusCode().is2xxSuccessful());
        assertNotNull(getResults);

        assertEquals("[]", getResults.getBody());
    }

    @Test
    public void willGetAllBookmarks() throws Exception {
        addBookmark("Alpha Site", "http://www.alpha.com", "one");
        addBookmark("Bravo Site", "http://www.bravo.com", "one");
        addBookmark("Charlie Site", "http://www.charlie.com", "two");
        addBookmark("Delta Site", "http://www.delta.com", "two");

        ResponseEntity<String> getResult = webClientHelper.getString("/api/bookmarks");
        assertTrue(getResult.getStatusCode().is2xxSuccessful());

        List<GroupResponse> groups = objectMapper.readValue(getResult.getBody(), new TypeReference<>() {});

        assertEquals(2, groups.size());
        assertEquals("one", groups.get(0).getName());
        assertEquals(2, groups.get(0).getBookmarks().size());
        assertEquals("two", groups.get(1).getName());
        assertEquals(2, groups.get(1).getBookmarks().size());
    }

    @Test
    public void importFromYamlFile() throws Exception {
        var importFile = new String(Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream("importfile.yml")).readAllBytes());

        ResponseEntity<String> postResult = webClientHelper.postStringWithAuth("/api/import", importFile);
        assertTrue(postResult.getStatusCode().is2xxSuccessful());

        var groups = groupRepository.findAll();
        assertEquals(2, groups.size());
    }

    private void deleteAllData() {
        bookmarksRepository.deleteAll();
        groupRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void deleteBookmark() throws Exception {
        addBookmark("Alpha Site", "http://www.alpha.com", "one");
        var result = Objects.requireNonNull(webClientHelper.getString("/api/bookmarks")).getBody();
        List<GroupResponse> groups = objectMapper.readValue(result, new TypeReference<>() {});
        var id = groups.get(0).getBookmarks().get(0).getId();

        webClientHelper.delete("/api/bookmarks/"+id);

        var getResult2 = Objects.requireNonNull(webClientHelper.getString("/api/bookmarks")).getBody();
        List<GroupResponse> groups2 = objectMapper.readValue(getResult2, new TypeReference<>() {
        });

        assertEquals(1, groups2.size());
        assertEquals("one", groups2.get(0).getName());
        assertEquals(0, groups2.get(0).getBookmarks().size());
    }
}
