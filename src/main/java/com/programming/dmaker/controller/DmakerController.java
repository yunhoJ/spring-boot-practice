package com.programming.dmaker.controller;

import com.programming.dmaker.dto.CreateDeveloper;
import com.programming.dmaker.dto.DeveloperDetailDto;
import com.programming.dmaker.dto.DeveloperDto;
import com.programming.dmaker.dto.EditDeveloper;
import com.programming.dmaker.entity.Developer;
import com.programming.dmaker.service.DMakerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
//사용자 입력이 최초로 받아지는곳

@Slf4j
@RestController // DMakerController RestController 타입의 Bean 등록
@RequiredArgsConstructor
public class DmakerController {
    private final DMakerService dMakerService;

    /*
     *        DMakerController(Bean)     DMakerService(Bean)       DeveloperRepository(Bean)
     * ======================Spring Application Context==============================================위에 동작
     *  service 를 controller에게 넣어준다 private final DMakerService dMakerService;
     *
     */
    @GetMapping("/developers")
    public List<DeveloperDto> getAllDevelopers() {
        log.info("GET /developers");
        return dMakerService.getAllDevelopers();
//        uu
    }
    @GetMapping("/developers/{memberId}")
    public DeveloperDetailDto getDeveloper(@PathVariable String memberId){
        log.info("GET /developer");
        return dMakerService.getDetailDeveloper(memberId);
    }

    @PostMapping("/create-developer")
    public CreateDeveloper.Response createDeveloper(
            @Valid//유효성 검사
            @RequestBody CreateDeveloper.Request request)
    {
        log.info("request : {}", request);
        return dMakerService.createDeveloper(request);


    }

    //수정 put 은 모든 정보 수정 patch 는 특정 정보 수정
    @PutMapping("/developers/{memberId}")
    public DeveloperDetailDto editDeveloper(@PathVariable String memberId, @Valid @RequestBody EditDeveloper.Request request){
        return dMakerService.EditDeveloper(memberId,request);
    }
}
