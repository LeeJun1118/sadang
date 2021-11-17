package com.market.sadang.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.market.sadang.domain.enumType.BoardStatus;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class BuyInterested {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    //CascadeType.MERGE – 트랜잭션이 종료되고 detach 상태에서 연관 엔티티를 추가하거나 변경된 이후에
    // 부모 엔티티가 merge()를 수행하게 되면 변경사항이 적용된다.(연관 엔티티의 추가 및 수정 모두 반영됨)
    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(updatable = false)
    @JsonBackReference
    private Member member;

    @ManyToOne
    @JoinColumn(updatable = false)
    @JsonBackReference
    private Board board;

    @Column
    @Enumerated(EnumType.STRING)
    private BoardStatus buyStatus = BoardStatus.none;

    @Column
    @Enumerated(EnumType.STRING)
    private BoardStatus interestedStatus = BoardStatus.none;


    @Builder
    public BuyInterested(Member member, Board board, BoardStatus buyStatus, BoardStatus interestedStatus) {
        this.member = member;
        this.board = board;
        this.buyStatus = buyStatus;
        this.interestedStatus = interestedStatus;
    }

    public void buy(BoardStatus status){
        this.buyStatus = status;
    }

    public void interested(BoardStatus status){
        this.interestedStatus = status;
    }
}
