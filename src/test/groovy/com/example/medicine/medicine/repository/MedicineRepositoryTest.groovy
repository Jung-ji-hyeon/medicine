package com.example.medicine.medicine.repository

import com.example.medicine.AbstractIntegrationContainerBaseTest
import com.example.medicine.medicine.entity.Medicine
import org.springframework.beans.factory.annotation.Autowired

import java.time.LocalDateTime

class MedicineRepositoryTest extends AbstractIntegrationContainerBaseTest {

    @Autowired
    private MedicineRepository medicineRepository

    // 각 테스트 메소드 시작 전에 데이터를 정리
    def setup() {
        medicineRepository.deleteAll()
    }

    def "MedicineRepository save"() {
        given:
        String address = "경기도 의정부시 시민로122번길 9-18 (의정부동)"
        String name = "GS25 의정신곡점"
        String number = "031-872-1599"
        double latitude = 36.11
        double longitude = 128.11

        def medicine = Medicine.builder()
                .medicineAddress(address)
                .medicineName(name)
                .medicineNumber(number)
                .latitude(latitude)
                .longitude(longitude)
                .build()

        when:
        def result = medicineRepository.save(medicine)

        then:
        result.getMedicineAddress() == address
        result.getMedicineName() == name
        result.getMedicineNumber() == number
        result.getLatitude() == latitude
        result.getLongitude() == longitude

    }

    def "MedicineRepository saveAll"() {
        given:
        String address = "경기도 의정부시 시민로122번길 9-18 (의정부동)"
        String name = "GS25 의정신곡점"
        String number = "031-872-1599"
        double latitude = 36.11
        double longitude = 128.11

        def medicine = Medicine.builder()
                .medicineAddress(address)
                .medicineName(name)
                .medicineNumber(number)
                .latitude(latitude)
                .longitude(longitude)
                .build()

        when:
        medicineRepository.saveAll(Arrays.asList(medicine))
        def result = medicineRepository.findAll()

        then:
        result.size() == 1

    }

    def "BaseTimeEntity 등록"() {
        given:
        LocalDateTime now = LocalDateTime.now()
        String address = "경기도 의정부시 시민로122번길 9-18 (의정부동)"
        String name = "GS25 의정신곡점"

        def medicine = Medicine.builder()
                .medicineAddress(address)
                .medicineName(name)
                .build()

        when:
        medicineRepository.save(medicine)
        def result = medicineRepository.findAll()

        then:
        result.get(0).getCreatedDate().isAfter(now)
        result.get(0).getModifiedDate().isAfter(now)
    }


}