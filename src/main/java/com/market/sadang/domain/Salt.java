package com.market.sadang.domain;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter

@AllArgsConstructor
@NoArgsConstructor

//해당 클래스의 빌더 패턴 클래스 생성, 생성자 상단에 선언 시 생성자에 포함된 필드만 빌더 생성
@Builder

@Entity
public class Salt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String salt;

    public Salt(String salt) {
        this.salt = salt;
    }
}
