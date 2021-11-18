package com.market.sadang.controller;


import com.market.sadang.domain.*;
import com.market.sadang.domain.enumType.BoardStatus;
import com.market.sadang.dto.bord.BoardCreateRequestDto;
import com.market.sadang.dto.bord.BoardListResponseDto;
import com.market.sadang.dto.bord.BoardResponseDto;
import com.market.sadang.dto.bord.BoardUpdateRequestDto;
import com.market.sadang.dto.form.BoardForm;
import com.market.sadang.dto.member.MyBoardListResponseDto;
import com.market.sadang.dto.myFile.MyFileDto;
import com.market.sadang.dto.myFile.MyFileResponseDto;
import com.market.sadang.repository.ChatRoomRepository;
import com.market.sadang.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

//final이 선언된 모든 필드를 인자값으로 하는 생성자를 대신 생성
@RequiredArgsConstructor
@Controller

public class BoardController {

    private final BoardService boardService;
    private final MemberService memberService;
    private final MyFileService myFileService;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomService chatRoomService;
    private final BuyInterestedService buyInterestedService;

    // 글 쓰기 폼
    @GetMapping("/board/new")
    public ModelAndView boardForm(ModelAndView modelAndView, HttpServletRequest request) {

        String username = null;
        Member member = memberService.findByMemberRequest();
        if (member == null) {
            modelAndView.setViewName("redirect:/login");
        } else {
            List<ChatRoom> roomList = chatRoomService.findRoomList();
            modelAndView.addObject("roomIdList", roomList);

            username = member.getUsername();
            modelAndView.addObject("username", username);
            modelAndView.addObject("boardForm", new BoardForm());
            modelAndView.setViewName("board/boardForm");
        }
        return modelAndView;
    }

    // 쓴 글 저장 후 글 보기
    @PostMapping("/board/new")
    public String create(@Valid BoardForm boardForm,
                         BindingResult result,
                         @RequestParam(value = "uploadFile", required = false) List<MultipartFile> files,
                         HttpServletRequest request) throws Exception {

        if (result.hasErrors()) {
            return "board/boardForm";
        } else {
            Member member = memberService.findByMemberRequest();
            BoardCreateRequestDto requestDto =
                    BoardCreateRequestDto.builder()
                            .member(member)
                            .title(boardForm.getTitle())
                            .price(boardForm.getPrice())
                            .content(boardForm.getContent())
                            .build();
            return "redirect:/board/" + boardService.create(requestDto, files);
        }
    }

    // 게시글 보기
    @GetMapping("/board/{id}")
    public ModelAndView searchById(@PathVariable Long id, ModelAndView modelAndView, HttpServletRequest request) {

        //게시글 id로 해당 글의 첨부파일 전체 조회
        List<MyFileResponseDto> myFileResponseDtoList = myFileService.findAllByBoard(id);

        //게시글 첨부파일 id 담을 List 객체 생성
        List<Long> myFileIdList = new ArrayList<>();

        String username = null;
        try {
            username = memberService.findByMemberRequest().getUsername();
        } catch (Exception e) {

        }

        for (MyFileResponseDto myFileResponseDto : myFileResponseDtoList) {
            myFileIdList.add(myFileResponseDto.getFileId());
        }

        BoardResponseDto boardResponseDto = boardService.searchById(id, myFileIdList);

        String interested = buyInterestedService.findByBoardIdInterestedStatus(id);

        String sold = "none";
        if (boardResponseDto.getStatus() == BoardStatus.sold)
            sold = BoardStatus.sold.name();

        List<ChatRoom> roomList = chatRoomService.findRoomList();
        modelAndView.addObject("roomIdList", roomList);
        modelAndView.addObject("username", username);
        modelAndView.addObject("interested", interested);

        modelAndView.addObject("board", boardResponseDto);
        modelAndView.addObject("sold", sold);
        modelAndView.setViewName("board/showBoard");
        return modelAndView;
    }

