package com.programming.dmaker.service;

import com.programming.dmaker.dto.CreateDeveloper;
import com.programming.dmaker.dto.DeveloperDetailDto;
import com.programming.dmaker.dto.DeveloperDto;
import com.programming.dmaker.dto.EditDeveloper;
import com.programming.dmaker.entity.Developer;
import com.programming.dmaker.repository.DeveloperRepository;
import com.programming.dmaker.type.DeveloperLevel;
import exception.DMakerException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.util.List;
import java.util.stream.Collectors;

import static exception.DMakerErrorCode.*;


@Service
@RequiredArgsConstructor// 자동으로 생성자 만들어줌
public class DMakerService {
    private final DeveloperRepository developerRepository;//자동으로 인젝션 해줌
    private final EntityManager em;
    @Transactional
    public CreateDeveloper.Response createDeveloper(CreateDeveloper.Request request) {
        validationCreateDeveloperRequest(request);
//        EntityTransaction transaction = em.getTransaction();
//        try {
//            transaction.begin();
//        business logic start
            Developer developer = Developer.builder()
                    .developerLevel(request.getDeveloperLevel())
                    .developerSkillType(request.getDeveloperSkillType())
                    .experienceYears(request.getExperienceYears())
                    .memberID(request.getMemberId())
                    .name(request.getName())
                    .age(request.getAge())
                    .build();
            developerRepository.save(developer);//db에 저장 -> controller 에서 사용
            return CreateDeveloper.Response.fromEntity(developer);

//        aop기반으로 동작 중복코드를 제거 할수 잇음
//            transaction.commit();
//        }
//        catch (Exception e){
//            transaction.rollback();
//            throw e;
//        }

    }

    private void validationCreateDeveloperRequest(CreateDeveloper.Request request) {
//        business validation
        validateDeveloperLevel(request.getDeveloperLevel(), request.getExperienceYears());

        developerRepository.findByMemberID(request.getMemberId())
                .ifPresent((developer -> {
                    throw new DMakerException(DUPLICATED_MEMBER_ID);
                }));

    }

    public List<DeveloperDto> getAllDevelopers() {

        return developerRepository.findAll()
                .stream().map(DeveloperDto::fromEntity).collect(Collectors.toList());
    }

    public DeveloperDetailDto getDetailDeveloper(String memberId) {
        return developerRepository.findByMemberID(memberId).map(DeveloperDetailDto::fromEntity).orElseThrow(()->new DMakerException(NO_DEVELOPER));
    }
    @Transactional
    public DeveloperDetailDto EditDeveloper(String memberId, EditDeveloper.Request request) {
        validateDeveloperLevel(request.getDeveloperLevel(), request.getExperienceYears());
        Developer developer=developerRepository.findByMemberID(memberId).orElseThrow(()->new DMakerException(NO_DEVELOPER));
        developer.setDeveloperLevel(request.getDeveloperLevel());
        developer.setDeveloperSkillType(request.getDeveloperSkillType());
        developer.setExperienceYears(request.getExperienceYears());
        return DeveloperDetailDto.fromEntity(developer);

    }




    private void validateDeveloperLevel(DeveloperLevel developerLevel, Integer experienceYears) {
        if(developerLevel ==DeveloperLevel.SENIER&& experienceYears <10)
        {
            throw new DMakerException(LEVEL_EXPERIENCE_YEARS_NOT_MATCHED);
        }
        if(developerLevel ==DeveloperLevel.JUNGNIOR
                &&(experienceYears <4|| experienceYears >10)){
            throw new DMakerException(LEVEL_EXPERIENCE_YEARS_NOT_MATCHED);
        }
        if(developerLevel ==DeveloperLevel.JUNIOR&& experienceYears >4){
            throw new DMakerException(LEVEL_EXPERIENCE_YEARS_NOT_MATCHED);
        }
    }
}
