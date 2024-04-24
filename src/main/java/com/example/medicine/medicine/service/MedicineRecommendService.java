package com.example.medicine.medicine.service;

import com.example.medicine.api.dto.DocumentDto;
import com.example.medicine.api.dto.KakaoApiResponseDto;
import com.example.medicine.api.service.KakaoAddressSearchService;
import com.example.medicine.direction.dto.OutputDto;
import com.example.medicine.direction.entity.Direction;
import com.example.medicine.direction.service.DirectionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MedicineRecommendService {

    private final KakaoAddressSearchService kakaoAddressSearchService;
    private final DirectionService directionService;
    public List<OutputDto> recommendMedicineList(String address) {

        KakaoApiResponseDto kakaoApiResponseDto = kakaoAddressSearchService.requestAddressSearch(address);

        if (Objects.isNull(kakaoApiResponseDto) || CollectionUtils.isEmpty(kakaoApiResponseDto.getDocumentList())) {
            log.error("[MedicineRecommendService recommendMedicineList] fail, address : {}", address);
            return Collections.emptyList();
        }

        DocumentDto documentDto = kakaoApiResponseDto.getDocumentList().get(0);

        List<Direction> directionList = directionService.buildDirectionList(documentDto);

        return directionService.saveAll(directionList)
                .stream()
                .map(this::convertToOutputDto)
                .collect(Collectors.toList());

    }

    private OutputDto convertToOutputDto(Direction direction) {
        return OutputDto.builder()
                .medicineAddress(direction.getTargetAddress())
                .medicineNumber(direction.getTargetMedicineNumber())
                .medicineName(direction.getTargetMedicineName())
                .directionUrl("")
                .roadViewUrl("")
                .distance(String.format("%.3f KM", direction.getDistance()))
                .build();
    }
}
