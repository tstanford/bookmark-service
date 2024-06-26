package com.timstanford.bookmarkservice.data;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookmarksRepository extends JpaRepository<Bookmark, Integer> {

    List<Bookmark> findAllByGroupId(Integer groupId);

}
