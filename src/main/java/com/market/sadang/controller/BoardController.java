package com.market.sadang.controller;


import com.market.sadang.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RequiredArgsConstructor
@RestController
public class BoardController {

    private final BoardRepository boardRepository;

    @GetMapping("/board")
    public ModelAndView board(ModelAndView model) {
        model.setViewName("board/boardForm");
        return model;
    }
}
