package com.myownbook.api.service;

import com.myownbook.api.dto.LoginDTO;
import com.myownbook.api.dto.UserResponseDTO;
import com.myownbook.api.exception.InvalidRefreshTokenException;
import com.myownbook.api.model.User;
import com.myownbook.api.model.UserToken;
import com.myownbook.api.repository.UserRepository;
import com.myownbook.api.repository.UserTokenRepository;
import com.myownbook.api.security.JwtManager;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.apache.logging.log4j.util.Strings;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Objects;
import java.util.Random;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserTokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtManager tokenManager;

    public UserService(UserRepository userRepository, UserTokenRepository tokenRepository, PasswordEncoder passwordEncoder, JwtManager tokenManager) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenManager = tokenManager;
    }

    @Transactional
    public UserResponseDTO join(@Valid LoginDTO userDTO) {
        User findUser = userRepository.findByUsername(userDTO.getUsername());
        if(!Objects.isNull(findUser)) {
            throw new IllegalArgumentException("이미 등록된 유저입니다.");
        }
        User joinUser = new User();
        joinUser.setUsername(userDTO.getUsername());
        joinUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        User savedUser = userRepository.save(joinUser);

        return createSignedUserWithRefreshToken(savedUser, "축하합니다! 회원가입 되었습니다.");
    }

    public User findUserByUsername(String username) {
        if(Strings.isBlank(username)) {
            throw new UsernameNotFoundException("잘못된 사용입니다.");
        }
        String name = username.trim();
        User findUser = userRepository.findByUsername(name);
        if(Objects.isNull(findUser)) {
            throw new UsernameNotFoundException(String.format("사용자(%s)를 찾을수 없습니다.", name));
        }
        return findUser;
    }

    public UserResponseDTO loginUser(User user) {
        tokenRepository.deleteByUsersId(user.getId());
        return createSignedUserWithRefreshToken(user, "로그인 성공!");
    }

    private void removeRefreshToken(String refreshToken) {
        tokenRepository.findByRefreshToken(refreshToken).ifPresentOrElse(tokenRepository::delete, () -> {
            throw new InvalidRefreshTokenException("잘못된 토큰입니다");
        });
    }

    private UserResponseDTO createSignedUserWithRefreshToken(User user, String message) {
        return createSignedInUser(user, message).setRefreshToken(createRefreshToken(user));
    }

    private UserResponseDTO createSignedInUser(User user, String message) {
        String token = tokenManager.create(org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(Objects.nonNull(user.getRole()) ? user.getRole().name() : "").build());
        return new UserResponseDTO().setUsername(user.getUsername())
                .setAccessToken(token).setUserId(user.getId()).setMessage(message);
    }


    private String createRefreshToken(User user) {
        String token = randomKey(128);
        tokenRepository.save(new UserToken().setRefreshToken(token).setUser(user));
        return token;
    }

    private String randomKey(int length) {
        final Random random = new SecureRandom();
        return String.format("%" + length + "s", new BigInteger(length * 5 /*base 32, 2^5*/, random)
                .toString(32)).replace('\u0020', '0');
    }
}
