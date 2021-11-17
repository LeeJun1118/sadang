package com.market.sadang.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.market.sadang.config.UserRole;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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

    @Column(nullable = false)
    private String price;

    @Column(nullable = false)
    private String address;

    //CascadeType.MERGE – 트랜잭션이 종료되고 detach 상태에서 연관 엔티티를 추가하거나 변경된 이후에
    // 부모 엔티티가 merge()를 수행하게 되면 변경사항이 적용된다.(연관 엔티티의 추가 및 수정 모두 반영됨)
    @ManyToOne(cascade = {CascadeType.MERGE})
    @JoinColumn(updatable = false)
    @JsonBackReference
    private Member member;


    //게시글 삭제 시 첨부파일도 같이 삭제
    @OneToMany(
            mappedBy = "board",
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            orphanRemoval = true)
    private List<MyFile> fileList = new ArrayList<>();

    @Column
    @Enumerated(EnumType.ORDINAL)
    private BoardStatus sellStatus = BoardStatus.sell;


    @OneToMany(mappedBy = "board", cascade = CascadeType.MERGE, orphanRemoval = true)
    private List<BuyInterested> buyInterested = new ArrayList<>();

    @Builder
    public Board(Member member, String title, String price, String content, String address) {
        this.member = member;
        this.title = title;
        this.price = price;
        this.content = content;
        this.address = address;
    }

    public void update(String title,String price,String content){
        this.title = title;
        this.price = price;
        this.content = content;
    }

    // Board에서 파일 처리 위함
    public void addFile(MyFile file) {
        this.fileList.add(file);

        // 게시글에 파일이 저장되어있지 않은 경우
        if (file.getBoard() != this)
            // 파일 저장
            file.setBoard(this);
    }

    public void sellerStatus(BoardStatus status){
        this.sellStatus = status;
    }
}
