package com.timstanford.bookmarkservice.data;

import jakarta.persistence.*;

@Entity
@Table(name = "[Group]", indexes = @Index(columnList = "name"))
public class Group {

    @Id
    @Column(name = "id", updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", unique=true)
    private String name;

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
}
