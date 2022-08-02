package com.programming.dmaker.dto;

import com.programming.dmaker.exception.DMakerErrorCode;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DmakerErrorResponse {
    private DMakerErrorCode errorCode;
    private String errorMessage;
}
