package com.goorm.jido_.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.Date;

@Entity
@Table(name = "user")
@Getter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", updatable = false)
    private Long userId; // 사용자 고유 ID

    @Column(name = "user_login_id", nullable = false, unique = true)
    private String userLoginId; // 로그인용 아이디

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "nickname", nullable = false)
    private String nickname; // 닉네임 (프로필 표시 이름)

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(name = "age")
    private Integer age;

    // created_at 열이 AUTO_INCREMENT 이므로, 아래와 같이 설정
    // insertable = false -> 	JPA가 insert 시 이 컬럼 값을 쿼리에 포함하지 않음
    // updatable = false -> JPA가 update 시 이 컬럼 값을 건드리지 않음
    @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
    private Date createdAt; // 가입 일시

    @Column(name = "updated_at")
    private Date updatedAt; // 정보 수정일시
}