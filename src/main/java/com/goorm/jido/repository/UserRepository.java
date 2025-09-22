package com.goorm.jido.repository;

import com.goorm.jido.dto.UserSearchResult;
import com.goorm.jido.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByNickname(String nickname); // 닉네임으로 유저 검색
  Optional<User> findByUserLoginId(String userLoginId);
  Optional<User> findByEmail(String email);

  @Query("SELECT new com.goorm.jido.dto.UserSearchResult(u.userId, u.nickname) " +
          "FROM User u WHERE LOWER(u.nickname) LIKE LOWER(:query)")
  List<UserSearchResult> searchByNicknameLikeOrInitial(@Param("query") String query);
}