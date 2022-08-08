package com.programming.dmaker.service;

import com.programming.dmaker.constant.DMakerConstant;
import com.programming.dmaker.dto.CreateDeveloper;
import com.programming.dmaker.dto.DeveloperDetailDto;
import com.programming.dmaker.dto.DeveloperDto;
import com.programming.dmaker.dto.EditDeveloper;
import com.programming.dmaker.entity.Developer;
import com.programming.dmaker.entity.RetiredDeveloper;
import com.programming.dmaker.exception.DMakerException;
import com.programming.dmaker.repository.DeveloperRepository;
import com.programming.dmaker.repository.RetiredDeveloperRepository;
import com.programming.dmaker.type.DeveloperLevel;
import com.programming.dmaker.type.StatusCode;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.programming.dmaker.constant.DMakerConstant.MAX_JUNIOR_EXPERIENCE_YEARS;
import static com.programming.dmaker.constant.DMakerConstant.MIN_SENIOR_EXPERIENCE_YEARS;
import static com.programming.dmaker.exception.DMakerErrorCode.*;


@Service
// 자동으로 생성자 만들어줌
public class DMakerService {
    private final DeveloperRepository developerRepository;//자동으로 인젝션 해줌
    private final RetiredDeveloperRepository retiredDeveloperRepository;

    public DMakerService(DeveloperRepository developerRepository, RetiredDeveloperRepository retiredDeveloperRepository) {
        this.developerRepository = developerRepository;
        this.retiredDeveloperRepository = retiredDeveloperRepository;
    }
    private Developer createDeveloperFromRequest (CreateDeveloper.Request request){
        return Developer.builder()
                .developerLevel(request.getDeveloperLevel())
                .developerSkillType(request.getDeveloperSkillType())
                .experienceYears(request.getExperienceYears())
                .statusCode(StatusCode.EMPLOYED)
                .memberID(request.getMemberId())
                .name(request.getName())
                .age(request.getAge())
                .build();
    }

    @Transactional
    public CreateDeveloper.Response createDeveloper(CreateDeveloper.Request request) {
        validationCreateDeveloperRequest(request);
//        EntityTransaction transaction = em.getTransaction();
//        try {
//            transaction.begin();
//        business logic start
            Developer developer = createDeveloperFromRequest(request);
            developerRepository.save(developer);//db에 저장 -> controller 에서 사용
            return CreateDeveloper.Response.fromEntity(developer);
////        return CreateDeveloper.Response.fromEntity(developerRepository.save(createDeveloperFromRequest(request)));// 해당 코드로 리팩토링 가능
//        aop기반으로 동작 중복코드를 제거 할수 잇음
//            transaction.commit();
//        }
//        catch (Exception e){
//            transaction.rollback();
//            throw e;
//        }

    }

    private void validationCreateDeveloperRequest(@NonNull CreateDeveloper.Request request) {//null 값이 오면 안됨
//        business validation
        validateDeveloperLevel(request.getDeveloperLevel(), request.getExperienceYears());

        developerRepository.findByMemberID(request.getMemberId())
                .ifPresent((developer -> {
                    throw new DMakerException(DUPLICATED_MEMBER_ID);
                }));

    }

    @Transactional(readOnly = true)//데이터의 변경 방지
    public List<DeveloperDto> getAllDevelopers() {

        return developerRepository.findDevelopersByStatusCodeEquals(StatusCode.EMPLOYED)
                .stream().map(DeveloperDto::fromEntity).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public DeveloperDetailDto getDetailDeveloper(String memberId) {
//        return developerRepository.findByMemberID(memberId).map(DeveloperDetailDto::fromEntity).orElseThrow(()->new DMakerException(NO_DEVELOPER));
        return DeveloperDetailDto.fromEntity(getDeveloperByMemberId(memberId));
    }
    @Transactional
    public DeveloperDetailDto EditDeveloper(String memberId, EditDeveloper.Request request) {
        validateDeveloperLevel(request.getDeveloperLevel(), request.getExperienceYears());

//        Developer developer=getDeveloperByMemberId(memberId);

        return DeveloperDetailDto.fromEntity(setDeveloperFromRequest(request, getDeveloperByMemberId(memberId)));

    }

    private Developer setDeveloperFromRequest(EditDeveloper.Request request, Developer developer) {
        developer.setDeveloperLevel(request.getDeveloperLevel());
        developer.setDeveloperSkillType(request.getDeveloperSkillType());
        developer.setExperienceYears(request.getExperienceYears());
        return developer;
    }

    private Developer getDeveloperByMemberId(String memberId){
        return developerRepository.findByMemberID(memberId).orElseThrow(()->new DMakerException(NO_DEVELOPER));
    }


//1. 매직넘버 없애는 방법 상수 이용
/*    private void validateDeveloperLevel(DeveloperLevel developerLevel, Integer experienceYears) {
        if(developerLevel ==DeveloperLevel.SENIER&& experienceYears <MIN_SENIOR_EXPERIENCE_YEARS)
        {
            throw new DMakerException(LEVEL_EXPERIENCE_YEARS_NOT_MATCHED);
        }
        if(developerLevel ==DeveloperLevel.JUNGNIOR
                &&(experienceYears <MAX_JUNIOR_EXPERIENCE_YEARS|| experienceYears >MIN_SENIOR_EXPERIENCE_YEARS)){
            throw new DMakerException(LEVEL_EXPERIENCE_YEARS_NOT_MATCHED);
        }
        if(developerLevel ==DeveloperLevel.JUNIOR&& experienceYears >MAX_JUNIOR_EXPERIENCE_YEARS){
            throw new DMakerException(LEVEL_EXPERIENCE_YEARS_NOT_MATCHED);
        }
    }*/
//2. enum 이용
/*private void validateDeveloperLevel(DeveloperLevel developerLevel, Integer experienceYears) {

    if(experienceYears<developerLevel.getMinExperienceYears() || experienceYears>developerLevel.getMaxExperienceYears()){
        throw new DMakerException(LEVEL_EXPERIENCE_YEARS_NOT_MATCHED);
    }
}*/
//    enum에 검증 로직까지 같이 있음
    private void validateDeveloperLevel(DeveloperLevel developerLevel, Integer experienceYears) {
        developerLevel.ValidateExperienceYears(experienceYears);


    }
    @Transactional
    public DeveloperDetailDto DeleteDeverloper(String memberId) {
//        1. EMPLOYED -> RETIRED
//        2. save into RetiredDeveloper
        Developer developer = developerRepository.findByMemberID(memberId)
                .orElseThrow(()->new DMakerException(NO_DEVELOPER));
        developer.setStatusCode(StatusCode.RETIRED);
        RetiredDeveloper retiredDeveloper=RetiredDeveloper.builder()
                .memberID(memberId)
                .name(developer.getName())
                .build();
        retiredDeveloperRepository.save(retiredDeveloper);
        return DeveloperDetailDto.fromEntity(developer);

    }

}
