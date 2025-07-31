package com.goorm.jido_.domain.controller;

import com.goorm.jido_.domain.dto.RoadmapRequestDto;
import com.goorm.jido_.domain.entity.Roadmap;
import com.goorm.jido_.domain.service.RoadmapService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roadmaps")
@RequiredArgsConstructor
public class RoadmapController {
    private final RoadmapService roadmapService;

    // 로드맵 생성
    @PostMapping
    public Roadmap create(@RequestBody RoadmapRequestDto dto) {
        return roadmapService.saveRoadmap(dto);
    }

    // 특정 로드맵 조회
    @GetMapping("/{id}")
    public Roadmap get(@PathVariable Long id) {
        return roadmapService.getRoadmap(id).orElse(null);
    }

    // 전체 로드맵 조회
    @GetMapping
    public List<Roadmap> getAll() {
        return roadmapService.getAllRoadmaps();
    }

    // 로드맵 삭제
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        roadmapService.deleteRoadmap(id);
    }
}