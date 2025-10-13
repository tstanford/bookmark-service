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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import static com.timstanford.bookmarkservice.service.Constants.DEFAULT_ICON;

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
            List<Bookmark> allByGroupId = bookmarksRepository.findAllByGroupIdOrderByTitle(groupResponse.getId());
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

        Bookmark bookmark = bookmarkMapper.mapToBookmark(bookmarkRequest);
        bookmark.setGroupId(group.getId());
        bookmark.setFavicon(Base64.getDecoder().decode(DEFAULT_ICON));

        List<Bookmark> existingBookmarks = bookmarksRepository.findAllByGroupIdAndUrl(group.getId(), bookmark.getUrl());

        if (!existingBookmarks.isEmpty()) {
            return bookmarkMapper.mapToBookmarkResponse(bookmark);
        } else {
            Bookmark savedBookmark = bookmarksRepository.save(bookmark);
            faviconDownloader.updateFavicon(bookmark.getId(), bookmark.getUrl());
            return bookmarkMapper.mapToBookmarkResponse(savedBookmark);
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
                bookmarkRequest.setFaviconUrl(bookmark.getFaviconUrl());
                addBookmark(bookmarkRequest);
            });
        });
    }

    @Override
    @Transactional
    public Group addNewGroup(String title) {
        return findOrCreateGroupByName(title);
    }

    @Override
    @Transactional
    public void moveBookmark(int bookmarkId, String groupName) {
        Group group = findOrCreateGroupByName(groupName);

        Bookmark bookmark = bookmarksRepository.getReferenceById(bookmarkId);
        bookmark.setGroupId(group.getId());
        bookmarksRepository.save(bookmark);
    }

    private Group findOrCreateGroupByName(BookmarkRequest bookmarkRequest) {
        return findOrCreateGroupByName(bookmarkRequest.getGroupName());
    }

    private Group findOrCreateGroupByName(String title) {
        groupRepository.addGroupIfNotExists(title);
        return groupRepository.findByName(title).orElseThrow(() -> new RuntimeException("Group Not Found"));
    }

    private GroupResponse mapToGroupResponse(Group group) {
        GroupResponse groupResponse = new GroupResponse();
        groupResponse.setName(group.getName());
        groupResponse.setId(group.getId());
        groupResponse.setBookmarks(new ArrayList<>());
        return groupResponse;
    }

}
