package com.myownbook.api.dto;

public class UserResponseDTO {
    private String message;
    private Long userId;
    private String username;
    private String refreshToken;
    private String accessToken;

    public UserResponseDTO() {}

    public Long getUserId() {
        return userId;
    }

    public UserResponseDTO setUserId(Long userId) {
        this.userId = userId;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public UserResponseDTO setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public UserResponseDTO setMessage(String message) {
        this.message = message;
        return this;
    }

    public UserResponseDTO setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
        return this;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public UserResponseDTO setAccessToken(String accessToken) {
        this.accessToken = accessToken;
        return this;
    }

    public String getAccessToken() {
        return accessToken;
    }
}
