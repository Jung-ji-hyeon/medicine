package com.example.medicine.medicine.cache

import com.example.medicine.AbstractIntegrationContainerBaseTest
import com.example.medicine.medicine.dto.MedicineDto
import org.springframework.beans.factory.annotation.Autowired

class MedicineRedisTemplateServiceTest extends AbstractIntegrationContainerBaseTest {

    @Autowired
    private MedicineRedisTemplateService medicineRedisTemplateService

    def setup() {
        medicineRedisTemplateService.findAll()
            .forEach(dto -> {
                medicineRedisTemplateService.delete(dto.getId())
            })
    }

    def "save success"() {
        given:
        String medicineName = "name"
        String medicineAddress = "address"
        MedicineDto dto = MedicineDto.builder()
                .id(1L)
                .medicineName(medicineName)
                .medicineAddress(medicineAddress)
                .build()

        when:
        medicineRedisTemplateService.save(dto)
        List<MedicineDto> result = medicineRedisTemplateService.findAll()

        then:
        result.size() == 1
        result.get(0).id == 1L
        result.get(0).medicineName == medicineName
        result.get(0).medicineAddress == medicineAddress
    }

    def "success fail"() {
        given:
        MedicineDto dto = MedicineDto.builder().build()

        when:
        medicineRedisTemplateService.save(dto)
        List<MedicineDto> result = medicineRedisTemplateService.findAll()

        then:
        result.size() == 0
    }

    def "delete"() {
        given:
        String medicineName = "name"
        String medicineAddress = "address"
        MedicineDto dto = MedicineDto.builder()
                .id(1L)
                .medicineName(medicineName)
                .medicineAddress(medicineAddress)
                .build()

        when:
        medicineRedisTemplateService.save(dto)
        medicineRedisTemplateService.delete(dto.getId())
        def result = medicineRedisTemplateService.findAll()

        then:
        result.size() == 0

    }

}