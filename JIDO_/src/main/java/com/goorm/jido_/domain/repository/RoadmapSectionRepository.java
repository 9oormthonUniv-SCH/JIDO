package com.goorm.jido_.domain.repository;

import com.goorm.jido_.domain.entity.RoadmapSection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoadmapSectionRepository extends JpaRepository<RoadmapSection, Long> {
    // 필요 시 커스텀 쿼리 메서드 추가 가능
}
