package com.myownbook.api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "token")
public class UserToken {

    @Id
    @GeneratedValue
    @Column(updatable = false, nullable = false)
    private Long id;

    @NotNull(message = "리프레쉬 토큰이 필요합니다")
    @Column(nullable = false)
    private String refreshToken;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    public Long getId() {
        return id;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public UserToken setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
        return this;
    }

    public User getUser() {
        return user;
    }

    public UserToken setUser(User user) {
        this.user = user;
        return this;
    }
}
