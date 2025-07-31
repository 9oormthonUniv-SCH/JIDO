package com.goorm.jido_.domain.service;

import com.goorm.jido_.domain.entity.RoadmapSection;
import com.goorm.jido_.domain.repository.RoadmapSectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}
