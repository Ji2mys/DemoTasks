package com.example.demo.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String username;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private Set<Task> tasks = new HashSet<>();

    public User() {}

    public User(long id, String username, Set<Task> tasks) {
        this.id = id;
        this.username = username;
        this.tasks = tasks;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public long getId() {
        return id;
    }

    public Set<Task> getTasks() {
        return tasks;
    }
}
