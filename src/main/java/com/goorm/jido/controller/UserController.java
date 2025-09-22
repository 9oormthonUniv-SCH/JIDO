package com.goorm.jido.controller;

import com.goorm.jido.dto.*;
import com.goorm.jido.entity.User;
import com.goorm.jido.config.CustomLogoutSuccessHandler;
import com.goorm.jido.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
public class UserController {

  private final UserService userService;
  private final CustomLogoutSuccessHandler customLogoutSuccessHandler;

  private final AuthenticationManager authenticationManager;

  // 사용자 정보 수정
  @PatchMapping("/user/{id}")
  public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody UserPatchRequestDto dto) {
    return ResponseEntity.status(HttpStatus.OK)
            .body(userService.patchUser(id, dto));
  }

  // 닉네임으로 유저 검색
  @GetMapping("/user/nick/{nickname}")
  @Operation(
          description = "사용자의 닉네임으로 유저 정보를 검색합니다."
  )
  public ResponseEntity<UserResponseDto> findByNickname(@PathVariable String nickname) {
    try{
      User user = userService.findByNickname(nickname);

      return ResponseEntity.status(HttpStatus.OK)
              .body(UserResponseDto.from(user));

    }catch (IllegalArgumentException e){
      return ResponseEntity.notFound().build();
    }
  }

  // 로그인용 아이디로 유저 검색
  @GetMapping("/user/loginid/{userLoginId}")
  @Operation(
          description = "사용자의 로그인 아이디로 유저 정보를 검색합니다."
  )
  public ResponseEntity<UserResponseDto> findByUserLoginId(@PathVariable String userLoginId) {
    try{
      User user = userService.findByUserLoginId(userLoginId);

      return ResponseEntity.status(HttpStatus.OK)
              .body(UserResponseDto.from(user));

    }catch (IllegalArgumentException e){
      return ResponseEntity.notFound().build();
    }
  }


  // 사용자 조회 (유저 찾기)
  @GetMapping("/user/{id}")
  public ResponseEntity<UserResponseDto> findById(@PathVariable Long id) {
    try{
      User user = userService.findById(id);
      return ResponseEntity.status(HttpStatus.OK)
              .body(UserResponseDto.from(user));

    }catch (IllegalArgumentException e){
      return ResponseEntity.notFound().build();
    }
  }

  // 이메일로 유저 검색
  @GetMapping("/user/email/{email}")
  @Operation(
          description = "사용자의 이메일으로 유저 정보를 검색합니다."
  )
  public ResponseEntity<UserResponseDto> findByEmail(@PathVariable String email) {
    try{
      User user = userService.findByEmail(email);

      return ResponseEntity.status(HttpStatus.OK)
              .body(UserResponseDto.from(user));

    }catch (IllegalArgumentException e){
      return ResponseEntity.notFound().build();
    }
  }



  // 회원 가입
  @PostMapping("/user")
  public ResponseEntity<SignupResponseDto> signUp(@RequestBody SignupRequestDto request){

    SignupResponseDto signupResponseDto = new SignupResponseDto(
            userService.save(request), // userId
            "signup successful"
    );

    return ResponseEntity.ok(signupResponseDto);
  }

  // 로그 아웃
  @GetMapping("/api/logout")
  public void logout(HttpServletRequest request,
                     HttpServletResponse response) throws IOException, ServletException {
    // 1. 인증 객체
    var authentication = SecurityContextHolder.getContext().getAuthentication();

    // 2. 로그아웃 수행
    new SecurityContextLogoutHandler().logout(request, response, authentication);

    // 3. 로그아웃 핸들러 호출
    customLogoutSuccessHandler.onLogoutSuccess(request, response, authentication);
  }


  // 로그인
  @Operation(summary = "로그인 API", description = "username, password를 JSON으로 보내면 로그인 처리됩니다.")
  @PostMapping("/api/login")
  public void login(@RequestBody LoginRequest request) {
    // 여기는 실제 동작하지 않고, Swagger 문서용 dummy API
    throw new UnsupportedOperationException("This is handled by Spring Security filter");
  }

}
