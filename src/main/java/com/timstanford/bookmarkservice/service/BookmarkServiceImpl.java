package com.timstanford.bookmarkservice.service;

import com.timstanford.bookmarkservice.api.BookmarkMapper;
import com.timstanford.bookmarkservice.api.BookmarkRequest;
import com.timstanford.bookmarkservice.data.Bookmark;
import com.timstanford.bookmarkservice.data.BookmarksRepository;
import com.timstanford.bookmarkservice.data.Group;
import com.timstanford.bookmarkservice.data.GroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookmarkServiceImpl implements BookmarkService {

    private final BookmarksRepository bookmarksRepository;
    private final GroupRepository groupRepository;
    private final BookmarkMapper bookmarkMapper;

    public BookmarkServiceImpl(
            BookmarksRepository bookmarksRepository,
            GroupRepository groupRepository,
            BookmarkMapper bookmarkMapper
    ) {
        this.bookmarksRepository = bookmarksRepository;
        this.groupRepository = groupRepository;
        this.bookmarkMapper = bookmarkMapper;
    }

    @Override
    public List<GroupResponse> getAllBookmarks() {
        List<GroupResponse> groups = groupRepository.findAll()
                .stream()
                .map(g -> mapToGroupResponse(g))
                .collect(Collectors.toList());

        groups.add(GroupResponse.getDefault());

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
            bookmarkRequest.setFavicon(getFavicon(bookmarkRequest.getUrl()));
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
    public String getFavicon(String site){
        String icon = "";
        try {
            WebClient client = WebClient.create(site);
            byte[] data = client.get()
                    .uri("/favicon.ico")
                    .retrieve()
                    .bodyToMono(byte[].class)
                    .share()
                    .block();
            icon = Base64.getEncoder().encodeToString(data);
        } catch (Exception exception){
            icon = "iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAAAAAA6mKC9AAAAoklEQVR4AU3PMcqDQBQE4HeZOY12NjYeIJJ4AnObYJN0Kr9eyTqgbiZvsiD6O82w34OFMYp0OeWxSJlEUTzKtqbCFej/wecCdY42graUaLTkOOCD/rXojgikRIW/BM8HbnSjpJVTirRFMYsmhhJDgmREGRiB8f823qdvfDrdVKHpkA7UFhNqha7HugORvdngsk8yjshqFOG0ZQSqwBOIx1anfpH8zz6kV+6TAAAAAElFTkSuQmCC";
        }

        return icon;
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

    private Group findOrCreateGroupByName(BookmarkRequest bookmarkRequest) {
        Group group = new Group();
        group.setName(bookmarkRequest.getGroupName());
        groupRepository.addGroupIfNotExists(group.getName());

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
