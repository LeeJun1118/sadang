package com.market.sadang.controller;


import com.market.sadang.domain.Member;
import com.market.sadang.domain.Response;
import com.market.sadang.domain.SignUpForm;
import com.market.sadang.domain.request.RequestLoginUser;
import com.market.sadang.domain.request.RequestVerifyEmail;
import com.market.sadang.service.AuthService;
import com.market.sadang.service.CookieUtil;
import com.market.sadang.service.JwtUtil;
import com.market.sadang.service.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RequiredArgsConstructor
@Controller
public class MemberController {

    private final AuthService authService;
    private final JwtUtil jwtUtil;
    private final CookieUtil cookieUtil;
    private final RedisUtil redisUtil;

    @GetMapping("/signup")
    public String signUpUser(Model model){
        model.addAttribute("signUpForm",new SignUpForm());
        return "signUpPage";
    }

    @PostMapping("/signup")
    public String signUpUser(@Valid SignUpForm signUpForm){
        Response response = new Response();

        try {
            authService.signUpUser(signUpForm);
            response.setResponse("success");
            response.setMessage("회원가입 성공");
//            return new Response("success", "회원가입을 성공적으로 완료했습니다.",null);
        }catch (Exception e){
            response.setResponse("failed");

            response.setData(e.toString());
            System.out.println(e.getMessage());
//            return new Response("error", "회원가입을 하는 도중 오류가 발생했습니다.", null);
        }
        return "emailVerifyPage";
    }

    @PostMapping("/login")
    public Response login(@RequestBody RequestLoginUser user,
                          HttpServletRequest req,
                          HttpServletResponse res){
        try {
            final Member member = authService.loginUser(user.getUsername(),user.getPassword());
            final String token = jwtUtil.generateToken(member);
            final String refreshJwt = jwtUtil.generateRefreshToken(member);

            Cookie accessToken = cookieUtil.createCookie(JwtUtil.ACCESS_TOKEN_NAME, token);
            Cookie refreshToken = cookieUtil.createCookie(JwtUtil.REFRESH_TOKEN_NAME,refreshJwt);

            redisUtil.setDataExpire(refreshJwt, member.getUsername(),JwtUtil.REFRESH_TOKEN_VALIDATION_SECOND);
            res.addCookie(accessToken);
            res.addCookie(refreshToken);

            return new Response("success", "로그인 성공", token);
        }
        catch (Exception e){
            return new Response("error","로그인 실패",e.getMessage());
        }
    }

    @PostMapping("/verify")
    public String verify(@RequestParam(value = "email") String email, HttpServletRequest req, HttpServletResponse res){
        Response response;
        try{
//            Member member = authService.findByUsername(requestVerifyEmail.getUsername());
//            authService.sendVerificationMail(member);
            authService.sendVerificationMail(email);
            response = new Response("success", "성공적으로 인증메일을 보냈습니다.",null);
            System.out.println(response);
        } catch (Exception e) {
            response = new Response("error","인증메일을 보내는데 실패했습니다.",e);
            System.out.println(response);
        }
        return "index";
    }

    @GetMapping("/verify/{key}")
    public Response getVerify(@PathVariable String key){
        Response response;
        try {
            authService.verifyEmail(key);
            response = new Response("success","성공적으로 인증메일을 확인했습니다.",null);
        } catch (Exception e) {
            response = new Response("error","인증메일을 보내는데 문제가 발생했습니다.",e);
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
