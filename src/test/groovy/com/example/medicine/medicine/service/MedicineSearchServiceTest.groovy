package com.example.medicine.medicine.service

import com.example.medicine.medicine.cache.MedicineRedisTemplateService
import com.example.medicine.medicine.entity.Medicine
import org.assertj.core.util.Lists
import spock.lang.Specification

class MedicineSearchServiceTest extends Specification {

    private MedicineSearchService medicineSearchService

    private MedicineRepositoryService medicineRepositoryService = Mock()
    private MedicineRedisTemplateService medicineRedisTemplateService = Mock()

    private List<Medicine> medicineList

    def setup() {
        medicineSearchService = new MedicineSearchService(medicineRepositoryService, medicineRedisTemplateService)

        medicineList = Lists.newArrayList(
                Medicine.builder()
                        .id(1L)
                        .medicineName("GS25 의정호국점")
                        .latitude(37.74218450)
                        .longitude(127.0528316)
                        .build(),
                Medicine.builder()
                        .id(2L)
                        .medicineName("GS25 의정부점")
                        .latitude(37.74255515)
                        .longitude(127.0497384)
                        .build()
        )
    }

    def "레디스 장애시 DB를 이용하여 비상의약품업소 데이터 조회"() {
        when:
        medicineRedisTemplateService.findAll() >> []
        medicineRepositoryService.findAll() >> medicineList

        def result = medicineSearchService.searchMedicineDtoList()

        then:
        result.size() == 2
    }

}