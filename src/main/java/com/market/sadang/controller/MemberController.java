package com.market.sadang.controller;


import com.market.sadang.config.UserRole;
import com.market.sadang.domain.Member;
import com.market.sadang.domain.Response;
import com.market.sadang.domain.SignUpForm;
import com.market.sadang.domain.dto.BoardUpdateRequestDto;
import com.market.sadang.domain.dto.MemberResponseDto;
import com.market.sadang.domain.dto.MemberUpdateRequestDto;
import com.market.sadang.domain.dto.form.BoardForm;
import com.market.sadang.domain.dto.form.MemberForm;
import com.market.sadang.domain.request.RequestLoginUser;
import com.market.sadang.domain.request.RequestVerifyUser;
import com.market.sadang.repository.MemberRepository;
import com.market.sadang.service.MemberService;
import com.market.sadang.service.authUtil.AuthService;
import com.market.sadang.service.authUtil.CookieUtil;
import com.market.sadang.service.authUtil.JwtUtil;
import com.market.sadang.service.authUtil.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

//final이 선언된 모든 필드를 인자값으로 하는 생성자를 대신 생성
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
    private final MemberService memberService;
    private final RedisTemplate redisTemplate;

//    private final JwtRequestFilter jwtRequestFilter;

    @GetMapping("/signup")
    public ModelAndView signUpUser(ModelAndView model) {
        model.addObject("signUpForm", new SignUpForm());
        model.setViewName("auth/signUpPage");
        return model;
    }

    @PostMapping(value = "/signup")
    public ModelAndView signUpUser(SignUpForm signUpForm, ModelAndView model) {
//        signUpForm.setAddress(signUpForm.getAddress() + " " + signUpForm.getDetailAddress());

        model.addObject("member", signUpForm);
        model.setViewName("auth/mailSend");
        return model;
    }

    @PostMapping("/idCheck")
    public int idCheck(@RequestBody RequestVerifyUser username) {
        // ajax에서 userId=testuserId 이런식으로 받아와짐
        Map<String, Object> object = new HashMap<String, Object>();
        int count = 0;
        if (username.getUsername() == "") {
            count = 1;
        } else
            count = memberRepository.countByUsername(username.getUsername());

        System.out.println("username==" + username.getUsername());
        System.out.println("countUser==" + count);

        return count;
    }

    @PostMapping("/updateIdCheck")
    public int updateIdCheck(@RequestBody RequestVerifyUser user, HttpServletRequest request) {
        Member member = memberService.searchMemberId(request);
        int count = 0;
        int distinctCount = memberRepository.countByUsername(user.getUsername());

        if (Objects.equals(user.getUsername(), member.getUsername())) {
            count = 0;
        } else {
            if (distinctCount > 0)
                count = 1;
        }

        System.out.println("update userId==" + user.getUsername());
        System.out.println("update countUser==" + count);

        return count;
    }

    @PostMapping("/login")
    public ModelAndView login(RequestLoginUser user,
                              ModelAndView modelAndView,
                              HttpServletRequest req,
                              HttpServletResponse res) {
        System.out.println("userId.getUsername()=====" + user.getUsername());
        Response response;

        try {
            final Member member = authService.loginUser(user.getUsername(), user.getPassword());
            final String token = jwtUtil.generateToken(member);
            final String refreshJwt = jwtUtil.generateRefreshToken(member);

            Cookie accessToken = cookieUtil.createCookie(JwtUtil.ACCESS_TOKEN_NAME, token);
            Cookie refreshToken = cookieUtil.createCookie(JwtUtil.REFRESH_TOKEN_NAME, refreshJwt);

            redisUtil.setDataExpire(refreshJwt, member.getUsername(), JwtUtil.REFRESH_TOKEN_VALIDATION_SECOND);
            res.addCookie(accessToken);
            res.addCookie(refreshToken);

            modelAndView.setViewName("redirect:");
            response = new Response("success", "로그인 성공", token);

        } catch (Exception e) {
            modelAndView.setViewName("auth/loginPage");
            System.out.println("로그인 실패 : " + e.getMessage());
            response = new Response("error", "로그인 실패", e.getMessage());
        }

        return modelAndView;
    }


    @GetMapping("/user/out")
    public ModelAndView logout(ModelAndView modelAndView,
                               HttpServletRequest req,
                               HttpServletResponse res) throws ServletException {

        Cookie accessToken = cookieUtil.getCookie(req, "accessToken");
        redisUtil.deleteData(accessToken.getValue());


        Cookie resAccessToken = new Cookie("accessToken", null);
        Cookie resRefreshToken = new Cookie("refreshToken", null);

        resAccessToken.setHttpOnly(true);
        resAccessToken.setSecure(false);
        resAccessToken.setMaxAge(0);
        resAccessToken.setPath("/");

        resRefreshToken.setHttpOnly(true);
        resRefreshToken.setSecure(false);
        resRefreshToken.setMaxAge(0);
        resRefreshToken.setPath("/");

        res.addCookie(resAccessToken);
        res.addCookie(resRefreshToken);


        modelAndView.setViewName("redirect:/");
        return modelAndView;
    }

  /*  @PostMapping("/login")
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

//            redisUtil.setDataExpire(refreshJwt, member.getUserId(), JwtUtil.REFRESH_TOKEN_VALIDATION_SECOND);
            redisUtil.setDataExpire(refreshJwt, member, JwtUtil.REFRESH_TOKEN_VALIDATION_SECOND);
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
    public ModelAndView verify(Member member, ModelAndView model) throws NotFoundException {
        Response response;

*//*        Member member = new Member();
        member.setAddress(signUpForm.getAddress());
        member.setUsername(signUpForm.getUsername());
        member.setPassword(signUpForm.getPassword());
        member.setEmail(signUpForm.getEmail());*//*


            // 회원 가입
//            authService.signUpUser(member);

            //메일 보냄
            authService.sendVerificationMail(member);

            RequestVerifyUser verifyUser = new RequestVerifyUser();
            verifyUser.setUserId(member.getUserId());
            model.addObject("userId", verifyUser.getUserId());

            response = new Response("success", "성공적으로 인증메일을 보냈습니다.", null);
            System.out.println(response.getMessage());


        model.setViewName("auth/mailConfirm");
        return model;
    }*/


    @PostMapping("/sendMail")
    public ModelAndView verify(Member member, ModelAndView model) {
        Response response;

        try {
            // 회원 가입
            authService.signUpUser(member);

            //메일 보냄
            authService.sendVerificationMail(member);

            RequestVerifyUser verifyUser = new RequestVerifyUser();
            verifyUser.setUsername(member.getUsername());
            model.addObject("username", verifyUser.getUsername());

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
    public int mailConfirm(@RequestBody RequestVerifyUser user) throws Exception {

        // ajax에서 userId=testUserId 이런식으로 받아와짐
        String name = user.getUsername().split("=")[1];

//        Map<String, Object> object = new HashMap<String, Object>();

        int sendReq = 0;

        Member member = authService.findByUserId(name);

        System.out.println(member.getUsername());
        if (member.getUsername() != null && member.getRole() == UserRole.ROLE_USER) {
            System.out.println("member.name()" + member.getUsername());
            System.out.println("member.getRole()=" + member.getRole());
            sendReq = 1;
//            object.put("responseCode", "success");
        } else {
            System.out.println("member.name()" + member.getUsername());
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

    @GetMapping("/test/new")
    public ModelAndView authTest(ModelAndView model, HttpServletRequest request) {
        //쿠키에서 토큰 받아서 사용자 정보 확인
        Cookie jwtToken = cookieUtil.getCookie(request, "accessToken");
        String username = null;

        if (jwtToken != null) {
            username = jwtUtil.getUsername(jwtToken.getValue());
            model.addObject("username", username);

        } else {
            model.addObject("username", "null");
        }

        model.setViewName("testForm");
        return model;
    }

    // My Page
    @GetMapping("/myPage")
    public ModelAndView myPageForm(ModelAndView modelAndView, HttpServletRequest request) {
        Member member = memberService.searchMemberId(request);
        modelAndView.addObject("member", member);
        modelAndView.setViewName("member/myPage");
        return modelAndView;
    }

    @GetMapping("/myPage/update")
    public ModelAndView updateInfoForm(ModelAndView modelAndView, HttpServletRequest request) {
        Member member = memberService.searchMemberId(request);
        modelAndView.addObject("member", new MemberResponseDto(member));
        modelAndView.setViewName("member/updateInfo");
        return modelAndView;
    }

    @PostMapping("/myInfo/update")
    public ModelAndView infoUpdate(@Valid MemberForm memberForm,
                               HttpServletRequest request,
                               ModelAndView modelAndView) {

        MemberUpdateRequestDto requestDto = MemberUpdateRequestDto.builder()
                .name(memberForm.getName())
                .username(memberForm.getUsername())
                .email(memberForm.getEmail())
                .address(memberForm.getAddress())
                .detailAddress(memberForm.getDetailAddress())
                .build();

        Member member = memberService.searchMemberId(request);
        memberService.update(requestDto,member.getId());

        modelAndView.setViewName("redirect:/");

        return modelAndView;
    }
}

