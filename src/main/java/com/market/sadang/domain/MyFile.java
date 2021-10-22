package com.market.sadang.domain;


import lombok.*;

import javax.persistence.*;

//lombok 어노테이션
//클래스 내 모든 필드의 Get 매소드를 자동 생성
@Getter
@Setter
//기본 생성자 자동 추가
//public 클래스명(){} 와 같은 효과
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class MyFile extends BaseTimeEntity{
    //PK 의 생성 규칙을 나타냄 strategy = GenerationType.IDENTITY 로 자동 증가 됨
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    //CascadeType.MERGE – 트랜잭션이 종료되고 detach 상태에서 연관 엔티티를 추가하거나 변경된 이후에
    // 부모 엔티티가 merge()를 수행하게 되면 변경사항이 적용된다.(연관 엔티티의 추가 및 수정 모두 반영됨)
    @ManyToOne
    private Board board;

    @Column(nullable = false)
    private String originFileName;

    @Column(nullable = false)
    private String filePath;

    @Column(nullable = false)
    private Long fileSize;

    @Builder
    public MyFile(String originFileName, String filePath, Long fileSize) {
        this.originFileName = originFileName;
        this.filePath = filePath;
        this.fileSize = fileSize;
    }

    // Board 정보 저장
    public void setBoard(Board board){
        this.board = board;

        // 게시글에 현재 파일이 존재하지 않는다면
        if(!board.getFileList().contains(this))
            // 파일 추가
            board.getFileList().add(this);
    }

/*    public void setMember(Member member){
        this.member = member;
        // 게시글에 현재 사용자가 존재하지 않는다면
        if(!member.getFileList().contains(this))
            // 파일 추가
            member.getFileList().add(this);
    }*/
}
