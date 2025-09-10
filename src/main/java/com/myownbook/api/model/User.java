package com.myownbook.api.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "client")
public class User {

    @Id
    private UUID id;

    @Column(nullable = false, length = 20)
    private String username;

    @Column(nullable = false, length = 30)
    private String password;
}
