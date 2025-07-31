package com.goorm.jido_.domain.service;

import com.goorm.jido_.domain.entity.StepContent;
import com.goorm.jido_.domain.repository.StepContentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StepContentService {
    private final StepContentRepository stepContentRepository;

    public StepContent saveStepContent(StepContent stepContent) {
        return stepContentRepository.save(stepContent);
    }

    public Optional<StepContent> getStepContent(Long id) {
        return stepContentRepository.findById(id);
    }

    public List<StepContent> getAllStepContents() {
        return stepContentRepository.findAll();
    }

    public void deleteStepContent(Long id) {
        stepContentRepository.deleteById(id);
    }
}
