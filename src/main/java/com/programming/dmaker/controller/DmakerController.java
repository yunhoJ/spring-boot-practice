package com.programming.dmaker.controller;

import com.programming.dmaker.dto.*;
import com.programming.dmaker.service.DMakerService;
import exception.DMakerException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
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
    @DeleteMapping("/developers/{memberId}")
    public DeveloperDetailDto detailDeveloper(@PathVariable String memberId){
        return dMakerService.DeleteDeverloper(memberId);
    }


    //    예외처리
    @ResponseStatus(value = HttpStatus.CONFLICT)//400대 상태 반환
    @ExceptionHandler(DMakerException.class)
    public DmakerErrorResponse handlerException(DMakerException e, HttpServletRequest request)
    {
        log.error("errorCode:{},url:{},message:{}",e.getDMakerErrorCode(),request.getRequestURI(),e.getDetailMessage() );
        return DmakerErrorResponse.builder()
                .errorCode(e.getDMakerErrorCode())
                .errorMessage(e.getMessage()).build();
    }
}
