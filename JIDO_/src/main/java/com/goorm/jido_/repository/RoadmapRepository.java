package com.goorm.jido_.repository;

import com.goorm.jido_.entity.Roadmap;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoadmapRepository extends JpaRepository<Roadmap, Long> {
    // 필요 시 커스텀 쿼리 메소드 정의 가능
}
