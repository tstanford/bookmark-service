package com.timstanford.bookmarkservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.timstanford.bookmarkservice.api.BookmarkMapper;
import com.timstanford.bookmarkservice.api.BookmarkRequest;
import com.timstanford.bookmarkservice.data.Bookmark;
import com.timstanford.bookmarkservice.data.BookmarksRepository;
import com.timstanford.bookmarkservice.data.Group;
import com.timstanford.bookmarkservice.data.GroupRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookmarkServiceImpl implements BookmarkService {

    private final BookmarksRepository bookmarksRepository;
    private final GroupRepository groupRepository;
    private final BookmarkMapper bookmarkMapper;
    private final FaviconDownloader faviconDownloader;
    private final ObjectMapper yamlMapper = new ObjectMapper(new YAMLFactory());

    public BookmarkServiceImpl(
            BookmarksRepository bookmarksRepository,
            GroupRepository groupRepository,
            BookmarkMapper bookmarkMapper,
            FaviconDownloader faviconDownloader
    ) {
        this.bookmarksRepository = bookmarksRepository;
        this.groupRepository = groupRepository;
        this.bookmarkMapper = bookmarkMapper;
        this.faviconDownloader = faviconDownloader;
    }

    @Override
    public List<GroupResponse> getAllBookmarks() {
        List<GroupResponse> groups = groupRepository.findAll()
                .stream()
                .map(g -> mapToGroupResponse(g))
                .collect(Collectors.toList());

        groups.stream().forEach(groupResponse -> {
            List<Bookmark> allByGroupId = bookmarksRepository.findAllByGroupId(groupResponse.getId());
            List<BookmarkResponse> bookmarkResponses = allByGroupId.stream()
                    .map(bookmarkMapper::mapToBookmarkResponse)
                    .collect(Collectors.toList());
            groupResponse.setBookmarks(bookmarkResponses);
        });

        return groups;
    }

    @Override
    @Transactional
    public BookmarkResponse addBookmark(BookmarkRequest bookmarkRequest) {
        Group group = findOrCreateGroupByName(bookmarkRequest);

        if(bookmarkRequest.getFavicon() == null) {
            bookmarkRequest.setFavicon(faviconDownloader.getFavicon(bookmarkRequest.getUrl()));
        }

        Bookmark bookmark = bookmarkMapper.mapToBookmark(bookmarkRequest);
        bookmark.setGroupId(group.getId());

        List<Bookmark> existingBookmarks = bookmarksRepository.findAllByGroupIdAndUrl(group.getId(), bookmark.getUrl());

        if (!existingBookmarks.isEmpty()) {
            return bookmarkMapper.mapToBookmarkResponse(bookmark);
        } else {
            return bookmarkMapper.mapToBookmarkResponse(bookmarksRepository.save(bookmark));
        }
    }

    @Override
    public void deleteBookmark(int id) {
        bookmarksRepository.deleteById(id);
    }

    @Override
    public void deleteAll(){
        bookmarksRepository.deleteAll();
        groupRepository.deleteAll();
    }

    @Override
    @Transactional
    public void importFromYaml(String yaml) throws JsonProcessingException {
        ImportFile file = yamlMapper.readValue(yaml, ImportFile.class);
        file.getGroups().forEach(group -> {
            group.getBookmarks().forEach(bookmark -> {
                var bookmarkRequest = new BookmarkRequest();
                bookmarkRequest.setGroupName(group.getName());
                bookmarkRequest.setTitle(bookmark.getTitle());
                bookmarkRequest.setUrl(bookmark.getUrl());
                addBookmark(bookmarkRequest);
            });
        });
    }

    private Group findOrCreateGroupByName(BookmarkRequest bookmarkRequest) {
        groupRepository.addGroupIfNotExists(bookmarkRequest.getGroupName());

        return groupRepository.findByName(bookmarkRequest.getGroupName()).orElseThrow(() -> new RuntimeException("Group Not Found"));
    }

    private GroupResponse mapToGroupResponse(Group group) {
        GroupResponse groupResponse = new GroupResponse();
        groupResponse.setName(group.getName());
        groupResponse.setId(group.getId());
        groupResponse.setBookmarks(new ArrayList<>());
        return groupResponse;
    }

}
