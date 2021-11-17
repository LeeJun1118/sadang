package com.market.sadang.init;

import com.market.sadang.domain.enumType.UserRole;
import com.market.sadang.domain.Member;
import com.market.sadang.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
@RequiredArgsConstructor
public class InitMember {
    private final InitService initService;
    private final MemberRepository memberRepository;

    @PostConstruct
    public void init() {
        if (memberRepository.findAll().size() == 0) {
            initService.initDB();
        }

    }
    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {

        private final String[] randomNames = {
                "이준", "전창희", "박진석", "이상지", "송성훈", "김혜석", "김장군", "최정웅", "서재균", "고상희", "이승호",
                "이세연", "홍승현", "최주원", "변지경", "최은아", "이민서", "권주안", "진하은", "김소경", "임수영", "정유진"
        };


        private final String[] randomUsernames = {
                "jun", "ss", "dd", "ff", "송성훈", "qq", "ww", "ee", "rr", "tt", "zz",
                "xx", "cc", "vv", "gg", "qwer", "asdf", "test", "test1", "test2", "test3", "test4"
        };

        private List<String> randomEmails = new ArrayList<String>();

        private final EntityManager em;

        private AddressVO addressVO = new AddressVO();
        private int cityCount = addressVO.getCityMapLength();


        public void initDB() {
            for (int i = 1; i < randomNames.length+1; i++) {
                randomEmails.add("example" + i + "@gmail.com");
            }
            for (int i = 1; i < randomNames.length+1; i++) {
                memberCreate(i);
            }
        }

        private void memberCreate(int i) {
            HashMap<Integer, String> cityMap = addressVO.getCityMap();
            HashMap<String, ArrayList<String>> streetMap = addressVO.getStreetMap();
            String city;
            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

            if(i > cityCount){
                city = cityMap.get(i % cityCount);
            }
            else{
                city = cityMap.get(i);
            }
            String street = streetMap.get(city).get(0);
            Address address = getAddress(
                    city,
                    street,
                    "OOO로 XXX",
                    "00000"
            );

            String detailAddress = " 상세 주소 Example";

            Member member = getMember(
                    randomNames[i-1],
                    randomUsernames[i-1],
                    bCryptPasswordEncoder.encode("1234"),
                    randomEmails.get(i-1),
                    address,
                    detailAddress

            );
            em.persist(member);
        }

        private Member getMember(String name, String username, String password, String email, Address address1, String detailAddress) {
            return new Member(
                    name,
                    username,
                    password,
                    email,
                    address1.getCity() + " " + address1.getStreet1(),
                    detailAddress,
                    UserRole.ROLE_USER
            );
        }

        private Address getAddress(String city, String street1, String street2, String zipcode) {
            return new Address(
                    city,
                    street1,
                    street2,
                    zipcode
            );
        }
    }
}