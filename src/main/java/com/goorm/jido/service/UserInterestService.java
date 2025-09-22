package com.goorm.jido.service;

import com.goorm.jido.entity.Category;
import com.goorm.jido.entity.User;
import com.goorm.jido.entity.userInterest.UserInterest;
import com.goorm.jido.entity.userInterest.UserInterestId;
import com.goorm.jido.repository.CategoryRepository;
import com.goorm.jido.repository.UserInterestRepository;
import com.goorm.jido.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserInterestService {

  private final UserInterestRepository userInterestRepository;
  private final UserRepository userRepository;

  // READ - 특정 유저 관심사 조회
  public List<UserInterest> getInterestsByUser(Long userId) {
    return userInterestRepository.findByUser_UserId(userId);
  }

  // CREATE - 사용자 관심사 추가
  public UserInterest addInterest(Long userId, Category category) {
    User user = userRepository.findById(userId).orElse(null);
    UserInterest userInterest = UserInterest.builder()
            .user(user)
            .category(category)
            .build();
    return userInterestRepository.save(userInterest);
  }

  // DELETE
  public void removeInterest(User user, Category category) {
    UserInterestId id = new UserInterestId(user, category);

    if (!userInterestRepository.existsById(id)) {
      throw new EntityNotFoundException("해당 관심사가 존재하지 않습니다.");
    }
    userInterestRepository.deleteById(id);
  }
}