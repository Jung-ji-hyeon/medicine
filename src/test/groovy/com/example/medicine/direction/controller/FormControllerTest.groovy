package com.example.medicine.direction.controller

import com.example.medicine.direction.dto.OutputDto
import com.example.medicine.medicine.service.MedicineRecommendService
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

class FormControllerTest extends Specification {

    private MockMvc mockMvc
    private MedicineRecommendService medicineRecommendService = Mock()
    private List<OutputDto> outputDtoList

    def setup() {
        // FormController MockMvc 객체로 만든다.
        mockMvc = MockMvcBuilders.standaloneSetup(new FormController(medicineRecommendService))
            .build()

        outputDtoList = new ArrayList<>()
        outputDtoList.addAll(
                OutputDto.builder()
                        .medicineName("medicine1")
                        .build(),
                OutputDto.builder()
                        .medicineName("medicine2")
                        .build()
        )
    }

    def "GET /"() {
        expect:
        // FormController 의 "/" URI를 get 방식으로 호출
        mockMvc.perform(get("/"))
            .andExpect(handler().handlerType(FormController.class))
            .andExpect(handler().methodName("main"))
            .andExpect(status().isOk())
            .andExpect(view().name("main"))
            .andDo(log())
    }

    def "POST /search"() {

        given:
        String inputAddress = "경기도 의정부시 효자로"

        when:
        def resultActions = mockMvc.perform(post("/search")
                .param("address", inputAddress))

        then:
        1 * medicineRecommendService.recommendMedicineList(argument -> {
            assert argument == inputAddress // mock 객체의 argument 검증
        }) >> outputDtoList

        resultActions
                .andExpect(status().isOk())
                .andExpect(view().name("result"))
                .andExpect(model().attributeExists("resultFormList"))  // model에 resultFormList key가 존재하는지 확인
                .andExpect(model().attribute("resultFormList", outputDtoList))
                .andDo(print())
    }





}