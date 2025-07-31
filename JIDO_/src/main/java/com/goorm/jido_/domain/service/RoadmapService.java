package com.goorm.jido_.domain.service;

import com.goorm.jido_.domain.entity.User;
import com.goorm.jido_.domain.dto.RoadmapRequestDto;
import com.goorm.jido_.domain.entity.Roadmap;
import com.goorm.jido_.domain.repository.RoadmapRepository;
import com.goorm.jido_.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoadmapService {
    private final RoadmapRepository roadmapRepository;
    private final UserRepository userRepository;

    public Roadmap saveRoadmap(RoadmapRequestDto dto) {
        User author = userRepository.findById(dto.getAuthorId())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        Roadmap roadmap = new Roadmap();
        roadmap.setAuthor(author);
        roadmap.setTitle(dto.getTitle());
        roadmap.setDescription(dto.getDescription());
        roadmap.setCategory(dto.getCategory());
        roadmap.setIsPublic(dto.getIsPublic());

        return roadmapRepository.save(roadmap);
    }

    public Optional<Roadmap> getRoadmap(Long id) {
        return roadmapRepository.findById(id);
    }

    public List<Roadmap> getAllRoadmaps() {
        return roadmapRepository.findAll();
    }

    public void deleteRoadmap(Long id) {
        roadmapRepository.deleteById(id);
    }
}