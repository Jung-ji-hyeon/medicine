package com.example.medicine.medicine.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MedicineDto {

    private Long id;

    private String medicineName;
    private String medicineAddress;
    private String medicineNumber;
    private double latitude;
    private double longitude;
}
