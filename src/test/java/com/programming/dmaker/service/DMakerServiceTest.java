package com.programming.dmaker.service;

import com.programming.dmaker.dto.CreateDeveloper;
import com.programming.dmaker.dto.DeveloperDetailDto;
import com.programming.dmaker.entity.Developer;
import com.programming.dmaker.exception.DMakerErrorCode;
import com.programming.dmaker.exception.DMakerException;
import com.programming.dmaker.repository.DeveloperRepository;
import com.programming.dmaker.repository.RetiredDeveloperRepository;
import com.programming.dmaker.type.DeveloperLevel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static com.programming.dmaker.constant.DMakerConstant.MAX_JUNIOR_EXPERIENCE_YEARS;
import static com.programming.dmaker.constant.DMakerConstant.MIN_SENIOR_EXPERIENCE_YEARS;
import static com.programming.dmaker.exception.DMakerErrorCode.LEVEL_EXPERIENCE_YEARS_NOT_MATCHED;
import static com.programming.dmaker.exception.DMakerErrorCode.NO_DEVELOPER;
import static com.programming.dmaker.type.DeveloperLevel.*;
import static com.programming.dmaker.type.DeveloperSkillType.*;
import static com.programming.dmaker.type.StatusCode.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

//@SpringBootTest // 테스트를 할때도 모든 bean을 띄워 테스트를 진행하겠다 통합테스트
@ExtendWith(MockitoExtension.class)
class DMakerServiceTest {
    @Test
    public void test(){
        String result="hello"+"world!";
        assertEquals("helloworld!",result);//junit.jupiter의 가장 많이 사용 (예상값, 실제값)
    }
//    스프링에서 dMakerService,developerRepository등 각각의 빈으로 등록한뒤 사용
    @Autowired//DmakerService 인젝션
    private DMakerService dMakerService;
  /*  @Test
    public void test2(){
        dMakerService.createDeveloper(CreateDeveloper.Request.builder()
                .age(40)
                .developerLevel(SENIER)
                .developerSkillType(FULL_STACK)
                .experienceYears(12)
                .memberId("memberid")
                .name("name").build());
        List<DeveloperDto> allDevelopers = dMakerService.getAllDevelopers();
        System.out.println("======================================");
        System.out.println(allDevelopers);
        System.out.println("======================================");
    }//격리성 떨어짐 ->mocking*/

    @Mock
    private DeveloperRepository developerRepository;
    @Mock
    private RetiredDeveloperRepository retiredDeveloperRepository;
    //두개의 가상의 mock을 생성
    @InjectMocks//위 mock을 가지고 테스트
    private  DMakerService dMakerServiceMocks;
    private final Developer defaultDeveloper = Developer.builder()
            .age(40)
            .developerLevel(SENIER)
            .developerSkillType(FULL_STACK)
            .experienceYears(12)
            .statusCode(EMPLOYED)
            .name("name").build();

    @Test
    public void test3(){//일반적인 get 로직 테스트
        //mock의 동작 정의

        given(developerRepository.findByMemberID(anyString())) //아무값이 들어가면 밑의 응답을 하겠다
                .willReturn(Optional.of(defaultDeveloper));
        DeveloperDetailDto developerdetail = dMakerServiceMocks.getDetailDeveloper("member");
        assertEquals(SENIER,developerdetail.getDeveloperLevel());
        assertEquals(12,developerdetail.getExperienceYears());
    }

    @Test
    public void  createDeveloperTest(){

//        tdd - given -> when -> then
        //given
        CreateDeveloper.Request request= CreateDeveloper.Request.builder()
                .age(40)
                .developerLevel(SENIER)
                .developerSkillType(FULL_STACK)
                .memberId("memberId")
                .experienceYears(12)
                .name("name").build();
        //mocking
        given(developerRepository.findByMemberID(anyString()))
                .willReturn(Optional.empty());
        //목에 저장되는 객체 캡처//mocking db 저장, 메서드에 날라가는 파라미터의 데이터 확인
        ArgumentCaptor<Developer> captor = ArgumentCaptor.forClass(Developer.class);
        //when
        CreateDeveloper.Response developer = dMakerServiceMocks.createDeveloper(request);
        //then
        //동작을 하면 특정 목이 몇번 호출 되었는지 검증
        verify(developerRepository,times(1))
                .save(captor.capture());//developerRepository 의 save가져와 캡처

        Developer saveDeveloper=captor.getValue();//캡처된 데이터 확인
        assertEquals(SENIER,saveDeveloper.getDeveloperLevel());
        assertEquals(FULL_STACK,saveDeveloper.getDeveloperSkillType());
        assertEquals(12,saveDeveloper.getExperienceYears());



    }
    private final CreateDeveloper.Request defaultRequest= CreateDeveloper.Request.builder()
            .age(40)
            .developerLevel(SENIER)
            .developerSkillType(FULL_STACK)
            .memberId("memberId")
            .experienceYears(12)
            .name("name").build();

    private CreateDeveloper.Request getDefaultRequest(DeveloperLevel developerLevel, Integer experienbceYears){
        return CreateDeveloper.Request.builder()
                .age(40)
                .developerLevel(developerLevel)
                .developerSkillType(FULL_STACK)
                .memberId("memberId")
                .experienceYears(experienbceYears)
                .name("name").build();
    }
    @Test
    public void  createDeveloperTestFailDuplicated(){

        //exception 검증
        //given
        //mocking
        given(developerRepository.findByMemberID(anyString()))
                .willReturn(Optional.of(defaultDeveloper));
        //목에 저장되는 객체 캡처
//        ArgumentCaptor<Developer> captor = ArgumentCaptor.forClass(Developer.class);
        //when
        //then
        DMakerException dMakerException = assertThrows(DMakerException.class,()->dMakerServiceMocks.createDeveloper(defaultRequest));
        //exception의 클래스 , 해당exception을 던지게될 동작 //동작과 결과를 한번에 처리
        assertEquals(DMakerErrorCode.DUPLICATED_MEMBER_ID,dMakerException.getDMakerErrorCode());

    }
    @Test
    public void  createDeveloperTestFailLEVEL(){

//        tdd - given -> when -> then
        //given


        //목에 저장되는 객체 캡처//mocking db 저장, 메서드에 날라가는 파라미터의 데이터 확인
        //when
        //then
        DMakerException dMakerException = assertThrows(DMakerException.class,()->dMakerServiceMocks.createDeveloper(getDefaultRequest(SENIER,MIN_SENIOR_EXPERIENCE_YEARS-1)));
        assertEquals(LEVEL_EXPERIENCE_YEARS_NOT_MATCHED,dMakerException.getDMakerErrorCode());
        dMakerException = assertThrows(DMakerException.class,()->dMakerServiceMocks.createDeveloper(getDefaultRequest(JUNIOR,MAX_JUNIOR_EXPERIENCE_YEARS+1)));
        assertEquals(LEVEL_EXPERIENCE_YEARS_NOT_MATCHED,dMakerException.getDMakerErrorCode());



    }

}