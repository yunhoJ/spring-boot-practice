package com.programming.dmaker.type;

import com.programming.dmaker.constant.DMakerConstant;
import com.programming.dmaker.exception.DMakerErrorCode;
import com.programming.dmaker.exception.DMakerException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.function.Function;

import static com.programming.dmaker.constant.DMakerConstant.MAX_JUNIOR_EXPERIENCE_YEARS;
import static com.programming.dmaker.constant.DMakerConstant.MIN_SENIOR_EXPERIENCE_YEARS;

@AllArgsConstructor
@Getter
public enum DeveloperLevel {
/*    NEW("신입 개발자",0,0),JUNIOR("주니어 개발자",1, MAX_JUNIOR_EXPERIENCE_YEARS),JUNGNIOR("중니어 개발자",MAX_JUNIOR_EXPERIENCE_YEARS+1,MIN_SENIOR_EXPERIENCE_YEARS-1),SENIER("시니어 개발자",MIN_SENIOR_EXPERIENCE_YEARS,30);

    private final String description;
    private final Integer minExperienceYears;
    private final Integer maxExperienceYears;*/


    NEW("신입 개발자",years->years==0),
    JUNIOR("주니어 개발자",years->years<=MAX_JUNIOR_EXPERIENCE_YEARS),
    JUNGNIOR("중니어 개발자",years->years>MAX_JUNIOR_EXPERIENCE_YEARS&&years<MIN_SENIOR_EXPERIENCE_YEARS),
    SENIER("시니어 개발자",years->years>MIN_SENIOR_EXPERIENCE_YEARS);
    private final String description;
    private final Function<Integer,Boolean> validateFunction;

    public void  ValidateExperienceYears(Integer years){
        if(!validateFunction.apply(years)){
            throw new DMakerException(DMakerErrorCode.LEVEL_EXPERIENCE_YEARS_NOT_MATCHED);
        }
    }
}
