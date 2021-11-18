package com.market.sadang.controller;


import com.market.sadang.domain.enumType.BoardStatus;
import com.market.sadang.domain.enumType.UserRole;
import com.market.sadang.domain.*;
import com.market.sadang.dto.form.SignUpForm;
import com.market.sadang.dto.member.MemberPageResponseDto;
import com.market.sadang.dto.member.MemberResponseDto;
import com.market.sadang.dto.member.MemberUpdateRequestDto;
import com.market.sadang.dto.form.MemberForm;
import com.market.sadang.dto.member.RequestVerifyUserDto;
import com.market.sadang.repository.ChatMessageRepository;
import com.market.sadang.repository.ChatRoomRepository;
import com.market.sadang.repository.MemberRepository;
import com.market.sadang.repository.SignUpRepository;
import com.market.sadang.service.BoardService;
import com.market.sadang.service.BuyInterestedService;
import com.market.sadang.service.ChatRoomService;
import com.market.sadang.service.MemberService;
import com.market.sadang.service.authUtil.AuthService;
import com.market.sadang.service.authUtil.MyUserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.*;

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
    private final MemberService memberService;
    private final BoardService boardService;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomService chatRoomService;
    private final ChatMessageRepository chatMessageRepository;
    private final BuyInterestedService buyInterestedService;
    private final MyUserDetailService myUserDetailService;
    private final SignUpRepository signUpRepository;

    @GetMapping("/signup")
    public ModelAndView signUpUser(ModelAndView model) {
        model.addObject("signUpForm", new SignUpForm());
        model.setViewName("auth/signUpPage");

        List<Member> Members = memberRepository.findAllByRole(UserRole.ROLE_NOT_PERMITTED);
        for (Member member : Members) {
            if (LocalDateTime.now().isAfter(member.getCreatedDate().plusMinutes(3))
                    && member.getRole() == UserRole.ROLE_NOT_PERMITTED) {
                signUpRepository.delete(signUpRepository.findByMember(member));
                memberRepository.delete(member);
            }
        }

        return model;
    }

    @PostMapping("/idCheck")
    public int idCheck(@RequestBody RequestVerifyUserDto username) {
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
    public int updateIdCheck(@RequestBody RequestVerifyUserDto user) {
        Member member = memberService.findByMember();
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

    @PostMapping("/sendMail")
    public ModelAndView verify(SignUpForm signUpForm, ModelAndView model) {
        Response response;

        try {
            Member member = new Member(signUpForm);
            // 회원 가입
//            authService.signUpUser(member);
            myUserDetailService.signUp(member);

            //메일 보냄
            authService.sendVerificationMail(member);

            RequestVerifyUserDto verifyUser = new RequestVerifyUserDto();
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
    public int mailConfirm(@RequestBody RequestVerifyUserDto user) throws Exception {

        // ajax에서 userId=testUserId 이런식으로 받아와짐
        String name = user.getUsername().split("=")[1];
        System.out.println(user.getUsername());

        int sendReq = 0;
        Member member = authService.findByUsername(name);


        System.out.println(member.getUsername());
        if (member.getUsername() != null && member.getRole() == UserRole.ROLE_USER) {
            System.out.println("member.name()" + member.getUsername());
            System.out.println("member.getRole()=" + member.getRole());
            sendReq = 1;
        } else {
            System.out.println("member.name()" + member.getUsername());
            System.out.println("member.getRole()=" + member.getRole());
            System.out.println("메일 인증 안함");
        }

        if (LocalDateTime.now().isAfter(member.getCreatedDate().plusMinutes(3))
                && member.getRole() == UserRole.ROLE_NOT_PERMITTED) {
            signUpRepository.delete(signUpRepository.findByMember(member));
            memberRepository.delete(member);
            sendReq = -1;
        }


        return sendReq;
    }

    @GetMapping("/user/login")
    public ModelAndView loginPage(ModelAndView modelAndView) {
        modelAndView.setViewName("auth/loginPage");
        return modelAndView;
    }


    // My Page
    @GetMapping("/myPage")
    public ModelAndView myPageForm(ModelAndView modelAndView) {
        Member member = memberService.findByMember();
        int countSellBoard = boardService.countAllByMemberBoardStatus(member, BoardStatus.sell);
        int countSoldBoard = boardService.countAllByMemberBoardStatus(member, BoardStatus.sold);
        int countBuyBoard = buyInterestedService.findByMemberAndBuyStatusOrInterestedStatus(member, BoardStatus.buy).size();
        int countInterestedBoard = buyInterestedService.findByMemberAndBuyStatusOrInterestedStatus(member, BoardStatus.interested).size();
        int countChatRoom = chatRoomService.findRoomList().size();


        MemberPageResponseDto memberPageResponseDto = new MemberPageResponseDto(member, countSellBoard, countSoldBoard, countBuyBoard, countInterestedBoard);

        List<ChatRoom> roomList = chatRoomService.findRoomList();
        modelAndView.addObject("roomIdList", roomList);

        modelAndView.addObject("member", memberPageResponseDto);
        modelAndView.addObject("username", member.getUsername());
        modelAndView.addObject("countChatRoom", countChatRoom);
        modelAndView.setViewName("member/myPage");
        return modelAndView;
    }

    @GetMapping("/myPage/update")
    public ModelAndView updateInfoForm(ModelAndView modelAndView) {
        Member member = memberService.findByMember();

        List<ChatRoom> roomList = chatRoomService.findRoomList();
        modelAndView.addObject("roomIdList", roomList);

        modelAndView.addObject("member", new MemberResponseDto(member));
        modelAndView.addObject("username", new MemberResponseDto(member).getUsername());
        modelAndView.setViewName("member/updateInfo");
        return modelAndView;
    }

    @PostMapping("/myInfo/update")
    public ModelAndView infoUpdate(@Valid MemberForm memberForm,
                                   ModelAndView modelAndView) {

        MemberUpdateRequestDto requestDto = MemberUpdateRequestDto.builder()
                .name(memberForm.getName())
                .username(memberForm.getUsername())
                .email(memberForm.getEmail())
                .address(memberForm.getAddress())
                .detailAddress(memberForm.getDetailAddress())
                .build();

        Member member = memberService.findByMember();
        memberService.update(requestDto, member.getId());

        modelAndView.setViewName("redirect:/");

        return modelAndView;
    }


    @GetMapping("/loginCheck")
    public int loginCheck() {
        int unReadMessages = -1;

        //로그인 하지 않은 사용자라면 -1 반환
        try {
            Member member = memberService.findByMember();
            if (member != null) {
                //receiver = member.gerusername , receiverStatus = N
                unReadMessages = chatMessageRepository.countAllByReceiverAndReceiverStatus(member, ReadStatus.N);
                return unReadMessages;
            }

        } catch (Exception e) {
            System.out.println("/loginCheck 에서 오류 발생" + e.getMessage());
        }
        System.out.println("/loginCheck == unReadMessages : " + unReadMessages);
        return unReadMessages;
    }
}

