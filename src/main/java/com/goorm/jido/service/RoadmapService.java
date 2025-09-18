package com.goorm.jido.service;

import com.goorm.jido.dto.*;
import com.goorm.jido.entity.*;
import com.goorm.jido.repository.RoadmapRepository;
import com.goorm.jido.repository.StepContentRepository;
import com.goorm.jido.repository.StepRepository;
import com.goorm.jido.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class RoadmapService {

    private final RoadmapRepository roadmapRepository;
    private final UserRepository userRepository;
    private final StepRepository stepRepository;
    private final StepContentRepository stepContentRepository;
    private final RoadmapLikeService roadmapLikeService;
    private final RoadmapBookmarkService roadmapBookmarkService;

    // 로드맵 생성
    public RoadmapResponseDto saveRoadmap(RoadmapRequestDto dto, Long userId) {
        Long finalAuthorId = (userId != null) ? userId : dto.authorId();
        if (finalAuthorId == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "authorId 또는 로그인 필요");
        }
        if (dto.title() == null || dto.title().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "title 필수");
        }
        if (dto.description() == null || dto.description().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "description 필수");
        }
        if (dto.category() == null || dto.category().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "category 필수");
        }

        User author = userRepository.findById(finalAuthorId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "사용자를 찾을 수 없습니다: " + finalAuthorId));

        LocalDateTime now = LocalDateTime.now();

        Roadmap roadmap = Roadmap.builder()
                .author(author)
                .title(dto.title())
                .description(dto.description())
                .category(dto.category())
                .isPublic(Boolean.TRUE.equals(dto.isPublic()))
                .createdAt(now)
                .updatedAt(now)
                .build();

        // sections DTO → 엔티티로 변환하면서 title + description + roadmap 주입
        if (dto.sections() != null && !dto.sections().isEmpty()) {
            List<RoadmapSection> sectionEntities = new ArrayList<>();
            for (int i = 0; i < dto.sections().size(); i++) {
                SectionRequestDto sectionDto = dto.sections().get(i);
                if (sectionDto == null || sectionDto.title() == null || sectionDto.title().isBlank()) {
                    continue;
                }
                RoadmapSection section = RoadmapSection.fromDto(sectionDto, roadmap, (long) (i + 1));
                sectionEntities.add(section);
            }
            roadmap.setRoadmapSections(sectionEntities);
        }
        Roadmap saved = roadmapRepository.save(roadmap);
        return RoadmapResponseDto.from(saved, 0L, false, 0L, false);
    }

    // 특정 로드맵 조회(라이트)
    @Transactional(readOnly = true)
    public Optional<RoadmapResponseDto> getRoadmap(Long id, Long userId) {
        return roadmapRepository.findById(id)
                .map(r -> RoadmapResponseDto.from(
                        r,
                        roadmapLikeService.countLikes(r.getRoadmapId()),
                        roadmapLikeService.isLiked(userId, r.getRoadmapId()),
                        roadmapBookmarkService.countBookmarks(r.getRoadmapId()),
                        roadmapBookmarkService.isBookmarked(userId, r.getRoadmapId())
                ));
    }

    // 전체 로드맵 조회(라이트)
    @Transactional(readOnly = true)
    public List<RoadmapResponseDto> getAllRoadmaps(Long userId) {
        return roadmapRepository.findAll().stream()
                .map(r -> RoadmapResponseDto.from(
                        r,
                        roadmapLikeService.countLikes(r.getRoadmapId()),
                        roadmapLikeService.isLiked(userId, r.getRoadmapId()),
                        roadmapBookmarkService.countBookmarks(r.getRoadmapId()),
                        roadmapBookmarkService.isBookmarked(userId, r.getRoadmapId())
                ))
                .toList();
    }

    // 로드맵 삭제
    @Transactional
    public void deleteRoadmap(Long id) {
        var roadmap = roadmapRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "로드맵을 찾을 수 없습니다."));
        roadmapRepository.delete(roadmap);
    }

    // 로드맵 수정
    @Transactional
    public RoadmapResponseDto updateRoadmap(Long id, Long userId, RoadmapUpdateRequestDto dto) {
        // 1. 권한 체크 + 로드맵 조회
        Roadmap roadmap = roadmapRepository.findByRoadmapIdAndAuthor_UserId(id, userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "로드맵이 없거나 권한이 없습니다."));

        // 2. 로드맵 기본 정보 업데이트
        roadmap.updateBasicInfo(dto.title(), dto.description(), dto.category(), dto.isPublic());

        // 3. 섹션 전체 교체 (DTO → 엔티티 변환)
        if (dto.sections() != null) {
            // 기존 섹션 다 제거
            roadmap.clearSections();

            // 새로운 섹션들 추가
            for (int i = 0; i < dto.sections().size(); i++) {
                SectionRequestDto sectionDto = dto.sections().get(i);
                RoadmapSection newSection = RoadmapSection.fromDto(sectionDto, roadmap, (long) (i + 1));
                roadmap.addSection(newSection);
            }
        }

        return RoadmapResponseDto.from(roadmap,
                roadmapLikeService.countLikes(roadmap.getRoadmapId()),
                roadmapLikeService.isLiked(userId, roadmap.getRoadmapId()),
                roadmapBookmarkService.countBookmarks(roadmap.getRoadmapId()),
                roadmapBookmarkService.isBookmarked(userId, roadmap.getRoadmapId()));
    }


    // 상세(트리) 조회: 로드맵 + 섹션 + 스텝 + 콘텐츠
    @Transactional(readOnly = true)
    public RoadmapDetailResponseDto getRoadmapDetail(Long id, Long userId) {
        var roadmap = roadmapRepository.findByIdWithSections(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "로드맵을 찾을 수 없습니다."));

        var sections = roadmap.getRoadmapSections();
        if (sections == null || sections.isEmpty()) {
            return RoadmapDetailResponseDto.from(roadmap, List.of());
        }

        var sectionIds = sections.stream().map(RoadmapSection::getSectionId).toList();
        var steps = stepRepository.findBySectionIds(sectionIds);

        var stepIds = steps.stream().map(Step::getStepId).toList();
        var contents = stepIds.isEmpty() ? List.<StepContent>of()
                : stepContentRepository.findByStepIds(stepIds);

        Map<Long, List<StepContentDto>> contentsByStepId = contents.stream()
                .collect(Collectors.groupingBy(
                        c -> c.getStep().getStepId(),
                        Collectors.mapping(StepContentDto::from, Collectors.toList())
                ));

        Map<Long, List<StepDto>> stepsBySectionId = new HashMap<>();
        for (var s : steps) {
            var list = stepsBySectionId.computeIfAbsent(s.getRoadmapSection().getSectionId(), k -> new ArrayList<>());
            list.add(StepDto.of(s, contentsByStepId.getOrDefault(s.getStepId(), List.of())));
        }

        var sectionDtos = sections.stream()
                .sorted(Comparator.comparing(RoadmapSection::getSectionNum))
                .map(sec -> SectionDto.of(sec, stepsBySectionId.getOrDefault(sec.getSectionId(), List.of())))
                .toList();

        return RoadmapDetailResponseDto.from(roadmap,
                sectionDtos,
                roadmapLikeService.countLikes(roadmap.getRoadmapId()),
                roadmapLikeService.isLiked(userId, roadmap.getRoadmapId()),
                roadmapBookmarkService.countBookmarks(roadmap.getRoadmapId()),
                roadmapBookmarkService.isBookmarked(userId, roadmap.getRoadmapId()));
    }
}
