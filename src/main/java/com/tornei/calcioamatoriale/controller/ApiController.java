package com.tornei.calcioamatoriale.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tornei.calcioamatoriale.service.TorneoService;

@RestController
@RequestMapping("/api")
public class ApiController {

    @Autowired
    private TorneoService torneoService;

    @GetMapping("/torneo/{id}/classifica")
    public Map<String, Integer> getClassifica(@PathVariable Long id) {
        return torneoService.getClassifica(id);
    }
}
