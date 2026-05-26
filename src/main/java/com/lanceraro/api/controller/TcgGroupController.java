package com.lanceraro.api.controller;

import com.lanceraro.api.model.TcgGroup;
import com.lanceraro.api.repository.TcgGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tcggroups")
public class TcgGroupController {

    @Autowired
    private TcgGroupRepository tcgGroupRepository;

    @GetMapping
    public List<TcgGroup> getAll() { return tcgGroupRepository.findAll(); }

    @GetMapping("/{id}")
    public TcgGroup getById(@PathVariable Long id) { return tcgGroupRepository.findById(id).orElse(null); }

    @PostMapping
    public TcgGroup add(@RequestBody TcgGroup tcgGroup) { return tcgGroupRepository.save(tcgGroup); }
}
