package com.timstanford.bookmarkservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.timstanford.bookmarkservice.api.dtos.BookmarkEditRequest;
import com.timstanford.bookmarkservice.api.BookmarkMapper;
import com.timstanford.bookmarkservice.api.dtos.BookmarkRequest;
import com.timstanford.bookmarkservice.api.exceptions.BookmarkNotFoundException;
import com.timstanford.bookmarkservice.api.exceptions.GroupAlreadyExistsException;
import com.timstanford.bookmarkservice.api.exceptions.GroupNotFoundException;
import com.timstanford.bookmarkservice.data.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.timstanford.bookmarkservice.service.Constants.DEFAULT_ICON;

@Service
public class BookmarkServiceImpl implements BookmarkService {
    private final BookmarksRepository bookmarksRepository;

    private final GroupRepository groupRepository;
    private final BookmarkMapper bookmarkMapper;
    private final FaviconDownloader faviconDownloader;
    private final UserRepository userRepository;
    private final ObjectMapper yamlMapper;

    public BookmarkServiceImpl(
            BookmarksRepository bookmarksRepository,
            GroupRepository groupRepository,
            BookmarkMapper bookmarkMapper,
            FaviconDownloader faviconDownloader,
            UserRepository userRepository,
            @Qualifier("yamlmapper") ObjectMapper yamlMapper
    ) {
        this.bookmarksRepository = bookmarksRepository;
        this.groupRepository = groupRepository;
        this.bookmarkMapper = bookmarkMapper;
        this.faviconDownloader = faviconDownloader;
        this.userRepository = userRepository;
        this.yamlMapper = yamlMapper;
    }

    private int getUserId() {
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("user not found"));
        return user.getUserId();
    }

    @Override
    public List<GroupResponse> getAllBookmarks() {
        List<GroupResponse> groups = groupRepository.findAllByUserIdOrderByName(getUserId())
                .stream()
                .map(this::mapToGroupResponse)
                .collect(Collectors.toList());

        groups.forEach(groupResponse -> {
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
        bookmark.setFavicon("data:image/png;base64,"+DEFAULT_ICON);

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
    @Transactional
    public void deleteAll(){
        var groups = groupRepository.findAllByUserIdOrderByName(getUserId());
        groups.forEach(group -> bookmarksRepository.deleteAllByGroupId(group.getId()));
        groupRepository.deleteAllByUserId(getUserId());
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

    @Override
    public void deleteGroup(int id) {
        var byGroupId = bookmarksRepository.getByGroupId(id);
        if(byGroupId.isEmpty()) {
            groupRepository.deleteById(id);
        } else {
            throw new RuntimeException("Group is not empty");
        }
    }

    @Override
    public void renameGroup(int id, String newGroupName) {
        var group = groupRepository.findById(id).orElseThrow(() -> new GroupNotFoundException(id));

        if(groupRepository.findByNameAndUserId(newGroupName, getUserId()).isPresent()) {
            throw new GroupAlreadyExistsException();
        }

        group.setName(newGroupName);
        groupRepository.save(group);
    }

    @Override
    public String export() throws JsonProcessingException {
        var data = getAllBookmarks();
        ImportFile file = new ImportFile();
        file.setGroups(data.stream().map(groupResponse -> {
            ImportGroup importGroup = new ImportGroup(groupResponse.getName());
            importGroup.setBookmarks(bookmarkMapper.mapToListOfSimpleBookmarks(groupResponse));
            return importGroup;
        }).toList());

        return yamlMapper.writeValueAsString(file);
    }

    @Override
    @Transactional
    public void editBookmark(int id, BookmarkEditRequest request) {
        var bookmark = bookmarksRepository.findById(id).orElseThrow(() -> new BookmarkNotFoundException(id));

        bookmark.setTitle(request.getTitle());
        bookmark.setUrl(request.getUrl());

        bookmarksRepository.save(bookmark);
    }

    private Group findOrCreateGroupByName(BookmarkRequest bookmarkRequest) {
        return findOrCreateGroupByName(bookmarkRequest.getGroupName());
    }

    private Group findOrCreateGroupByName(String title) {
        groupRepository.addGroupIfNotExists(title, getUserId());
        return groupRepository.findByNameAndUserId(title, getUserId()).orElseThrow(() -> new RuntimeException("Group Not Found"));
    }

    private GroupResponse mapToGroupResponse(Group group) {
        GroupResponse groupResponse = new GroupResponse();
        groupResponse.setName(group.getName());
        groupResponse.setId(group.getId());
        groupResponse.setBookmarks(new ArrayList<>());
        return groupResponse;
    }

}
