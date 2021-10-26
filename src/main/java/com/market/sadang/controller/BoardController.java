package com.market.sadang.controller;


import com.market.sadang.domain.Board;
import com.market.sadang.domain.Member;
import com.market.sadang.domain.MyFile;
import com.market.sadang.domain.dto.*;
import com.market.sadang.domain.dto.form.BoardForm;
import com.market.sadang.service.BoardService;
import com.market.sadang.service.MemberService;
import com.market.sadang.service.MyFileService;
import lombok.RequiredArgsConstructor;
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
@RestController
public class BoardController {

    private final BoardService boardService;
    private final MemberService memberService;
    private final MyFileService myFileService;

    // 글 쓰기 폼
    @GetMapping("/board/new")
    public ModelAndView boardForm(ModelAndView modelAndView) {
        modelAndView.addObject("boardForm", new BoardForm());
        modelAndView.setViewName("board/boardForm");
        return modelAndView;
    }

    // 쓴 글 저장 후 글 보기
    @PostMapping("/board/new")
    public ModelAndView create(@Valid BoardForm boardForm,
                               BindingResult result,
                               @RequestParam(value = "uploadFile", required = false) List<MultipartFile> files,
                               HttpServletRequest request,
                               ModelAndView modelAndView) throws Exception {
//        modelAndView.setViewName("redirect:/board/" + boardService.create(boardCreateRequestDto, request));
        Member member = memberService.searchMemberId(request);

        BoardCreateRequestDto requestDto =
                BoardCreateRequestDto.builder()
                        .member(member)
                        .title(boardForm.getTitle())
                        .price(boardForm.getPrice())
                        .content(boardForm.getContent())
                        .address(member.getAddress())
                        .build();

        modelAndView.setViewName("redirect:/board/" + boardService.create(requestDto, files));
        return modelAndView;
    }

    // 게시글 보기
    @GetMapping("/board/{id}")
    public ModelAndView searchById(@PathVariable Long id, ModelAndView modelAndView) {

        //게시글 id로 해당 글의 첨부파일 전체 조회
        List<MyFileResponseDto> myFileResponseDtoList = myFileService.findAllByBoard(id);

        //게시글 첨부파일 id 담을 List 객체 생성
        List<Long> myFileIdList = new ArrayList<>();

        for (MyFileResponseDto myFileResponseDto : myFileResponseDtoList) {
            myFileIdList.add(myFileResponseDto.getFileId());
        }

        BoardResponseDto boardResponseDto = boardService.searchById(id, myFileIdList);

        modelAndView.addObject("board", boardResponseDto);
        modelAndView.setViewName("board/showBoard");
        return modelAndView;
    }

    @GetMapping("/")
    public ModelAndView searchAllDesc(ModelAndView modelAndView
            , @RequestParam(value = "search", defaultValue = "") String search) {

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

        modelAndView.addObject("boardList", responseDtoList);
        modelAndView.setViewName("index");
        return modelAndView;
    }

    @GetMapping("/board/update/{id}")
    public ModelAndView updateForm(@PathVariable Long id, ModelAndView modelAndView) {
        BoardUpdateRequestDto board = boardService.findById(id);
        modelAndView.addObject("boardForm", board);
        modelAndView.setViewName("board/updateBoard");
        return modelAndView;
    }

    @GetMapping("/board/verify/{id}")
    public int writerVerify(@PathVariable Long id,
                            HttpServletRequest request) {
        //작성자가 맞으면 게시글 번호 반환하고 아니면 -1을 반환하게 해야함
        System.out.println("verify board id ==" + id);
        Board board = boardService.verifyWriter(id, request);
        if (board != null)
            return board.getId().intValue();
        else
            return -1;
    }

    @PostMapping("/board/update/{id}")
    public ModelAndView update(@PathVariable Long id,
                               @Valid BoardForm boardForm,
                               @RequestParam(value = "uploadFile", required = false) List<MultipartFile> files,
                               ModelAndView modelAndView) throws Exception {

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
        modelAndView.setViewName("redirect:/board/" + id);
        return modelAndView;
    }

    @PostMapping("/board/delete")
    public int delete(@RequestBody BoardWriterRequestDto requestDto,
                      HttpServletRequest request) {
        long userId = Long.parseLong(requestDto.getBoardId());
        return boardService.delete(userId, request);
    }
}
