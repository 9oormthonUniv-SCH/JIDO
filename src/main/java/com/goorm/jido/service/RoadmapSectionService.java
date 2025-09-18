package com.goorm.jido.service;

import com.goorm.jido.dto.RoadmapSectionUpdateRequestDto;
import com.goorm.jido.entity.RoadmapSection;
import com.goorm.jido.repository.RoadmapSectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoadmapSectionService {
    private final RoadmapSectionRepository roadmapSectionRepository;

    public RoadmapSection saveSection(RoadmapSection section) {
        return roadmapSectionRepository.save(section);
    }

    public Optional<RoadmapSection> getSection(Long id) {
        return roadmapSectionRepository.findById(id);
    }

    public List<RoadmapSection> getAllSections() {
        return roadmapSectionRepository.findAll();
    }

    public void deleteSection(Long id) {
        roadmapSectionRepository.deleteById(id);
    }

    // 섹션 수정 (작성자 본인만)
    @Transactional
    public RoadmapSection updateSection(Long sectionId, Long userId, RoadmapSectionUpdateRequestDto dto) {
        if (userId == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Login required");
        }
        RoadmapSection section = roadmapSectionRepository
                .findBySectionIdAndRoadmap_Author_UserId(sectionId, userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "섹션이 없거나 권한이 없습니다."));

        section.update(dto.title(), dto.description(), dto.sectionNum()); // 엔티티 도메인 메서드(세터 없이)
        return section; // Dirty checking으로 반영
    }
}
