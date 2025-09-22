package com.goorm.jido.controller;


import com.goorm.jido.entity.Category;
import com.goorm.jido.entity.User;
import com.goorm.jido.entity.userInterest.UserInterest;
import com.goorm.jido.repository.CategoryRepository;
import com.goorm.jido.service.CategoryService;
import com.goorm.jido.service.UserInterestService;
import com.goorm.jido.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user-interests")
@RequiredArgsConstructor
public class UserInterestController {

  private final UserInterestService userInterestService;
  private final UserService userService;
  private final CategoryRepository categoryRepository;

  // READ (특정 유저 관심사 조회)
  @GetMapping("/{userId}")
  @Operation(
          description = "사용자의 관심사를 조회합니다."
  )
  public List<UserInterest> getUserInterests(@PathVariable Long userId) {
    return userInterestService.getInterestsByUser(userId);
  }

  // DELETE (특정 관심사 삭제)
  @Operation(
          description = "사용자의 특정 관심사를 삭제합니다."
  )
  @DeleteMapping("/{userId}/{categoryId}")
  public void deleteInterest(@PathVariable Long userId, @PathVariable String categoryId) {
    User user = userService.findById(userId);
    Category category = categoryRepository.findByCategoryId(categoryId).orElse(null);

    userInterestService.removeInterest(user, category);
  }


}
