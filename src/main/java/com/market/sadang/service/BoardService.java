package com.market.sadang.service;

import com.market.sadang.config.FileHandler;
import com.market.sadang.domain.Board;
import com.market.sadang.domain.Member;
import com.market.sadang.domain.MyFile;
import com.market.sadang.domain.dto.*;
import com.market.sadang.repository.BoardRepository;
import com.market.sadang.repository.MemberRepository;
import com.market.sadang.repository.MyFileRepository;
import com.market.sadang.service.authUtil.CookieUtil;
import com.market.sadang.service.authUtil.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class BoardService {

    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;
    private final CookieUtil cookieUtil;
    private final MyFileRepository fileRepository;
    private final FileHandler fileHandler;
    private final MemberService memberService;

    @Transactional
    public Long create(BoardCreateRequestDto requestDto,
                       List<MultipartFile> files) throws Exception {

        Board board = new Board(
                requestDto.getMember(),
                requestDto.getTitle(),
                requestDto.getPrice(),
                requestDto.getAddress(),
                requestDto.getContent());

        List<MyFile> fileList = fileHandler.parseFileInfo(board, files);

        files.forEach(f -> {
            if (f.getSize() != 0) {
                for (MyFile file : fileList) {
                    board.addFile(fileRepository.save(file));
                }
            }
        });

        return boardRepository.save(board).getId();
    }

    //DB에 쿼리를 날리는 부분이 없다
    //JPA 영속성 컨텍스트 : Entity를 영구 저장하는 환경
    //JPA Entity Manager 가 활성화된 상태로 트랜잭션 내에서 DB data 를 가져옴
    // ---> 영속성 컨텍스트 유지 상태
    // 영속성 컨텍스트가 유지된 상태에서 해당 data 값을 변경할 경우, 트랜잭션이 종료되는
    // 시점에 해당 테이블에 변경분을 반영함
    // ==> Entity 객체의 값만 변경하면 별도로 update 쿼리를 날릴 필요가 없음
    @Transactional
    public Long update(Long id, BoardUpdateRequestDto requestDto, List<MultipartFile> addFileList) throws Exception {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));

        List<MyFile> myFileList = fileHandler.parseFileInfo(board,addFileList);

        board.update(requestDto.getTitle(),
                requestDto.getContent());

        return id;
    }

    // readonly : 트랜잭션 범위는 유지하되 기능을 조회로 제한하여 조회 속도 개선
    @Transactional(readOnly = true)
    public BoardResponseDto searchById(Long id, List<Long> fileIdList) {
        Board board = boardRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));
        return new BoardResponseDto(board,fileIdList);
    }

    @Transactional(readOnly = true)
    public List<Board> searchAllDesc() {
        return boardRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    @Transactional
    public int delete(long id, HttpServletRequest request) {
        Board board = verifyWriter(id, request);
        if (board != null) {
            boardRepository.delete(board);
            return 1;
        } else
            return 0;
    }

    @Transactional
    public Board verifyWriter(Long id, HttpServletRequest request) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시물이 존재하지 않습니다"));
        String memberId = memberService.searchMemberId(request).getUsername();
        if (Objects.equals(board.getMember().getUsername(), memberId)) {
            return board;
        } else return null;
    }

    @Transactional
    public BoardUpdateRequestDto findById(Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시물이 존재하지 않습니다."));

        BoardUpdateRequestDto myBoard = new BoardUpdateRequestDto();
        myBoard.setId(board.getId());
        myBoard.setTitle(board.getTitle());
        myBoard.setContent(board.getContent());

        return myBoard;
    }

    public List<Board> searchParam(String search) {
//        List<Board> boardList = boardRepository.findByTitleContainingOrMember_Address(search);
        return boardRepository.findByTitleContainingAndMember_Address(search, search);
    }
}
