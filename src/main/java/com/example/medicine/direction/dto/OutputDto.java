package com.example.medicine.direction.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OutputDto {

    private String medicineName;     // 비상의약품업소 이름
    private String medicineNumber;   // 비상의약품업소 전화번호
    private String medicineAddress;  // 비상의약품업소 주소
    private String directionUrl;     // 비상의약품업소 길안내
    private String roadViewUrl;      // 비상의약품업소 로드뷰
    private String distance;         // 사용자와 비상의약품업소의 거리
}
