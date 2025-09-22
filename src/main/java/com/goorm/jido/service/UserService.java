package com.goorm.jido.service;
import com.goorm.jido.dto.SignupRequestDto;
import com.goorm.jido.dto.UserPatchRequestDto;
import com.goorm.jido.entity.User;
import com.goorm.jido.entity.userInterest.UserInterest;
import com.goorm.jido.repository.CategoryRepository;
import com.goorm.jido.repository.UserInterestRepository;
import com.goorm.jido.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserService {

  private final UserRepository userRepository;
  private final UserInterestRepository userInterestRepository;
  private final CategoryRepository categoryRepository;

  private final BCryptPasswordEncoder bCryptPasswordEncoder;

  public User findByNickname(String nickname) { // 닉네임으로 유저 검색
    return userRepository.findByNickname(nickname)
            .orElseThrow(() -> new IllegalArgumentException());
  }

  public User findById(Long id) { // User.Id로 검색
    return userRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException());
  }


  public User findByUserLoginId(String userLoginId) {
    return userRepository.findByUserLoginId(userLoginId)
            .orElseThrow(() -> new IllegalArgumentException());
  }

  public User findByEmail(String email) {
    return userRepository.findByEmail(email)
            .orElseThrow(() -> new IllegalArgumentException());
  }


  public Long save(SignupRequestDto dto) {
    // 1. 유저 정보 저장
    User saved =  userRepository.save(User.builder()
            .userLoginId(dto.userLoginId())
            .password(bCryptPasswordEncoder.encode(dto.password()))
            .email(dto.email())
            .nickname(dto.nickName())
            //.age(dto.age())
            .build()
    );


    // 2. 유저 관심 카테고리 저장
    for (String category : dto.categories()){
      System.out.println(category);
      userInterestRepository.save(UserInterest.builder()
              .user(saved)
              .category(categoryRepository.findByCategoryId(category).orElseThrow(() -> new IllegalArgumentException("dddddddd")))
              .build()
      );
    }

    return  saved.getUserId();
  }


  @Transactional
  // 유저 정보 업데이트
  public User patchUser(Long id, UserPatchRequestDto dto) {
    User user = userRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));

    if (dto.getUserLoginId() != null) {
      user.setUserLoginId(dto.getUserLoginId());
    }
    if (dto.getEmail() != null) {
      user.setEmail(dto.getEmail());
    }
    if (dto.getNickname() != null) {
      user.setNickname(dto.getNickname());
    }
    if (dto.getPassword() != null) {
      user.setPassword(bCryptPasswordEncoder.encode(dto.getPassword()));
    }
    if (dto.getAge() != null) {
      user.setAge(dto.getAge());
    }

//    return userRepository.save(user);
    return user;
  }
}
