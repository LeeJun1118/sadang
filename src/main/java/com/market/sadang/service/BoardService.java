package com.market.sadang.service;

import com.market.sadang.config.handler.FileHandler;
import com.market.sadang.domain.*;
import com.market.sadang.domain.enumType.BoardStatus;
import com.market.sadang.dto.bord.BoardCreateRequestDto;
import com.market.sadang.dto.bord.BoardMemberDto;
import com.market.sadang.dto.bord.BoardResponseDto;
import com.market.sadang.dto.bord.BoardUpdateRequestDto;
import com.market.sadang.dto.member.MyBoardListResponseDto;
import com.market.sadang.repository.BoardRepository;
import com.market.sadang.repository.BuyInterestedRepository;
import com.market.sadang.repository.MyFileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@RequiredArgsConstructor
@Service
public class BoardService {

    private final BoardRepository boardRepository;
    private final MyFileRepository fileRepository;
    private final FileHandler fileHandler;
    private final MemberService memberService;
    private final BuyInterestedRepository buyInterestedRepository;
    private final BuyInterestedService buyInterestedService;


    @Transactional
    public Long create(BoardCreateRequestDto requestDto,
                       List<MultipartFile> files) throws Exception {

        Board board = new Board(
                requestDto.getMember(),
                requestDto.getTitle(),
                requestDto.getPrice(),
                requestDto.getContent(),
                requestDto.getAddress());

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

        List<MyFile> myFileList = fileHandler.parseFileInfo(board, addFileList);

        board.update(requestDto.getTitle(),
                requestDto.getPrice(),
                requestDto.getContent());

        return id;
    }

    @Transactional
    public void sellerStatus(Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));

        if (board.getSellStatus() == BoardStatus.sell)
            board.sellerStatus(BoardStatus.sold);
        else
            board.sellerStatus(BoardStatus.sell);
    }

    // readonly : 트랜잭션 범위는 유지하되 기능을 조회로 제한하여 조회 속도 개선
    @Transactional(readOnly = true)
    public BoardResponseDto searchById(Long id, List<Long> fileIdList) {
        Board board = boardRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));
        return new BoardResponseDto(board, fileIdList);
    }

    @Transactional(readOnly = true)
    public List<Board> searchAllDesc() {
        return boardRepository.findAllBySellStatus(Sort.by(Sort.Direction.DESC, "id"), BoardStatus.sell);
    }

    @Transactional
    public void delete(long id) {
        Board board = verifyWriter(id);
        if (board != null)
            boardRepository.delete(board);
    }

    @Transactional
    public Board verifyWriter(Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시물이 존재하지 않습니다"));
        Member member = memberService.findByMemberRequest();
        if (Objects.equals(board.getMember(), member)) {
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
        myBoard.setPrice(board.getPrice());

        return myBoard;
    }

    @Transactional
    public Board findByIdBoard(Long id) {
        return boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시물이 존재하지 않습니다."));
    }

    @Transactional
    public BoardMemberDto findByIdMember(Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시물이 존재하지 않습니다."));

        BoardMemberDto myBoard = new BoardMemberDto(board);
        return myBoard;
    }

    public List<Board> searchParam(String search) {
        return boardRepository.findAllByTitleContainingOrAddressContainingAndSellStatus(search, search, BoardStatus.sell);
    }

    public List<MyBoardListResponseDto> boardListMemberAndBoardStatus(Member member, BoardStatus status) {
        List<Board> boardList = findByMemberAndBoardStatus(member, status);
        List<MyBoardListResponseDto> dtoList = new ArrayList<>();

        if (boardList != null) {
            for (Board board : boardList) {
                dtoList.add(new MyBoardListResponseDto(board));
            }
        }
        return dtoList;
    }

    // 해당 사용자가 팔고 있는 모든 게시글의 수
    public int countAllByMemberBoardStatus(Member member, BoardStatus status) {
        return findByMemberAndBoardStatus(member, status).size();
    }


    // 작성자와 BoardStatus 로 모든 게시글 찾기
    public List<Board> findByMemberAndBoardStatus(Member member, BoardStatus status) {
        return boardRepository.findAllByMemberAndSellStatus(member, status);
    }

    // 구매 상품 등록, 해제
    @Transactional
    public void buy(Long boardId) {
        Member member = memberService.findByMemberRequest();
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시물이 존재하지 않습니다."));
        BuyInterested buyInterested = buyInterestedRepository.findByBoardAndMember(board, member);

        if (buyInterested != null) {
            if (buyInterested.getBuyStatus() == BoardStatus.buy)
                buyInterested.buy(BoardStatus.none);
            else
                buyInterested.buy(BoardStatus.buy);
        } else
            buyInterestedRepository.save(new BuyInterested(member, board, BoardStatus.buy, BoardStatus.none));

    }

    // 관심 상품 등록, 해제
    @Transactional
    public void interested(Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));
        Member member = memberService.findByMemberRequest();
        BuyInterested buyInterested = buyInterestedRepository.findByBoardAndMember(board, member);

        if (buyInterested == null) {
            buyInterestedRepository.save(new BuyInterested(member, board, BoardStatus.none, BoardStatus.interested));
        } else {
            if (buyInterested.getInterestedStatus() == BoardStatus.interested)
                buyInterested.interested(BoardStatus.none);
            else
                buyInterested.interested(BoardStatus.interested);
        }
    }

    // 사용자의 구매리스트 또는 관심 리스트
    public List<MyBoardListResponseDto> findBoardListByMemberAndBuyStatusOrInterestedStatus(BoardStatus status) {
        Member member = memberService.findByMemberRequest();
        List<BuyInterested> buyOrInterestedList = null;
        List<MyBoardListResponseDto> dtoList = new ArrayList<>();
        Board board;

        // status 로 구매 리스트, 관심 리스트 정함
        buyOrInterestedList = buyInterestedService.findByMemberAndBuyStatusOrInterestedStatus(member, status);

        if (buyOrInterestedList == null)
            return null;
        else {
            for (BuyInterested buyInterested : buyOrInterestedList) {
                board = boardRepository.findById(buyInterested.getBoard().getId())
                        .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));
                dtoList.add(new MyBoardListResponseDto(board));
            }
            return dtoList;
        }
    }
}
