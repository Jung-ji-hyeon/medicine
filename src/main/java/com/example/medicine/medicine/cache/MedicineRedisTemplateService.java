package com.example.medicine.medicine.cache;

import com.example.medicine.medicine.dto.MedicineDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class MedicineRedisTemplateService {

    private static final String CACHE_KEY = "MEDICINE";
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;
    private HashOperations<String, String, String> hashOperations;

    @PostConstruct
    public void init() {
        this.hashOperations = redisTemplate.opsForHash();
    }

    public void save(MedicineDto medicineDto) {
        if (Objects.isNull(medicineDto) || Objects.isNull(medicineDto.getId())) {
            log.error("values must not be null");
            return;
        }
        try {
            hashOperations.put(CACHE_KEY, medicineDto.getId().toString(), serializeMedicineDto(medicineDto));
            log.info("[MedicineRedisTemplateService save success] id : {}", medicineDto.getId());
        } catch (Exception e) {
            log.error("[MedicineRedisTemplateService save error] {} ", e.getMessage());
        }
    }

    public List<MedicineDto> findAll() {
        try {
            List<MedicineDto> list = new ArrayList<>();
            for (String value : hashOperations.entries(CACHE_KEY).values()) {
                MedicineDto medicineDto = deserializeMedicineDto(value);
                list.add(medicineDto);
            }
            return list;

        } catch (Exception e) {
            log.error("[MedicineRedisTemplateService findAll error] : {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    public void delete(Long id) {
        hashOperations.delete(CACHE_KEY, String.valueOf(id));
        log.info("[MedicineRedisTemplateService delete] : {} ", id);
    }

    private String serializeMedicineDto(MedicineDto medicineDto) throws JsonProcessingException {
        return objectMapper.writeValueAsString(medicineDto);
    }

    private MedicineDto deserializeMedicineDto(String value) throws JsonProcessingException {
        return objectMapper.readValue(value, MedicineDto.class);
    }

}
