package com.example.medicine.direction.service

import com.example.medicine.api.dto.DocumentDto
import com.example.medicine.direction.repository.DirectionRepository
import com.example.medicine.medicine.dto.MedicineDto
import com.example.medicine.medicine.service.MedicineSearchService
import spock.lang.Specification


class DirectionServiceTest extends Specification {

    private MedicineSearchService medicineSearchService = Mock()
    private DirectionRepository directionRepository = Mock()
    private Base62Service base62Service = Mock()

    private DirectionService directionService = new DirectionService(medicineSearchService, directionRepository, base62Service)

    private List<MedicineDto> medicineList

    def setup() {
        medicineList = new ArrayList<>()
        medicineList.addAll(
                MedicineDto.builder()
                        .id(1L)
                        .medicineName("GS25 의정호국점")
                        .medicineAddress("경기도 의정부시 호국로 1115-14")
                        .latitude(202681.909773400)
                        .longitude(471177.568709900)
                        .build(),
                MedicineDto.builder()
                        .id(2L)
                        .medicineName("씨유 의정부경민대점")
                        .medicineAddress("경기도 의정부시 호국로1111번길 42")
                        .latitude(202681.909773000)
                        .longitude(471177.568709000)
                        .build()
        )
    }

    def "buildDirectionList - 결과 값이 거리 순으로 정렬이 되는지 확인"() {
        given:
        def addressName = "경기도 의정부시 호국로 1147"
        double inputLatitude = 202681.909773444
        double inputLongitude = 471177.568709956

        def documentDto = DocumentDto.builder()
                .addressName(addressName)
                .latitude(inputLatitude)
                .longitude(inputLongitude)
                .build()

        when:
        medicineSearchService.searchMedicineDtoList() >> medicineList

        def results = directionService.buildDirectionList(documentDto)

        then:
        results.size() == 2
        results.get(0).targetMedicineName == "GS25 의정호국점"
        results.get(1).targetMedicineName == "씨유 의정부경민대점"

    }

    def "buildDirectionList - 정해진 반경 10 km 내에 검색이 되는지 확인"() {
        given:
        medicineList.add(
                MedicineDto.builder()
                        .id(3L)
                        .medicineName("세븐일레븐 금오시티점")
                        .medicineAddress("경기도 의정부시 청사로47번길 12")
                        .latitude(206020.392715200)
                        .longitude(472236.910192015)
                        .build()
        )

        def addressName = "경기도 의정부시 호국로 1147"
        double inputLatitude = 202681.909773444
        double inputLongitude = 471177.568709956

        def documentDto = DocumentDto.builder()
                .addressName(addressName)
                .latitude(inputLatitude)
                .longitude(inputLongitude)
                .build()

        when:
        medicineSearchService.searchMedicineDtoList() >> medicineList

        def results = directionService.buildDirectionList(documentDto)

        then:
        results.size() == 2
        results.get(0).targetMedicineName == "GS25 의정호국점"
        results.get(1).targetMedicineName == "씨유 의정부경민대점"
    }

}