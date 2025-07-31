package com.goorm.jido_.domain.controller;

import com.goorm.jido_.domain.entity.StepContent;
import com.goorm.jido_.domain.service.StepContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/step-contents")
@RequiredArgsConstructor
public class StepContentController {
    private final StepContentService stepContentService;

    @PostMapping
    public StepContent create(@RequestBody StepContent content) {
        return stepContentService.saveStepContent(content);
    }

    @GetMapping("/{id}")
    public StepContent get(@PathVariable Long id) {
        return stepContentService.getStepContent(id).orElse(null);
    }

    @GetMapping
    public List<StepContent> getAll() {
        return stepContentService.getAllStepContents();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        stepContentService.deleteStepContent(id);
    }
}