    @GetMapping("/")
    public ModelAndView searchAllDesc(ModelAndView modelAndView
            , @RequestParam(value = "search", defaultValue = "") String search,
                                      HttpServletRequest request) {

        List<Board> boardList = null;
        if (Objects.equals(search, "")) {
            boardList = boardService.searchAllDesc();
        } else {
            boardList = boardService.searchParam(search);
        }

        //반환할 리스트 생성
        List<BoardListResponseDto> responseDtoList = new ArrayList<>();

        //전체 리스트를 하나씩 반환할 리스트에 넣음
        for (Board board : boardList) {
            BoardListResponseDto responseDto = new BoardListResponseDto(board);
            responseDtoList.add(responseDto);
        }


        List<ChatRoom> roomList = chatRoomService.findRoomList();

        Member member = memberService.findByMemberRequest();

        String username = null;
        if (member != null)
            username = member.getUsername();


        modelAndView.addObject("boardList", responseDtoList);
        modelAndView.addObject("roomIdList", roomList);
        modelAndView.addObject("username", username);

        modelAndView.setViewName("index");
        return modelAndView;
    }

    @GetMapping("/board/update/{id}")
    public ModelAndView updateForm(@PathVariable Long id, ModelAndView modelAndView, HttpServletRequest request) {
        BoardUpdateRequestDto board = boardService.findById(id);
        Member member = memberService.findByMemberRequest();

        List<ChatRoom> roomList = chatRoomService.findRoomList();
        modelAndView.addObject("roomIdList", roomList);

        modelAndView.addObject("username", member.getUsername());
        modelAndView.addObject("boardForm", board);
        modelAndView.addObject("id", id);
        modelAndView.setViewName("board/updateBoard");
        return modelAndView;
    }

    @GetMapping("/board/verify/{id}")
    public int writerVerify(@PathVariable Long id,
                            HttpServletRequest request) {
        //작성자가 맞으면 게시글 번호 반환하고 아니면 -1을 반환하게 해야함
        System.out.println("verify board id ==" + id);
        Board board = boardService.verifyWriter(id);
        if (board != null)
            return board.getId().intValue();
        else
            return -1;
    }

    @PutMapping("/board/update/{id}")
    public String update(@PathVariable Long id,
                         @Valid BoardForm boardForm,
                         BindingResult result,
                         Model model,
                         @RequestParam(value = "uploadFile", required = false) List<MultipartFile> files) throws Exception {

        if (result.hasErrors()) {
            model.addAttribute("id", id);
            return "board/updateBoard";
        }
        BoardUpdateRequestDto requestDto = BoardUpdateRequestDto.builder()
                .title(boardForm.getTitle())
                .price(boardForm.getPrice())
                .content(boardForm.getContent())
                .build();

        List<MyFileResponseDto> myFiles = myFileService.findAllByBoard(id);
        // 새롭게 전달되어온 파일들의 목록을 저장할 List 선언
        List<MultipartFile> addFileList = new ArrayList<>();

        //DB에 존재 하지 않는다면
        if (CollectionUtils.isEmpty(myFiles)) {
            //전달된 파일이 하나라도 존재한다면
            if (!CollectionUtils.isEmpty(files)) {
                for (MultipartFile file : files) {
                    //저장할 파일 목록에 추가
                    addFileList.add(file);
                }
            }
        }
        //DB에 하나 이상 존재한다면
        else {
            //전달된 파일이 없다면
            if (CollectionUtils.isEmpty(files)) {
                for (MyFileResponseDto dbMyFile : myFiles) {
                    myFileService.deleteFile(dbMyFile.getFileId());
                }
            }
            //전달된 파일이 하나 이상 존재
            else {
                List<String> dbOriginNameList = new ArrayList<>();

                //DB에서 파일 원본명 추출
                for (MyFileResponseDto dbMyFile : myFiles) {
                    MyFileDto dbMyFileDto = myFileService.findByFileId(dbMyFile.getFileId());
                    String dbOriginFileName = dbMyFileDto.getOriginFileName();

                    //삭제 요청 파일
                    if (!files.contains(dbOriginFileName)) {
                        myFileService.deleteFile(dbMyFile.getFileId());
                    } else {
                        dbOriginNameList.add(dbOriginFileName);
                    }
                }

                //전달된 파일 하나씩 검사
                for (MultipartFile multipartFile : files) {
                    String multipartOriginName = multipartFile.getOriginalFilename();
                    // DB에 없는 파일이라면
                    if (!dbOriginNameList.contains(multipartOriginName)) {
                        addFileList.add(multipartFile);
                    }
                }


            }
        }
        boardService.update(id, requestDto, addFileList);


        return "redirect:/board/" + id;
//        modelAndView.setViewName("redirect:/board/" + id);
//        return modelAndView;
    }

    @DeleteMapping("/board/delete/{id}")
    public ModelAndView delete(@PathVariable Long id,
                               ModelAndView modelAndView) {
        boardService.delete(id);
        modelAndView.setViewName("redirect:/");
        return modelAndView;
    }

