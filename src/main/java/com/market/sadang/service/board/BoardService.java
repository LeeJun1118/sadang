package com.market.sadang.service.board;

import com.market.sadang.domain.Board;
import com.market.sadang.domain.Member;
import com.market.sadang.domain.dto.BoardCreateRequestDto;
import com.market.sadang.domain.dto.BoardListResponseDto;
import com.market.sadang.domain.dto.BoardResponseDto;
import com.market.sadang.domain.dto.BoardUpdateRequestDto;
import com.market.sadang.repository.BoardRepository;
import com.market.sadang.repository.MemberRepository;
import com.market.sadang.service.authUtil.CookieUtil;
import com.market.sadang.service.authUtil.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.swing.plaf.metal.MetalMenuBarUI;
import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class BoardService {

    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;
    private final CookieUtil cookieUtil;

    @Transactional
    public Long create(BoardCreateRequestDto requestDto, HttpServletRequest request) {
        Cookie jwtToken = cookieUtil.getCookie(request, "accessToken");
        String memberId = jwtUtil.getUserId(jwtToken.getValue());
        Member member = memberRepository.findByUserId(memberId);
        requestDto.setMember(member);
        return boardRepository.save(requestDto.toEntity()).getId();
    }

    //DB에 쿼리를 날리는 부분이 없다
    //JPA 영속성 컨텍스트 : Entity를 영구 저장하는 환경
    //JPA Entity Manager 가 활성화된 상태로 트랜잭션 내에서 DB data 를 가져옴
    // ---> 영속성 컨텍스트 유지 상태
    // 영속성 컨텍스트가 유지된 상태에서 해당 data 값을 변경할 경우, 트랜잭션이 종료되는
    // 시점에 해당 테이블에 변경분을 반영함
    // ==> Entity 객체의 값만 변경하면 별도로 update 쿼리를 날릴 필요가 없음
    @Transactional
    public Long update(Long id, BoardUpdateRequestDto requestDto) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));

        board.update(requestDto.getTitle(),
                requestDto.getContent());

        return id;
    }

    // readonly : 트랜잭션 범위는 유지하되 기능을 조회로 제한하여 조회 속도 개선
    @Transactional(readOnly = true)
    public BoardResponseDto searchById(Long id) {
        Board board = boardRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));

        return new BoardResponseDto(board);
    }

    @Transactional(readOnly = true)
    public List<BoardListResponseDto> searchAllDesc(){
        return boardRepository.findAll(Sort.by(Sort.Direction.DESC,"id")).stream()
                .map(BoardListResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void delete(Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시물이 존재하지 않습니다"));
        boardRepository.delete(board);
    }
}