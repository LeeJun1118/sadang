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

    private String username;
    private String userId;
    private String email;
    private String address;
    private String detailAddress;

    @Builder
    public MemberResponseDto(Member member) {
        this.username = member.getUsername();
        this.userId = member.getUserId();
        this.email = member.getEmail();
        this.address = member.getAddress();
        this.detailAddress = member.getDetailAddress();
    }
}
