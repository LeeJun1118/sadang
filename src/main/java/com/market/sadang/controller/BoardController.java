package com.market.sadang.controller;


import com.market.sadang.domain.dto.BoardCreateRequestDto;
import com.market.sadang.domain.dto.BoardListResponseDto;
import com.market.sadang.domain.dto.BoardResponseDto;
import com.market.sadang.domain.dto.BoardUpdateRequestDto;
import com.market.sadang.service.authUtil.CookieUtil;
import com.market.sadang.service.authUtil.JwtUtil;
import com.market.sadang.service.board.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
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

    @PutMapping("/board/{id}")
    public Long update(@PathVariable Long id, @RequestBody BoardUpdateRequestDto requestDto) {
        return boardService.update(id, requestDto);
    }

    @DeleteMapping("/board/{id}")
    public void delete(@PathVariable Long id) {
        boardService.delete(id);
    }
}