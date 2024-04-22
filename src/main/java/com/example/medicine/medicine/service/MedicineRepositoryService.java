package com.example.medicine.medicine.service;

import com.example.medicine.medicine.entity.Medicine;
import com.example.medicine.medicine.repository.MedicineRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class MedicineRepositoryService {

    private final MedicineRepository medicineRepository;

    @Transactional
    public void updateAddress(Long id, String address) {
        Medicine entity = medicineRepository.findById(id).orElse(null);

        if (Objects.isNull(entity)) {
            log.error("[MedicineRepositoryService updateAddress] not found id : {}", id);
            return;
        }

        entity.changeMedicineAddress(address);
    }

    @Transactional(readOnly = true)
    public List<Medicine> findAll() {
        return medicineRepository.findAll();
    }
}
