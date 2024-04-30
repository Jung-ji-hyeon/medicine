package com.example.medicine.medicine.controller;

import com.example.medicine.medicine.cache.MedicineRedisTemplateService;
import com.example.medicine.medicine.dto.MedicineDto;
import com.example.medicine.medicine.service.MedicineRepositoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MedicineController {

    private final MedicineRepositoryService medicineRepositoryService;
    private final MedicineRedisTemplateService medicineRedisTemplateService;

    // 데이터 초기 셋팅을 위한 임시 메서드
    @GetMapping("/redis/save")
    public String save() {

        List<MedicineDto> medicineDtoList = medicineRepositoryService.findAll()
                .stream().map(medicine -> MedicineDto.builder()
                        .id(medicine.getId())
                        .medicineName(medicine.getMedicineName())
                        .medicineNumber(medicine.getMedicineNumber())
                        .medicineAddress(medicine.getMedicineAddress())
                        .latitude(medicine.getLatitude())
                        .longitude(medicine.getLongitude())
                        .build())
                .collect(Collectors.toList());

        medicineDtoList.forEach(medicineRedisTemplateService::save);

        return "success";
    }

}
