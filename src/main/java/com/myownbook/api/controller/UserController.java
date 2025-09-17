package com.myownbook.api.controller;

import com.myownbook.api.dto.LoginDTO;
import com.myownbook.api.dto.UserResponseDTO;
import com.myownbook.api.model.User;
import com.myownbook.api.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> signUp(@RequestBody @Valid LoginDTO userDTO) {
        UserResponseDTO savedUser = userService.join(userDTO);
        return ResponseEntity.ok(savedUser);
    }

    @GetMapping
    public ResponseEntity<UserResponseDTO> login(@Valid LoginDTO userDTO) {
        User userByUsername = userService.findUserByUsername(userDTO.getUsername());
        if(passwordEncoder.matches(userDTO.getPassword(), userByUsername.getPassword())) {
            return ResponseEntity.ok(userService.loginUser(userByUsername));
        }
        throw new InsufficientAuthenticationException("이름과 비밀번호를 다시 확인해 주세요");
    }
}
