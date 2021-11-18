package com.market.sadang.service;

import com.market.sadang.domain.Member;
import com.market.sadang.dto.member.MemberUpdateRequestDto;
import com.market.sadang.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    //사용자 찾기
    public Member findByMember() {

        try {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String username;
            if (principal instanceof UserDetails) {
                username = ((UserDetails) principal).getUsername();
            } else {
                username = principal.toString();
            }

            return findByUsername(username);

        } catch (Exception e) {
            System.out.println("findByMemberRequest() ERROR : " + e.getMessage());
            return null;
        }
    }

    @Transactional
    public void update(MemberUpdateRequestDto requestDto, Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다."));

        member.update(
                requestDto.getName()
                , requestDto.getUsername()
                , requestDto.getEmail()
                , requestDto.getAddress()
                , requestDto.getDetailAddress());
    }

    public Member findById(Long userId) {
        try {
            return memberRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다."));
        } catch (Exception e) {
            return null;
        }
    }

    public Member findByUsername(String username) {
        try {
            return memberRepository.findByUsername(username)
                    .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다."));
        } catch (Exception e) {
            return null;
        }
    }
}
