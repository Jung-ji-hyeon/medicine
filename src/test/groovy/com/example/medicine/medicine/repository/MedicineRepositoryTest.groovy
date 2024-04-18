package com.example.medicine.medicine.repository

import com.example.medicine.AbstractIntegrationContainerBaseTest
import com.example.medicine.medicine.entity.Medicine
import org.springframework.beans.factory.annotation.Autowired

class MedicineRepositoryTest extends AbstractIntegrationContainerBaseTest {

    @Autowired
    private MedicineRepository medicineRepository

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

}