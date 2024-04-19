package com.example.medicine.medicine.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "medicine")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Medicine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String medicineName;
    private String medicineAddress;
    private String medicineNumber;
    private double latitude;
    private double longitude;

    public void changeMedicineAddress(String address) {
        this.medicineAddress = address;
    }
}
