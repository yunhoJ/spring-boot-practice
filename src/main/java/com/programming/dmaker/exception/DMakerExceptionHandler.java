package com.programming.dmaker.exception;

import com.programming.dmaker.dto.DmakerErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

import static com.programming.dmaker.exception.DMakerErrorCode.*;

//각 컨트롤러에 조언해주는 역활의 클래스
@RestControllerAdvice//bean으로 등록을 하기 위한 어노테이션
@Slf4j
public class DMakerExceptionHandler {
    @ExceptionHandler(DMakerException.class)
    public DmakerErrorResponse handleException (DMakerException e, HttpServletRequest request){
        log.error("errorcode : {}, url : {}, message : {}",e.getDMakerErrorCode(),request.getRequestURI(),e.getDetailMessage());
        return DmakerErrorResponse.builder()
                .errorCode(e.getDMakerErrorCode())
                .errorMessage(e.getDetailMessage())
                .build();
    }
    @ExceptionHandler(value = {
            HttpRequestMethodNotSupportedException.class,//정해진 get post가아닌 다른 메서드를 요청하면 발생함
            MethodArgumentNotValidException.class //자바 bean벨리데이션 했을때 문제가 발생하면 발생하는 에러
    })
    public DmakerErrorResponse handlebadRequest(Exception e ,HttpServletRequest request)
    {
        log.error("url : {}, message : {}",request.getRequestURI(),e.getMessage());
        return DmakerErrorResponse.builder()
                .errorCode(INVALID_REQUEST)
                .errorMessage(INVALID_REQUEST.getMessage())
                .build();
    }
    //최후의 에러
    @ExceptionHandler(Exception.class)
    public DmakerErrorResponse handleException(Exception e,HttpServletRequest request){
        log.error("url : {}, message : {}",request.getRequestURI(),e.getMessage());
        return DmakerErrorResponse.builder()
                .errorCode(INTERNAL_SERVER_ERROR)
                .errorMessage(INTERNAL_SERVER_ERROR.getMessage())
                .build();
    }
}
