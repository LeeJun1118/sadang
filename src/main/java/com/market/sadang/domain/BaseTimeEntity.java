package com.market.sadang.domain;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

//lombok 어노테이션
//클래스 내 모든 필드의 Get 매소드 자동 생성
@Getter

//모든 Entity 의 상위 클래스가 됨
//JPA Entity 클래스들이 BaseTimeEntity 을 상속할 경우 필드들(createDate,modifiedDate)도 컬럼으로 인식
@MappedSuperclass

//BaseTimeEntity 클래스에 Auditing 기능을 포함
@EntityListeners(AuditingEntityListener.class)
public class BaseTimeEntity {

    //Entity 가 생성되어 저장될 때 시간이 자동 저장
    @CreatedDate
    private LocalDateTime createdDate;

    //조회한 Entity 값을 변경할 때 시간이 자동 저장
    @LastModifiedDate
    private LocalDateTime modifiedDate;
}
