package com.market.sadang.domain.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MemberUpdateRequestDto {
    private String userId;
    private String username;
    private String email;
    private String address;
    private String detailAddress;

    @Builder
    public MemberUpdateRequestDto(String userId, String username, String email, String address, String detailAddress) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.address = address;
        this.detailAddress = detailAddress;
    }
}