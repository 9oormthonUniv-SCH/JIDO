package com.goorm.jido.exception;

// 유저 찾을수 없을때 예외
public class UserNotFoundException extends RuntimeException {
  public UserNotFoundException(String message) {
    super(message);
  }
}
