package com.goorm.jido.entity.userInterest;

import com.goorm.jido.entity.Category;
import com.goorm.jido.entity.User;

import java.io.Serializable;
import java.util.Objects;

// UserInterest 복합키 클래스
public class UserInterestId implements Serializable {
  private User user;
  private Category category;

  public UserInterestId(){}

  public UserInterestId(User user, Category category) {
    this.user = user;
    this.category = category;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof UserInterestId)) return false;
    UserInterestId that = (UserInterestId) o;

    return Objects.equals(user.getUserId(), that.user.getUserId()) &&
            Objects.equals(category.getCategoryId(), that.category.getCategoryId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(user.getUserId(), category.getCategoryId());
  }
}
