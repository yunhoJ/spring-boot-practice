package com.programming.dmaker.repository;

import com.programming.dmaker.entity.Developer;
import com.programming.dmaker.entity.RetiredDeveloper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

//엔티티를 저장해주는 리포지토리
//developer 를관리해주는 인터페이스
//jpa기술을 활용함
@Repository //repository 타입의 Bean으로 등록
public interface RetiredDeveloperRepository extends JpaRepository<RetiredDeveloper,Long> {


}
