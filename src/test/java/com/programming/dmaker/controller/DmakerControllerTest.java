package com.programming.dmaker.controller;

import com.programming.dmaker.dto.DeveloperDto;
import com.programming.dmaker.entity.Developer;
import com.programming.dmaker.service.DMakerService;
import com.programming.dmaker.type.DeveloperLevel;
import com.programming.dmaker.type.DeveloperSkillType;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

//컨트롤러 테스트
@WebMvcTest(DmakerController.class)//해당 컨트롤러만 올려준다.
class DmakerControllerTest {
    @Autowired
    private MockMvc mockMvc;//호출을 가상으로 만들어줌

    @MockBean
    private DMakerService dMakerService;//가짜 빈 생성

    private MediaType contentType=new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            StandardCharsets.UTF_8);


    @Test
    void getAllDevelopers()throws  Exception{
        DeveloperDto developerFrontend = DeveloperDto.builder()
                .developerSkillType(DeveloperSkillType.FRONT_END)
                .developerLevel(DeveloperLevel.JUNIOR)
                .memberId("memberId1_Front")
                .build();
        DeveloperDto developerBackend = DeveloperDto.builder()
                .developerSkillType(DeveloperSkillType.BACK_END)
                .developerLevel(DeveloperLevel.SENIER)
                .memberId("memberId2_backend")
                .build();
        given(dMakerService.getAllDevelopers())
                .willReturn(Arrays.asList(developerFrontend,developerBackend));

        mockMvc.perform(get("/developers").contentType(contentType))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(

                        jsonPath("$.[0].developerSkillType",

                                is(DeveloperSkillType.FRONT_END.name()))
//
                )
                .andExpect(
                        jsonPath("$.[1].developerSkillType",
                                is(DeveloperSkillType.BACK_END.name()))
//
                );
    }




}