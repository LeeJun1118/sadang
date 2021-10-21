package com.market.sadang.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
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

    @ManyToOne(cascade = {CascadeType.MERGE})
    @JsonBackReference
    private Member member;

    @OneToMany(
            mappedBy = "board",
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            orphanRemoval = true)
    private List<MyFile> fileList = new ArrayList<>();


    @Builder
    public Board(Member member, String title, String content) {
        this.member = member;
        this.title = title;
        this.content = content;
    }

    public void update(String title,String content){
        this.title = title;
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
}