    @DeleteMapping("/myBoard/delete/{id}")
    public String deleteMyBoard(@PathVariable Long id,
                                HttpServletRequest request) {
        boardService.delete(id);
        String referer = request.getHeader("Referer");
        return "redirect:"+ referer;
    }

    @GetMapping("/sell")
    public ModelAndView sell(HttpServletRequest request,
                             ModelAndView modelAndView) {
        Member member = memberService.findByMemberRequest();
        List<MyBoardListResponseDto> boardList = boardService.boardListMemberAndBoardStatus(member, BoardStatus.sell);

        List<ChatRoom> roomList = chatRoomService.findRoomList();
        modelAndView.addObject("roomIdList", roomList);

        modelAndView.addObject("boardList", boardList);
        modelAndView.addObject("username", member.getUsername());
        modelAndView.setViewName("member/salesHistory");

        return modelAndView;
    }

    @GetMapping("/sold")
    public ModelAndView sold(HttpServletRequest request,
                             ModelAndView modelAndView) {
        Member member = memberService.findByMemberRequest();
        List<MyBoardListResponseDto> boardList = boardService.boardListMemberAndBoardStatus(member, BoardStatus.sold);

        List<ChatRoom> roomList = chatRoomService.findRoomList();
        modelAndView.addObject("roomIdList", roomList);

        modelAndView.addObject("boardList", boardList);
        modelAndView.addObject("username", member.getUsername());
        modelAndView.setViewName("member/soldHistory");

        return modelAndView;
    }

    @GetMapping("/buy")
    public ModelAndView buy(HttpServletRequest request,
                            ModelAndView modelAndView) {
        List<MyBoardListResponseDto> boardList =
                boardService.findBoardListByMemberAndBuyStatusOrInterestedStatus(request, BoardStatus.buy);
        Member member = memberService.findByMemberRequest();

        List<ChatRoom> roomList = chatRoomService.findRoomList();
        modelAndView.addObject("roomIdList", roomList);

        modelAndView.addObject("boardList", boardList);
        modelAndView.addObject("username", member.getUsername());
        modelAndView.setViewName("member/purchaseHistory");

        return modelAndView;
    }

    @GetMapping("/interested")
    public ModelAndView interested(HttpServletRequest request,
                                   ModelAndView modelAndView) {
        List<MyBoardListResponseDto> boardList =
                boardService.findBoardListByMemberAndBuyStatusOrInterestedStatus(request, BoardStatus.interested);

        Member member = memberService.findByMemberRequest();


        List<ChatRoom> roomList = chatRoomService.findRoomList();
        modelAndView.addObject("roomIdList", roomList);
        modelAndView.addObject("username", member.getUsername());
        modelAndView.addObject("boardList", boardList);
        modelAndView.setViewName("member/interestedHistory");

        return modelAndView;
    }


    @GetMapping("/board/sold/{id}")
    public String sold(@PathVariable Long id,
                             HttpServletRequest request) {

        boardService.sellerStatus(id);

        String referer = request.getHeader("Referer");
        return "redirect:"+ referer;
    }


    @GetMapping("/board/buy/{roomId}")
    public ModelAndView bought(@PathVariable String roomId,
                               HttpServletRequest request,
                               ModelAndView modelAndView) {

        ChatRoom chatRoom = chatRoomService.findByRoomId(roomId);
        System.out.println(chatRoom.getBoardId());
        boardService.buy(chatRoom.getBoardId());

        modelAndView.setViewName("redirect:/chat/room/enter/" + roomId);
        return modelAndView;
    }
    @GetMapping("/board/myPage/buy/{id}")
    public ModelAndView myPagebuy(@PathVariable Long id,
                               ModelAndView modelAndView) {

        boardService.buy(id);
        modelAndView.setViewName("redirect:/buy");

        return modelAndView;
    }

    @GetMapping("/board/interested/{id}")
    public ModelAndView interested(@PathVariable Long id,
                                   HttpServletRequest request,
                                   ModelAndView modelAndView) {

        boardService.interested(id);

        modelAndView.setViewName("redirect:/board/" + id);
        return modelAndView;
    }

    @GetMapping("/myBoard/interested/{id}")
    public ModelAndView myInterested(@PathVariable Long id,
                                     HttpServletRequest request,
                                     ModelAndView modelAndView) {

        boardService.interested(id);

        modelAndView.setViewName("redirect:/interested");
        return modelAndView;
    }
}
