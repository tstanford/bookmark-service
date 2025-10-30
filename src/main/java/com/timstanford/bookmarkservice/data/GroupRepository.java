package com.timstanford.bookmarkservice.data;

import com.timstanford.bookmarkservice.service.GroupResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface GroupRepository extends JpaRepository<Group, Integer> {
    Optional<Group> findByNameAndUserId(String groupName, int userId);

    @Query(value = "insert into \"group\" (name, userId) values (?1, ?2) on conflict do nothing;", nativeQuery = true)
    @Modifying
    void addGroupIfNotExists(String name, int userId);

    List<Group> findAllByUserIdOrderByName(int userId);

    void deleteAllByUserId(int userId);
}
