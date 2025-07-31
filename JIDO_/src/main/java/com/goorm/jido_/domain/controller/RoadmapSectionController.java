package com.goorm.jido_.domain.controller;

import com.goorm.jido_.domain.entity.RoadmapSection;
import com.goorm.jido_.domain.service.RoadmapSectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sections")
@RequiredArgsConstructor
public class RoadmapSectionController {
    private final RoadmapSectionService roadmapSectionService;

    @PostMapping
    public RoadmapSection create(@RequestBody RoadmapSection section) {
        return roadmapSectionService.saveSection(section);
    }

    @GetMapping("/{id}")
    public RoadmapSection get(@PathVariable Long id) {
        return roadmapSectionService.getSection(id).orElse(null);
    }

    @GetMapping
    public List<RoadmapSection> getAll() {
        return roadmapSectionService.getAllSections();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        roadmapSectionService.deleteSection(id);
    }
}
