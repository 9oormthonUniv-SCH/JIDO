package com.goorm.jido_.domain.controller;

import com.goorm.jido_.domain.entity.Step;
import com.goorm.jido_.domain.service.StepService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/steps")
@RequiredArgsConstructor
public class StepController {
    private final StepService stepService;

    @PostMapping
    public Step create(@RequestBody Step step) {
        return stepService.saveStep(step);
    }

    @GetMapping("/{id}")
    public Step get(@PathVariable Long id) {
        return stepService.getStep(id).orElse(null);
    }

    @GetMapping
    public List<Step> getAll() {
        return stepService.getAllSteps();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        stepService.deleteStep(id);
    }
}
