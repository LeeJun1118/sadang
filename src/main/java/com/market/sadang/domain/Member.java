package com.market.sadang.domain;

import com.market.sadang.domain.enumType.UserRole;
import com.market.sadang.dto.form.SignUpForm;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

//lombok 어노테이션
//클래스 내 모든 필드의 Get 매소드를 자동 생성
@Getter
@Setter

//기본 생성자 자동 추가
//public 클래스명(){} 와 같은 효과
@NoArgsConstructor

//클래스에 존재하는 모든 필드에 대한 생성자 자동 생성
@AllArgsConstructor
//@EqualsAndHashCode(of = "id")


//해당 클래스의 빌더 패턴 클래스 생성
//생성자 상단에 선언 시 생성자에 포함된 필드만 빌더에 포함
@Builder

//JPA 어노테이션
//테이블과 연결될 클래스임을 나타냄
//기본 값으로 클래스의 카멜케이스 이름을 언더스코어 네이밍(_)으로 테이블 이름을 매칭(ex: SalesManager.java -> sales_manager table)
@Entity
//Spring Security는 UserDetails 객체를 통해 권한 정보를 관리하기 때문에 User 클래스에 UserDetails 를 구현하고 추가 정보를 재정의 해야함
public class Member extends BaseTimeEntity {
    //해당 테이블의 PK 필드를 나타냄
    @Id
    //PK 의 생성 규칙을 나타냄 strategy = GenerationType.IDENTITY 로 자동 증가 됨
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    //테이블의 컬럼을 나타내면 굳이 선언하지 않아도 해당 클래스의 모든 필드는 모두 컬럼이 됨.
    //기본 값 외에 추가 변경 옵션이 있을 때 사용
    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String detailAddress;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private UserRole role = UserRole.ROLE_NOT_PERMITTED;

    //CascadeType.MERGE – 트랜잭션이 종료되고 detach 상태에서 연관 엔티티를 추가하거나 변경된 이후에
    // 부모 엔티티가 merge()를 수행하게 되면 변경사항이 적용된다.(연관 엔티티의 추가 및 수정 모두 반영됨)
    @OneToMany(mappedBy = "member", cascade = CascadeType.MERGE, orphanRemoval = true)
    private List<Board> boards = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.MERGE, orphanRemoval = true)
    private List<BuyInterested> buyInterested = new ArrayList<>();

    @OneToMany(mappedBy = "sender", cascade = CascadeType.MERGE, orphanRemoval = true)
    private List<ChatMessage> sender = new ArrayList<>();

    @OneToMany(mappedBy = "receiver", cascade = CascadeType.MERGE, orphanRemoval = true)
    private List<ChatMessage> receiver = new ArrayList<>();

    @OneToMany(mappedBy = "seller", cascade = CascadeType.MERGE, orphanRemoval = true)
    private List<ChatRoom> seller = new ArrayList<>();

    @OneToMany(mappedBy = "buyer", cascade = CascadeType.MERGE, orphanRemoval = true)
    private List<ChatRoom> buyer = new ArrayList<>();

    public Member(SignUpForm signUpForm) {
        this.name = signUpForm.getName();
        this.username = signUpForm.getUsername();
        this.password = signUpForm.getPassword();
        this.email = signUpForm.getEmail();
        this.address = signUpForm.getAddress();
        this.detailAddress = signUpForm.getDetailAddress();
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", role=" + role +
                '}';
    }

    public void update(String name, String username, String email, String address, String detailAddress) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.address = address;
        this.detailAddress = detailAddress;
    }

    @Builder

    public Member(String name, String username, String password, String email, String address, String detailAddress, UserRole role) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.email = email;
        this.address = address;
        this.detailAddress = detailAddress;
        this.role = role;
    }
}
