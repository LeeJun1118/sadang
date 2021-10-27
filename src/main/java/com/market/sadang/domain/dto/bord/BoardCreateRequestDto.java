package com.market.sadang.domain.dto.bord;

import com.market.sadang.domain.Board;
import com.market.sadang.domain.Member;
import com.market.sadang.domain.MyFile;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor

// DTO 사용 이유 : entity 클래스는 데이터베이스와 맞닿은 핵심 클래스이며, 이를 기준으로
// 1. 테이블 생성 및 스키마 변경
// 2. 수많은 서비스 클래스나 비즈니스 로직 동작
// 이로 인해 entity 클래스가 변경되면 여러 클래스에 영향을 끼치게 됨
public class BoardCreateRequestDto {

    private Member member;
    private String title;
    private String price;
    private String content;
    private String address;

    @Builder
    public BoardCreateRequestDto(Member member, String title, String price, String content, String address) {
        this.member = member;
        this.title = title;
        this.price = price;
        this.content = content;
        this.address = address;
    }

    public Board toEntity(){
        return Board.builder()
                .member(member)
                .title(title)
                .price(price)
                .content(content)
                .address(address)
                .build();
    }
}
