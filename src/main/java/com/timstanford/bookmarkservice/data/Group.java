package com.timstanford.bookmarkservice.data;

import jakarta.persistence.*;

@Entity
@Table(name="[Group]", indexes = {
        @Index(name = "uniqueIndex", columnList = "name,userId", unique = true)
})
public class Group {

    @Id
    @Column(name = "id", updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "userId")
    private int userId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
