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


   /* @GetMapping("/board")
    public ModelAndView board(ModelAndView model) {
        model.setViewName("board/boardForm");
        return model;
    }*/

    @GetMapping("/board/new")
    public ModelAndView boardForm(ModelAndView modelAndView) {
        modelAndView.addObject("boardForm", new BoardResponseDto());
        modelAndView.setViewName("board/boardForm");
        return modelAndView;
    }

    @PostMapping("/board/new")
    public String create(BoardCreateRequestDto boardCreateRequestDto,
                         HttpServletRequest request) {
        return "/board/" + boardService.create(boardCreateRequestDto, request);
    }

    @GetMapping("/board/{id}")
    public BoardResponseDto searchById(@PathVariable Long id) {
        return boardService.searchById(id);
    }

    @GetMapping("/board")
    public String searchAllDesc(Model model) {
        List<BoardListResponseDto> boardList = boardService.searchAllDesc();
        model.addAttribute("boardList", boardList);

        return "index";
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
