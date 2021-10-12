package com.market.sadang.controller;


import com.market.sadang.domain.Member;
import com.market.sadang.domain.Response;
import com.market.sadang.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class MemberController {

    private final AuthService authService;

    @PostMapping("/signup")
    public Response signUpUser(@RequestBody Member member){
        Response response = new Response();

        try {
            authService.signUpUser(member);
            response.setResponse("success");
            response.setMessage("회원가입 성공");
//            return new Response("success", "회원가입을 성공적으로 완료했습니다.",null);
        }catch (Exception e){
            response.setResponse("failed");
            response.setMessage(member.toString());

            response.setData(e.toString());
            System.out.println(e.getMessage());
//            return new Response("error", "회원가입을 하는 도중 오류가 발생했습니다.", null);
        }
        return response;
    }

    /*@PostMapping("/join")
    public Long join(@RequestBody Map<String, String> user) {
        return memberRepository.save(Member.builder()
                .email(user.get("email"))
                .username(user.get("username"))
                .address(user.get("address"))
                .password(passwordEncoder.encode(user.get("password")))
                .build()).getId();
    }*/

    /*@PostMapping("/login")
    public String login(@RequestBody Map<String, String> user) {
        Member member = memberRepository.findByEmail(user.get("email"))
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 E-Mail 입니다."));
        if (!passwordEncoder.matches(user.get("password"), member.getPassword())) {
            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
        }
        return jwtTokenProvider.createToken(member.getUsername(), member.getRoles());
    }*/

}
