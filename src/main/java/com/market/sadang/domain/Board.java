package com.market.sadang.domain;

import lombok.*;

import javax.persistence.*;

//테이블과 연결될 클래스
@Entity

@Getter
@Setter

//기본 생성자 자동 생성
@NoArgsConstructor

//클래스에 존재하는 모든 필드에 대한 생성자 자동 생성
@AllArgsConstructor

@Builder
public class Board extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @ManyToOne(cascade = {CascadeType.MERGE})
    private Member member;

    public void update(String title,String content){
        this.title = title;
        this.content = content;
    }
}
