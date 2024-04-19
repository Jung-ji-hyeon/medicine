package com.example.medicine.medicine.service

import com.example.medicine.AbstractIntegrationContainerBaseTest
import com.example.medicine.medicine.entity.Medicine
import com.example.medicine.medicine.repository.MedicineRepository
import org.springframework.beans.factory.annotation.Autowired

class MedicineRepositoryServiceTest extends AbstractIntegrationContainerBaseTest {

    @Autowired
    private MedicineRepositoryService medicineRepositoryService

    @Autowired
    private MedicineRepository medicineRepository

    def setup() {
        medicineRepository.deleteAll()
    }

    def "MedicineRepository update - dirty checking success"() {
        given:
        String inputAddress = "경기도 의정부시 시민로122번길"
        String modifiedAddress = "경기도 의정부시 회룡로117번길"
        String name = "GS25 의정신곡점"

        def medicine = Medicine.builder()
                .medicineAddress(inputAddress)
                .medicineName(name)
                .build()

        when:
        def entity = medicineRepository.save(medicine)
        medicineRepositoryService.updateAddress(entity.getId(), modifiedAddress)

        def result = medicineRepository.findAll()

        then:
        result.get(0).getMedicineAddress() == modifiedAddress


    }

}