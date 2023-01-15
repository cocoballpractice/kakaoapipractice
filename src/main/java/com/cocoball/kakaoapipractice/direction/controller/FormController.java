package com.cocoball.kakaoapipractice.direction.controller;

import com.cocoball.kakaoapipractice.direction.dto.InputDto;
import com.cocoball.kakaoapipractice.pharmacy.service.PharmacyRecommendationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Controller
@RequiredArgsConstructor
public class FormController {

    private final PharmacyRecommendationService pharmacyRecommendationService;

    @GetMapping("/")
    public String main() {
        return "main";
    }

    @PostMapping("/search")
    public ModelAndView postDirection(@ModelAttribute InputDto inputDto) {

        ModelAndView mv = new ModelAndView();
        mv.setViewName("output");
        mv.addObject("outputFormList", pharmacyRecommendationService.recommendationPharmacyList(inputDto.getAddress()));

        return mv;
    }

}
