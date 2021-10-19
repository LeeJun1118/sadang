package com.market.sadang.controller;


import com.market.sadang.config.JwtRequestFilter;
import com.market.sadang.config.UserRole;
import com.market.sadang.domain.Member;
import com.market.sadang.domain.Response;
import com.market.sadang.domain.SignUpForm;
import com.market.sadang.domain.request.RequestLoginUser;
import com.market.sadang.domain.request.RequestVerifyUser;
import com.market.sadang.repository.MemberRepository;
import com.market.sadang.service.AuthService;
import com.market.sadang.service.CookieUtil;
import com.market.sadang.service.JwtUtil;
import com.market.sadang.service.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor

//ResponseBody 를 모든 메소드에 적용해줌
//ResponseBody 는 비동기 통신을 위해 Json 형태로 data를 담아서 보내기 위함
//RequestBody 도 마찬가지지
@RestController
//@Controller
public class MemberController {

    private final AuthService authService;
    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;
    private final CookieUtil cookieUtil;
    private final RedisUtil redisUtil;

//    private final JwtRequestFilter jwtRequestFilter;

    @GetMapping("/signup")
    public ModelAndView signUpUser(ModelAndView model) {
        model.addObject("signUpForm", new SignUpForm());
        model.setViewName("auth/signUpPage");
        return model;
    }

    @PostMapping(value = "/signup")
    public ModelAndView signUpUser(SignUpForm signUpForm, ModelAndView model) {
        signUpForm.setAddress(signUpForm.getAddress() + " " + signUpForm.getDetailAddress());

        model.addObject("member", signUpForm);
        model.setViewName("auth/mailSend");
        return model;
    }

    @PostMapping("/idCheck")
    public int idCheck(@RequestBody RequestVerifyUser userId) {
        // ajax에서 userId=testuserId 이런식으로 받아와짐
        Map<String, Object> object = new HashMap<String, Object>();
        int count = 0;
        if (userId.getUserId() == "") {
            count = 1;
        } else
            count = memberRepository.countByUserId(userId.getUserId());

        System.out.println("userId==" + userId.getUserId());
        System.out.println("countUser==" + count);

        return count;
    }

    @PostMapping("/login")
    public ModelAndView login(RequestLoginUser user,
                              ModelAndView modelAndView,
                              HttpServletRequest req,
                              HttpServletResponse res) {
        System.out.println("userId.getUserId()=====" + user.getUserId());
        Response response;

        try {
            final Member member = authService.loginUser(user.getUserId(), user.getPassword());
            final String token = jwtUtil.generateToken(member);
            final String refreshJwt = jwtUtil.generateRefreshToken(member);

            Cookie accessToken = cookieUtil.createCookie(JwtUtil.ACCESS_TOKEN_NAME, token);
            Cookie refreshToken = cookieUtil.createCookie(JwtUtil.REFRESH_TOKEN_NAME, refreshJwt);

            redisUtil.setDataExpire(refreshJwt, member.getUserId(), JwtUtil.REFRESH_TOKEN_VALIDATION_SECOND);
            res.addCookie(accessToken);
            res.addCookie(refreshToken);

            modelAndView.setViewName("/index");
            response = new Response("success", "로그인 성공", token);

        } catch (Exception e) {
            modelAndView.setViewName("auth/loginPage");
            response = new Response("error", "로그인 실패", e.getMessage());
        }

        return modelAndView;
    }

    @PostMapping("/sendMail")
    public ModelAndView verify(Member member, ModelAndView model) {
        Response response;

        try {
            // 회원 가입
            authService.signUpUser(member);

            //메일 보냄
            authService.sendVerificationMail(member);

            RequestVerifyUser verifyUser = new RequestVerifyUser();
            verifyUser.setUserId(member.getUserId());
            model.addObject("userId", verifyUser.getUserId());

            response = new Response("success", "성공적으로 인증메일을 보냈습니다.", null);
            System.out.println(response.getMessage());
        } catch (Exception e) {
            response = new Response("error", "인증메일을 보내는데 실패했습니다.", e);
//            System.out.println(response.getMessage());
        }

        model.setViewName("auth/mailConfirm");
        return model;
    }

    @GetMapping("/verify/{key}")
    public ModelAndView getVerify(@PathVariable String key, ModelAndView modelAndView) {
        Response response;
        try {
            authService.verifyEmail(key);
            modelAndView.setViewName("auth/verify");
            return modelAndView;
        } catch (Exception e) {
            modelAndView.setViewName("auth/expired");
            return modelAndView;
        }
    }

    @PostMapping("/confirm")
    public int mailConfirm(@RequestBody RequestVerifyUser userId) throws Exception {

        // ajax에서 userId=testUserId 이런식으로 받아와짐
        String name = userId.getUserId().split("=")[1];

//        Map<String, Object> object = new HashMap<String, Object>();

        int sendReq = 0;

        Member member = authService.findByUserId(name);

        System.out.println(member.getUserId());
        if (member.getUserId() != null && member.getRole() == UserRole.ROLE_USER) {
            System.out.println("member.name()" + member.getUserId());
            System.out.println("member.getRole()=" + member.getRole());
            sendReq = 1;
//            object.put("responseCode", "success");
        } else {
            System.out.println("member.name()" + member.getUserId());
            System.out.println("member.getRole()=" + member.getRole());
//            object.put("responseCode", "error");
            System.out.println("메일 인증 안함");
        }
        return sendReq;
    }

    @GetMapping("/login")
    public ModelAndView loginPage(ModelAndView modelAndView) {
        modelAndView.setViewName("auth/loginPage");
        return modelAndView;
    }

    @GetMapping("/board/new")
    public ModelAndView test(ModelAndView model,HttpServletRequest request){
        String jwtToken = cookieUtil.getCookie(request,"accessToken").getValue();
        String userId = jwtUtil.getUserId(jwtToken);

        model.addObject("userId",userId);
        model.setViewName("board/boardForm");
        return model;
    }
}

