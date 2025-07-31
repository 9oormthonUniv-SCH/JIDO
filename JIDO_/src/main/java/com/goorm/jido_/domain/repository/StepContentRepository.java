package com.goorm.jido_.domain.repository;

import com.goorm.jido_.domain.entity.StepContent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StepContentRepository extends JpaRepository<StepContent, Long> {
    // 필요 시 커스텀 쿼리 메소드 정의 가능
}
