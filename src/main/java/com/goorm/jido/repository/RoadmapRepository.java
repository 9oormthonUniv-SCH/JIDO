package com.goorm.jido.repository;

import com.goorm.jido.dto.RoadmapSearchResult;
import com.goorm.jido.entity.Roadmap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RoadmapRepository extends JpaRepository<Roadmap, Long> {

    List<Roadmap> findByAuthor_UserId(Long authorId);
    List<Roadmap> findByIsPublicTrue();  // 공개 로드맵만 조회
    Optional<Roadmap> findByRoadmapIdAndAuthor_UserId(Long roadmapId, Long userId);

    @Query("SELECT new com.goorm.jido.dto.RoadmapSearchResult(r.roadmapId, r.title, r.category) " +
            "FROM Roadmap r WHERE LOWER(r.title) LIKE LOWER(:query)")
    List<RoadmapSearchResult> searchByTitleOrInitial(@Param("query") String query);


    // 🔸 상세 조회용: 로드맵 + 섹션까지만 fetch-join (다중 bag 이슈 회피)
    @Query("""
           SELECT DISTINCT r
           FROM Roadmap r
           LEFT JOIN FETCH r.roadmapSections rs
           WHERE r.roadmapId = :id
           """)
    Optional<Roadmap> findByIdWithSections(@Param("id") Long id);

    @Query("SELECT r FROM Roadmap r WHERE r.category = :categoryId OR r.category LIKE CONCAT(:categoryId, '.%')")
    List<Roadmap> findByCategoryOrSubCategory(@Param("categoryId") String categoryId);
}
