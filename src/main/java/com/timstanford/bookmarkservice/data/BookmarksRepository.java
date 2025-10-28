package com.timstanford.bookmarkservice.data;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookmarksRepository extends JpaRepository<Bookmark, Integer> {

    List<Bookmark> findAllByGroupIdOrderByTitle(Integer groupId);
    List<Bookmark> findAllByGroupIdAndUrl(Integer groupId, String url);
    List<Bookmark> getByGroupId(int groupId);

    void deleteAllByGroupId(Integer id);
}
