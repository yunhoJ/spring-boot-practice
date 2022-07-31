package com.programming.dmaker.dto;

import com.programming.dmaker.entity.Developer;
import com.programming.dmaker.type.DeveloperLevel;
import com.programming.dmaker.type.DeveloperSkillType;
import lombok.*;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeveloperDetailDto {
    private DeveloperLevel developerLevel;
    private DeveloperSkillType developerSkillType;
    private Integer experienceYears;
    private String name;
    private String memberId;
    private Integer age;

    public static DeveloperDetailDto fromEntity(Developer developer){
        return DeveloperDetailDto.builder()
                .developerLevel(developer.getDeveloperLevel())
                .developerSkillType(developer.getDeveloperSkillType())
                .experienceYears(developer.getExperienceYears())
                .name(developer.getName())
                .age(developer.getAge())
                .memberId(developer.getMemberID()).build();

    }
}
