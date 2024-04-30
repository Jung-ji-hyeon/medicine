package com.example.medicine.medicine.service;

import com.example.medicine.medicine.cache.MedicineRedisTemplateService;
import com.example.medicine.medicine.dto.MedicineDto;
import com.example.medicine.medicine.entity.Medicine;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MedicineSearchService {

    private final MedicineRepositoryService medicineRepositoryService;
    private final MedicineRedisTemplateService medicineRedisTemplateService;

    public List<MedicineDto> searchMedicineDtoList() {

        // redis
        List<MedicineDto> medicineDtoList = medicineRedisTemplateService.findAll();
        if (!medicineDtoList.isEmpty()) return medicineDtoList;

        // db
        return medicineRepositoryService.findAll()
                .stream()
                .map(this::convertToMedicineDto)
                .collect(Collectors.toList());

    }

    private MedicineDto convertToMedicineDto(Medicine medicine) {

        return MedicineDto.builder()
                .id(medicine.getId())
                .medicineAddress(medicine.getMedicineAddress())
                .medicineName(medicine.getMedicineName())
                .medicineNumber(medicine.getMedicineNumber())
                .latitude(medicine.getLatitude())
                .longitude(medicine.getLongitude())
                .build();
    }
}
