package com.myownbook.api.controller;

import com.myownbook.api.dto.UserDTO;
import com.myownbook.api.dto.UserResponseDTO;
import com.myownbook.api.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> join(@RequestBody @Valid UserDTO userDTO) {
        UserResponseDTO savedUser = userService.join(userDTO);
        return ResponseEntity.ok(savedUser);
    }
}
