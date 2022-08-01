package com.programming.dmaker.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum StatusCode {
    EMPLOYED("고용"),RETIRED("퇴직");
    private final  String description ;
}
