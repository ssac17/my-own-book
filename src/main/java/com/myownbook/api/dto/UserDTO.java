package com.myownbook.api.dto;

import com.myownbook.api.model.Book;
import com.myownbook.api.model.RoleEnum;

public class UserDTO {
    private Long id;
    private String username;
    private RoleEnum role;

    public UserDTO() {
    }

    public UserDTO(Long id, String username, RoleEnum role) {
        this.id = id;
        this.username = username;
        this.role = role;
    }

    public UserDTO(Book book) {
        this.id = book.getUser().getId();
        this.username = book.getUser().getUsername();
        this.role = book.getUser().getRole();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public RoleEnum getRole() {
        return role;
    }

    public void setRole(RoleEnum role) {
        this.role = role;
    }
}
