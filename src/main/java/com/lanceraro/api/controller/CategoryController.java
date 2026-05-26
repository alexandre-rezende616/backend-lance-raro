package com.lanceraro.api.controller;

import com.lanceraro.api.model.Category;
import com.lanceraro.api.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping
    public List<Category> getAll() { return categoryRepository.findAll(); }

    @GetMapping("/{id}")
    public Category getById(@PathVariable Long id) { return categoryRepository.findById(id).orElse(null); }

    @PostMapping
    public Category add(@RequestBody Category category) { return categoryRepository.save(category); }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        categoryRepository.deleteById(id);
    }
}
