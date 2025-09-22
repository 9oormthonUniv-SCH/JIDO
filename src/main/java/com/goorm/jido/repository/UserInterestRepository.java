package com.goorm.jido.repository;

import com.goorm.jido.entity.userInterest.UserInterest;
import com.goorm.jido.entity.userInterest.UserInterestId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserInterestRepository extends JpaRepository<UserInterest, UserInterestId> {
  List<UserInterest> findByUser_UserId(Long userId);
}


//public interface UserInterestRepository extends JpaRepository<UserInterest, Long> { }
