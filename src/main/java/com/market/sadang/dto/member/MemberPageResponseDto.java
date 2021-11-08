package com.market.sadang.dto.member;

import com.market.sadang.domain.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberPageResponseDto {
    String name;
    String username;
    String email;
    String address;
    String detailAddress;
    int countSellBoard;
    int countSoldBoard;

    @Builder
    public MemberPageResponseDto(Member member, int countSellBoard, int countSoldBoard) {
        this.name = member.getName();
        this.username = member.getUsername();
        this.email = member.getEmail();
        this.address = member.getAddress();
        this.detailAddress = member.getDetailAddress();
        this.countSellBoard  = countSellBoard;
        this.countSoldBoard  = countSoldBoard;
    }
}

