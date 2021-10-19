package com.market.sadang.controller;

import com.market.sadang.domain.SignUpForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RequiredArgsConstructor
@RestController
public class BoardController {

    @GetMapping("/board")
    public ModelAndView board(ModelAndView model) {
        model.setViewName("board/boardForm");
        return model;
    }
}
