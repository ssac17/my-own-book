package com.myownbook.api.service;

import com.myownbook.api.dto.UserDTO;
import com.myownbook.api.dto.UserResponseDTO;
import com.myownbook.api.model.User;
import com.myownbook.api.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public UserResponseDTO join(@Valid UserDTO userDTO) {
        User findUser = userRepository.findByUsername(userDTO.getUsername());
        if(!Objects.isNull(findUser)) {
            throw new IllegalArgumentException("이미 등록된 유저입니다.");
        }
        User joinUser = new User();
        joinUser.setUsername(userDTO.getUsername());
        joinUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        User savedUser = userRepository.save(joinUser);

        return new UserResponseDTO(savedUser.getUsername(), "신규가입 되었습니다.");
    }
}
