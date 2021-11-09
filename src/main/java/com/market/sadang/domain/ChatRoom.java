package com.market.sadang.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


/*@Getter
@Setter
public class ChatRoom implements Serializable {

    private static final long serialVersionUID = 6494678977089006639L;

    private String roomId;
    private String name;

    public static ChatRoom create(String name) {
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.roomId = UUID.randomUUID().toString();
        chatRoom.name = name;
        return chatRoom;
    }
}*/

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ChatRoom implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column
    private String roomId;

    @Column
    private Long boardId;

    @ManyToOne
    private Member seller;

    @ManyToOne
    private Member buyer;

    @Column
    private String boardTitle;

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatMessage> chatMessage = new ArrayList<>();

    public ChatRoom(String roomId, Long boardId, Member seller, Member buyer, String boardTitle) {
        this.roomId = roomId;
        this.boardId = boardId;
        this.seller = seller;
        this.buyer = buyer;
        this.boardTitle = boardTitle;
    }

    public void deleteSeller(){
        this.seller = null;
    }

    public void deleteBuyer(){
        this.buyer = null;
    }
}