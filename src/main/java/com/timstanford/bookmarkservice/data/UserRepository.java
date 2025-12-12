package com.timstanford.bookmarkservice.data;

import com.timstanford.bookmarkservice.api.UserWithStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsernameIgnoreCase(String username);

    @Query(value = "select u.user_id as userId, u.username as username, u.email_address as emailAddress, u.is_admin as isAdmin, count(b.*) as bookmarkCount \n" +
            "from \"user\" as u \n" +
            "left join \"group\" as g on g.user_id = u.user_id \n" +
            "left join bookmarks as b on b.group_id = g.id \n" +
            "group by u.user_id, u.username;", nativeQuery = true)
    List<UserWithStats> findAllUsersWithStats();
}
