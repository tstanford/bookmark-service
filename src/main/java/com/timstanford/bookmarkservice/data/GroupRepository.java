package com.timstanford.bookmarkservice.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface GroupRepository extends JpaRepository<Group, Integer> {
    Group findByName(String groupName);

    @Query(value = "insert into \"group\" (name) values (?1) on conflict do nothing;", nativeQuery = true)
    @Modifying
    void addGroupIfNotExists(String name);
}
