package com.market.sadang.init;

import lombok.Data;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;

@Data
public class AddressVO {

    private HashMap<Integer, String> cityMap = new HashMap<>();
    private HashMap<String, ArrayList<String>> streetMap = new HashMap<>();

    @PostConstruct
    public int getCityMapLength(){
        return cityMap.size();
    }

    public AddressVO() {
        cityMap.put(1, "서울특별시");
        cityMap.put(2, "부산광역시");
        cityMap.put(3, "대구광역시");
        cityMap.put(4, "인천광역시");
        cityMap.put(5, "광주광역시");
        cityMap.put(6, "대전광역시");
        cityMap.put(7, "울산광역시");
        cityMap.put(8, "세종특별자치시");
        cityMap.put(9, "경기도");
        cityMap.put(10, "강원도");
        cityMap.put(11, "충청북도");
        cityMap.put(12, "충청남도");
        cityMap.put(13, "전라북도");
        cityMap.put(14, "전라남도");
        cityMap.put(15, "경상북도");
        cityMap.put(16, "경상남도");
        cityMap.put(17, "제주특별자치도");

        for (Integer integer : cityMap.keySet()) {
            streetMap.put(cityMap.get(integer), new ArrayList<>());
        }

        // 서울
        streetMap.get(cityMap.get(1)).add("종로구");
        streetMap.get(cityMap.get(1)).add("중구");
        streetMap.get(cityMap.get(1)).add("용산구");
        streetMap.get(cityMap.get(1)).add("성동구");
        streetMap.get(cityMap.get(1)).add("광진구");
        streetMap.get(cityMap.get(1)).add("동대문구");
        streetMap.get(cityMap.get(1)).add("중랑구");
        streetMap.get(cityMap.get(1)).add("성북구");
        streetMap.get(cityMap.get(1)).add("강북구");
        streetMap.get(cityMap.get(1)).add("도봉구");
        streetMap.get(cityMap.get(1)).add("노원구");
        streetMap.get(cityMap.get(1)).add("은평구");
        streetMap.get(cityMap.get(1)).add("서대문구");
        streetMap.get(cityMap.get(1)).add("마포구");
        streetMap.get(cityMap.get(1)).add("양천구");
        streetMap.get(cityMap.get(1)).add("강서구");
        streetMap.get(cityMap.get(1)).add("구로구");
        streetMap.get(cityMap.get(1)).add("금천구");
        streetMap.get(cityMap.get(1)).add("영등포구");
        streetMap.get(cityMap.get(1)).add("동작구");
        streetMap.get(cityMap.get(1)).add("관악구");
        streetMap.get(cityMap.get(1)).add("서초구");
        streetMap.get(cityMap.get(1)).add("강남구");
        streetMap.get(cityMap.get(1)).add("송파구");
        streetMap.get(cityMap.get(1)).add("강동구");


        // 부산
        streetMap.get(cityMap.get(2)).add("중구");
        streetMap.get(cityMap.get(2)).add("서구");
        streetMap.get(cityMap.get(2)).add("동구");
        streetMap.get(cityMap.get(2)).add("영도구");
        streetMap.get(cityMap.get(2)).add("부산진구");
        streetMap.get(cityMap.get(2)).add("동래구");
        streetMap.get(cityMap.get(2)).add("남구");
        streetMap.get(cityMap.get(2)).add("북구");
        streetMap.get(cityMap.get(2)).add("해운대구");
        streetMap.get(cityMap.get(2)).add("사하구");
        streetMap.get(cityMap.get(2)).add("금정구");
        streetMap.get(cityMap.get(2)).add("강서구");
        streetMap.get(cityMap.get(2)).add("연제구");
        streetMap.get(cityMap.get(2)).add("수영구");
        streetMap.get(cityMap.get(2)).add("사상구");
        streetMap.get(cityMap.get(2)).add("기장군");

        //대구
        streetMap.get(cityMap.get(3)).add("중구");
        streetMap.get(cityMap.get(3)).add("동구");
        streetMap.get(cityMap.get(3)).add("서구");
        streetMap.get(cityMap.get(3)).add("남구");
        streetMap.get(cityMap.get(3)).add("북구");
        streetMap.get(cityMap.get(3)).add("수성구");
        streetMap.get(cityMap.get(3)).add("달서구");
        streetMap.get(cityMap.get(3)).add("달성군");

        //인천
        streetMap.get(cityMap.get(4)).add("중구");
        streetMap.get(cityMap.get(4)).add("동구");
        streetMap.get(cityMap.get(4)).add("남구");
        streetMap.get(cityMap.get(4)).add("연수구");
        streetMap.get(cityMap.get(4)).add("남동구");
        streetMap.get(cityMap.get(4)).add("부평구");
        streetMap.get(cityMap.get(4)).add("계양구");
        streetMap.get(cityMap.get(4)).add("서구");
        streetMap.get(cityMap.get(4)).add("강화군");
        streetMap.get(cityMap.get(4)).add("옹진군");

        //광주
        streetMap.get(cityMap.get(5)).add("동구");
        streetMap.get(cityMap.get(5)).add("서구");
        streetMap.get(cityMap.get(5)).add("남구");
        streetMap.get(cityMap.get(5)).add("북구");
        streetMap.get(cityMap.get(5)).add("광산구");

        //대전
        streetMap.get(cityMap.get(6)).add("동구");
        streetMap.get(cityMap.get(6)).add("중구");
        streetMap.get(cityMap.get(6)).add("서구");
        streetMap.get(cityMap.get(6)).add("유성구");
        streetMap.get(cityMap.get(6)).add("대덕구");

        //울산
        streetMap.get(cityMap.get(7)).add("중구");
        streetMap.get(cityMap.get(7)).add("남구");
        streetMap.get(cityMap.get(7)).add("동구");
        streetMap.get(cityMap.get(7)).add("북구");
        streetMap.get(cityMap.get(7)).add("울주군");

        //세종
        streetMap.get(cityMap.get(8)).add("세종시");
        //경기도
        streetMap.get(cityMap.get(9)).add("수원시장안구");
        streetMap.get(cityMap.get(9)).add("수원시권선구");
        streetMap.get(cityMap.get(9)).add("수원시팔달구");
        streetMap.get(cityMap.get(9)).add("수원시영통구");
        streetMap.get(cityMap.get(9)).add("성남시수정구");
        streetMap.get(cityMap.get(9)).add("성남시중원구");
        streetMap.get(cityMap.get(9)).add("성남시분당구");
        streetMap.get(cityMap.get(9)).add("의정부시");
        streetMap.get(cityMap.get(9)).add("안양시만안구");
        streetMap.get(cityMap.get(9)).add("안양시동안구");
        streetMap.get(cityMap.get(9)).add("부천시원미구");
        streetMap.get(cityMap.get(9)).add("부천시소사구");
        streetMap.get(cityMap.get(9)).add("부천시오정구");
        streetMap.get(cityMap.get(9)).add("광명시");
        streetMap.get(cityMap.get(9)).add("평택시");
        streetMap.get(cityMap.get(9)).add("동두천시");
        streetMap.get(cityMap.get(9)).add("안산시상록구");
        streetMap.get(cityMap.get(9)).add("안산시단원구");
        streetMap.get(cityMap.get(9)).add("고양시덕양구");
        streetMap.get(cityMap.get(9)).add("고양시일산동구");
        streetMap.get(cityMap.get(9)).add("고양시일산서구");
        streetMap.get(cityMap.get(9)).add("과천시");
        streetMap.get(cityMap.get(9)).add("구리시");
        streetMap.get(cityMap.get(9)).add("남양주시");
        streetMap.get(cityMap.get(9)).add("오산시");
        streetMap.get(cityMap.get(9)).add("시흥시");
        streetMap.get(cityMap.get(9)).add("군포시");
        streetMap.get(cityMap.get(9)).add("의왕시");
        streetMap.get(cityMap.get(9)).add("하남시");
        streetMap.get(cityMap.get(9)).add("용인시처인구");
        streetMap.get(cityMap.get(9)).add("용인시기흥구");
        streetMap.get(cityMap.get(9)).add("용인시수지구");
        streetMap.get(cityMap.get(9)).add("파주시");
        streetMap.get(cityMap.get(9)).add("이천시");
        streetMap.get(cityMap.get(9)).add("안성시");
        streetMap.get(cityMap.get(9)).add("김포시");
        streetMap.get(cityMap.get(9)).add("화성시");
        streetMap.get(cityMap.get(9)).add("광주시");
        streetMap.get(cityMap.get(9)).add("양주시");
        streetMap.get(cityMap.get(9)).add("포천시");
        streetMap.get(cityMap.get(9)).add("여주시");
        streetMap.get(cityMap.get(9)).add("연천군");
        streetMap.get(cityMap.get(9)).add("가평군");
        streetMap.get(cityMap.get(9)).add("양평군");

        //강원도
        streetMap.get(cityMap.get(10)).add("춘천시");
        streetMap.get(cityMap.get(10)).add("원주시");
        streetMap.get(cityMap.get(10)).add("강릉시");
        streetMap.get(cityMap.get(10)).add("동해시");
        streetMap.get(cityMap.get(10)).add("태백시");
        streetMap.get(cityMap.get(10)).add("속초시");
        streetMap.get(cityMap.get(10)).add("삼척시");
        streetMap.get(cityMap.get(10)).add("홍천군");
        streetMap.get(cityMap.get(10)).add("영월군");
        streetMap.get(cityMap.get(10)).add("평창군");
        streetMap.get(cityMap.get(10)).add("정선군");
        streetMap.get(cityMap.get(10)).add("철원군");
        streetMap.get(cityMap.get(10)).add("화천군");
        streetMap.get(cityMap.get(10)).add("양구군");
        streetMap.get(cityMap.get(10)).add("인제군");
        streetMap.get(cityMap.get(10)).add("고성군");
        streetMap.get(cityMap.get(10)).add("양양군");

        //충청북도
        streetMap.get(cityMap.get(11)).add("청주시상당구");
        streetMap.get(cityMap.get(11)).add("청주시흥덕구");
        streetMap.get(cityMap.get(11)).add("충주시");
        streetMap.get(cityMap.get(11)).add("제천시");
        streetMap.get(cityMap.get(11)).add("청원군");
        streetMap.get(cityMap.get(11)).add("보은군");
        streetMap.get(cityMap.get(11)).add("옥천군");
        streetMap.get(cityMap.get(11)).add("영동군");
        streetMap.get(cityMap.get(11)).add("진천군");
        streetMap.get(cityMap.get(11)).add("괴산군");
        streetMap.get(cityMap.get(11)).add("음성군");
        streetMap.get(cityMap.get(11)).add("단양군");
        streetMap.get(cityMap.get(11)).add("증평군");

        //충청남도
        streetMap.get(cityMap.get(12)).add("천안시동남구");
        streetMap.get(cityMap.get(12)).add("천안시서북구");
        streetMap.get(cityMap.get(12)).add("공주시");
        streetMap.get(cityMap.get(12)).add("보령시");
        streetMap.get(cityMap.get(12)).add("아산시");
        streetMap.get(cityMap.get(12)).add("서산시");
        streetMap.get(cityMap.get(12)).add("논산시");
        streetMap.get(cityMap.get(12)).add("계룡시");
        streetMap.get(cityMap.get(12)).add("당진시");
        streetMap.get(cityMap.get(12)).add("금산군");
        streetMap.get(cityMap.get(12)).add("부여군");
        streetMap.get(cityMap.get(12)).add("서천군");
        streetMap.get(cityMap.get(12)).add("청양군");
        streetMap.get(cityMap.get(12)).add("홍성군");
        streetMap.get(cityMap.get(12)).add("예산군");
        streetMap.get(cityMap.get(12)).add("태안군");

        //전라북도
        streetMap.get(cityMap.get(13)).add("전주시완산구");
        streetMap.get(cityMap.get(13)).add("전주시덕진구");
        streetMap.get(cityMap.get(13)).add("군산시");
        streetMap.get(cityMap.get(13)).add("익산시");
        streetMap.get(cityMap.get(13)).add("정읍시");
        streetMap.get(cityMap.get(13)).add("남원시");
        streetMap.get(cityMap.get(13)).add("김제시");
        streetMap.get(cityMap.get(13)).add("완주군");
        streetMap.get(cityMap.get(13)).add("진안군");
        streetMap.get(cityMap.get(13)).add("무주군");
        streetMap.get(cityMap.get(13)).add("장수군");
        streetMap.get(cityMap.get(13)).add("임실군");
        streetMap.get(cityMap.get(13)).add("순창군");
        streetMap.get(cityMap.get(13)).add("고창군");
        streetMap.get(cityMap.get(13)).add("부안군");

        //전라남도
        streetMap.get(cityMap.get(14)).add("목포시");
        streetMap.get(cityMap.get(14)).add("여수시");
        streetMap.get(cityMap.get(14)).add("순천시");
        streetMap.get(cityMap.get(14)).add("나주시");
        streetMap.get(cityMap.get(14)).add("광양시");
        streetMap.get(cityMap.get(14)).add("담양군");
        streetMap.get(cityMap.get(14)).add("곡성군");
        streetMap.get(cityMap.get(14)).add("구례군");
        streetMap.get(cityMap.get(14)).add("고흥군");
        streetMap.get(cityMap.get(14)).add("보성군");
        streetMap.get(cityMap.get(14)).add("화순군");
        streetMap.get(cityMap.get(14)).add("장흥군");
        streetMap.get(cityMap.get(14)).add("강진군");
        streetMap.get(cityMap.get(14)).add("해남군");
        streetMap.get(cityMap.get(14)).add("영암군");
        streetMap.get(cityMap.get(14)).add("무안군");
        streetMap.get(cityMap.get(14)).add("함평군");
        streetMap.get(cityMap.get(14)).add("영광군");
        streetMap.get(cityMap.get(14)).add("장성군");
        streetMap.get(cityMap.get(14)).add("완도군");
        streetMap.get(cityMap.get(14)).add("진도군");
        streetMap.get(cityMap.get(14)).add("신안군");

        //경상북도
        streetMap.get(cityMap.get(15)).add("포항시남구");
        streetMap.get(cityMap.get(15)).add("포항시북구");
        streetMap.get(cityMap.get(15)).add("경주시");
        streetMap.get(cityMap.get(15)).add("김천시");
        streetMap.get(cityMap.get(15)).add("안동시");
        streetMap.get(cityMap.get(15)).add("구미시");
        streetMap.get(cityMap.get(15)).add("영주시");
        streetMap.get(cityMap.get(15)).add("영천시");
        streetMap.get(cityMap.get(15)).add("상주시");
        streetMap.get(cityMap.get(15)).add("문경시");
        streetMap.get(cityMap.get(15)).add("경산시");
        streetMap.get(cityMap.get(15)).add("군위군");
        streetMap.get(cityMap.get(15)).add("의성군");
        streetMap.get(cityMap.get(15)).add("청송군");
        streetMap.get(cityMap.get(15)).add("영양군");
        streetMap.get(cityMap.get(15)).add("청도군");
        streetMap.get(cityMap.get(15)).add("고령군");
        streetMap.get(cityMap.get(15)).add("성주군");
        streetMap.get(cityMap.get(15)).add("칠곡군");
        streetMap.get(cityMap.get(15)).add("예천군");
        streetMap.get(cityMap.get(15)).add("봉화군");
        streetMap.get(cityMap.get(15)).add("울진군");
        streetMap.get(cityMap.get(15)).add("울릉군");

        //경상남도
        streetMap.get(cityMap.get(16)).add("진주시");
        streetMap.get(cityMap.get(16)).add("통영시");
        streetMap.get(cityMap.get(16)).add("사천시");
        streetMap.get(cityMap.get(16)).add("김해시");
        streetMap.get(cityMap.get(16)).add("밀양시");
        streetMap.get(cityMap.get(16)).add("거제시");
        streetMap.get(cityMap.get(16)).add("양산시");
        streetMap.get(cityMap.get(16)).add("창원시의창구");
        streetMap.get(cityMap.get(16)).add("창원시마산합포구");
        streetMap.get(cityMap.get(16)).add("창원시진해구");
        streetMap.get(cityMap.get(16)).add("의령군");
        streetMap.get(cityMap.get(16)).add("함안군");
        streetMap.get(cityMap.get(16)).add("창녕군");
        streetMap.get(cityMap.get(16)).add("고성군");
        streetMap.get(cityMap.get(16)).add("남해군");
        streetMap.get(cityMap.get(16)).add("하동군");
        streetMap.get(cityMap.get(16)).add("산청군");
        streetMap.get(cityMap.get(16)).add("함양군");
        streetMap.get(cityMap.get(16)).add("거창군");
        streetMap.get(cityMap.get(16)).add("합천군");
        //제주
        streetMap.get(cityMap.get(17)).add("제주시");
        streetMap.get(cityMap.get(17)).add("서귀포시");
    }

}
