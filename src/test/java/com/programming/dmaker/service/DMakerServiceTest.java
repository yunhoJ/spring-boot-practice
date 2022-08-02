package com.programming.dmaker.service;

import com.programming.dmaker.dto.CreateDeveloper;
import com.programming.dmaker.dto.DeveloperDetailDto;
import com.programming.dmaker.dto.DeveloperDto;
import com.programming.dmaker.entity.Developer;
import com.programming.dmaker.repository.DeveloperRepository;
import com.programming.dmaker.repository.RetiredDeveloperRepository;
import com.programming.dmaker.type.DeveloperLevel;
import com.programming.dmaker.type.DeveloperSkillType;
import com.programming.dmaker.type.StatusCode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static com.programming.dmaker.type.DeveloperLevel.SENIER;
import static com.programming.dmaker.type.DeveloperSkillType.*;
import static com.programming.dmaker.type.StatusCode.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

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
    @Test
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
    }//격리성 떨어짐 ->mocking

    @Mock
    private DeveloperRepository developerRepository;
    @Mock
    private RetiredDeveloperRepository retiredDeveloperRepository;
    //두개의 가상의 mock을 생성
    @InjectMocks//위 mock을 가지고 테스트
    private  DMakerService dMakerServiceMocks;
    @Test
    public void test3(){
        //mock의 동작 정의
        given(developerRepository.findByMemberID(anyString())) //아무값이 들어가면 밑의 응답을 하겠다
                .willReturn(Optional.of(Developer.builder()
                        .age(40)
                        .developerLevel(SENIER)
                        .developerSkillType(FULL_STACK)
                        .experienceYears(12)
                        .statusCode(EMPLOYED)
                        .name("name").build()));
        DeveloperDetailDto developerdetail = dMakerServiceMocks.getDetailDeveloper("memberId");
        assertEquals(SENIER,developerdetail.getDeveloperLevel());
        assertEquals(122,developerdetail.getExperienceYears());


    }

}