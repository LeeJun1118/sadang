package com.market.sadang.domain.dto;

import com.market.sadang.domain.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberPageReponseDto {
    String name;
    String username;
    String email;
    String address;
    String detailAddress;
    int countBoard;

    public MemberPageReponseDto(Member member,int countBoard) {
        this.name = member.getName();
        this.username = member.getUsername();
        this.email = member.getEmail();
        this.address = member.getAddress();
        this.detailAddress = member.getDetailAddress();
        this.countBoard  = countBoard;
    }
}
