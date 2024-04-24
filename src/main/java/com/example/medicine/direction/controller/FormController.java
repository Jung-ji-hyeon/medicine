package com.example.medicine.direction.controller;

import com.example.medicine.direction.dto.InputDto;
import com.example.medicine.medicine.service.MedicineRecommendService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
public class FormController {

    private final MedicineRecommendService medicineRecommendService;

    @GetMapping("/")
    public String main() {
        return "main";
    }

    @PostMapping("/search")
    public ModelAndView postDirection(@ModelAttribute InputDto inputDto) {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("result");
        modelAndView.addObject("resultFormList", medicineRecommendService.recommendMedicineList(inputDto.getAddress()));

        return modelAndView;
    }
}
