package com.programming.dmaker.exception;

import lombok.Getter;

@Getter
public class DMakerException extends RuntimeException{
    private DMakerErrorCode dMakerErrorCode;
    private String detailMessage;

    public DMakerException(DMakerErrorCode dMakerErrorCode){
        super(dMakerErrorCode.getMessage());
        this.dMakerErrorCode=dMakerErrorCode;
        this.detailMessage=dMakerErrorCode.getMessage();

    }
    public DMakerException(DMakerErrorCode dMakerErrorCode,String detailMessage){
        super(detailMessage);
        this.dMakerErrorCode=dMakerErrorCode;
        this.detailMessage=detailMessage;

    }
}
