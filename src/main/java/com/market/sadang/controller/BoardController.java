package com.market.sadang.controller;


import com.market.sadang.domain.Board;
import com.market.sadang.domain.dto.*;
import com.market.sadang.service.board.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

//final이 선언된 모든 필드를 인자값으로 하는 생성자를 대신 생성
@RequiredArgsConstructor
@RestController
public class BoardController {

    private final BoardService boardService;

    // 글 쓰기 폼
    @GetMapping("/board/new")
    public ModelAndView boardForm(ModelAndView modelAndView) {
        modelAndView.addObject("boardForm", new BoardResponseDto());
        modelAndView.setViewName("board/boardForm");
        return modelAndView;
    }

    // 쓴 글 저장 후 글 보기
    @PostMapping("/board/new")
    public ModelAndView create(BoardCreateRequestDto boardCreateRequestDto,
                               HttpServletRequest request,
                               ModelAndView modelAndView) {
        modelAndView.setViewName("redirect:/board/" + boardService.create(boardCreateRequestDto, request));
        return modelAndView;
    }

    // 게시글 보기
    @GetMapping("/board/{id}")
    public ModelAndView searchById(@PathVariable Long id, ModelAndView modelAndView) {
        BoardResponseDto boardResponseDto = boardService.searchById(id);
        modelAndView.addObject("board", boardResponseDto);
        modelAndView.setViewName("board/showBoard");
        return modelAndView;
    }

    @GetMapping("/")
    public ModelAndView searchAllDesc(ModelAndView modelAndView) {
        List<BoardListResponseDto> boardList = boardService.searchAllDesc();
        modelAndView.addObject("boardList", boardList);
        modelAndView.setViewName("index");
        return modelAndView;
    }

    @GetMapping("/board/update/{id}")
    public ModelAndView updateForm(@PathVariable Long id, ModelAndView modelAndView) {
        BoardUpdateRequestDto board = boardService.findById(id);
        modelAndView.addObject("board",board);
        modelAndView.setViewName("board/updateBoard");
        return modelAndView;
    }

    @GetMapping("/board/verify/{id}")
    public int writerVerify(@PathVariable Long id,
                             HttpServletRequest request) {
        //작성자가 맞으면 게시글 번호 반환하고 아니면 -1을 반환하게 해야함
        System.out.println("verify board id =="+id);
        Board board = boardService.verifyWriter(id, request);
        if (board != null)
            return board.getId().intValue();
        else
            return -1;
    }

    @PutMapping("/board/{id}")
    public Long update(@PathVariable Long id, @RequestBody BoardUpdateRequestDto requestDto) {
        return boardService.update(id, requestDto);
    }

    @PostMapping("/board/delete")
    public int delete(@RequestBody BoardWriterRequestDto requestDto,
                      HttpServletRequest request) {
        long userId = Long.parseLong(requestDto.getBoardId());
        return boardService.delete(userId, request);
    }
}
