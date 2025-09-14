package com.myownbook.api.repository;

import com.myownbook.api.model.UserToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserTokenRepository extends CrudRepository<UserToken, UUID> {
    Optional<UserToken> findByRefreshToken(String refreshToken);
    Optional<UserToken> deleteByUsersId(Long id);
}
