package com.market.sadang.domain.dto;

import com.market.sadang.domain.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@NoArgsConstructor
public class MemberResponseDto {

    private String name;
    private String username;
    private String email;
    private String address;
    private String detailAddress;

    @Builder
    public MemberResponseDto(Member member) {
        this.name = member.getName();
        this.username = member.getUsername();
        this.email = member.getEmail();
        this.address = member.getAddress();
        this.detailAddress = member.getDetailAddress();
    }
}
