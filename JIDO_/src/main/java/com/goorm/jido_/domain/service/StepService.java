package com.goorm.jido_.domain.service;

import com.goorm.jido_.domain.entity.Step;
import com.goorm.jido_.domain.repository.StepRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StepService {
    private final StepRepository stepRepository;

    public Step saveStep(Step step) {
        return stepRepository.save(step);
    }

    public Optional<Step> getStep(Long id) {
        return stepRepository.findById(id);
    }

    public List<Step> getAllSteps() {
        return stepRepository.findAll();
    }

    public void deleteStep(Long id) {
        stepRepository.deleteById(id);
    }
}
