package com.example.medicine.direction.service;

import com.example.medicine.api.dto.DocumentDto;
import com.example.medicine.direction.entity.Direction;
import com.example.medicine.direction.repository.DirectionRepository;
import com.example.medicine.medicine.service.MedicineSearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DirectionService {

    private static final int MAX_SEARCH_COUNT = 5; // 비상의약품업소 최대 검색 횟수
    private static final double RADIUS_KM = 10.0; // 반경 10km
    private static final String DIRECTION_BASE_URL = "https://map.kakao.com/link/map/";


    private final MedicineSearchService medicineSearchService;
    private final DirectionRepository directionRepository;
    private final Base62Service base62Service;

    @Transactional
    public List<Direction> saveAll(List<Direction> directionList) {
        if (CollectionUtils.isEmpty(directionList)) return Collections.emptyList();

        return directionRepository.saveAll(directionList);
    }

    public String findDirectionUrlById(String encodedId) {
        Long decodedId = base62Service.decodeDirectionId(encodedId);
        Direction direction = directionRepository.findById(decodedId).orElse(null);

        String params = String.join(",", direction.getTargetMedicineName(),
                String.valueOf(direction.getTargetLatitude()), String.valueOf(direction.getTargetLongitude()));

        String result = UriComponentsBuilder.fromHttpUrl(DIRECTION_BASE_URL + params).toUriString();

        return result;

    }

    public List<Direction> buildDirectionList(DocumentDto documentDto) {

        if (Objects.isNull(documentDto)) return Collections.emptyList();

        // 비상의약품업소 데이터 조회
        return medicineSearchService.searchMedicineDtoList()
                .stream().map(medicineDto ->
                        Direction.builder()
                                .inputAddress(documentDto.getAddressName())
                                .inputLatitude(documentDto.getLatitude())
                                .inputLongitude(documentDto.getLongitude())
                                .targetMedicineName(medicineDto.getMedicineName())
                                .targetMedicineNumber(medicineDto.getMedicineNumber())
                                .targetAddress(medicineDto.getMedicineAddress())
                                .targetLatitude(medicineDto.getLatitude())
                                .targetLongitude(medicineDto.getLongitude())
                                .distance(
                                        calculateDistance(documentDto.getLatitude(), documentDto.getLongitude(),
                                                medicineDto.getLatitude(), medicineDto.getLongitude())
                                )
                                .build())
                .filter(direction -> direction.getDistance() <= RADIUS_KM)
                .sorted(Comparator.comparing(Direction::getDistance))
                .limit(MAX_SEARCH_COUNT)
                .collect(Collectors.toList());
    }

    // 고객이 위치한 위도 및 경도, 비상의약품업소 위도 및 경도
    // Haversine formula
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        lat1 = Math.toRadians(lat1);
        lon1 = Math.toRadians(lon1);
        lat2 = Math.toRadians(lat2);
        lon2 = Math.toRadians(lon2);

        double earthRadius = 6371; //Kilometers
        return earthRadius * Math.acos(Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon1 - lon2));
    }
}
