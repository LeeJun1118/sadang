package com.market.sadang.service.authUtil;


import com.market.sadang.domain.Member;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@RequiredArgsConstructor
@Component
public class JwtUtil {
    // 1000L = 1초
    // 10초
    public final static long TOKEN_VALIDATION_SECOND = 1000L * 60 * 60;
    // 60분
    public final static long REFRESH_TOKEN_VALIDATION_SECOND = 1000L * 60 * 60 * 24;

    final static public String ACCESS_TOKEN_NAME = "accessToken";
    final static public String REFRESH_TOKEN_NAME = "refreshToken";

    private final MyUserDetailService userDetailService;

    @Value("${spring.jwt.secret}")
    private String SECRET_KEY;

    private Key getSigningKey(String secretKey){
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    //토큰이 유효한 토큰인지 검사한 후, 토큰에 담긴 Payload 값을 가져온다.
    public Claims extractAllClaims(String token) throws ExpiredJwtException{
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey(SECRET_KEY))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    //추출한 Payload로부터 Username 가져온다.
    public String getUsername(String token){
        return extractAllClaims(token).get("username",String.class);
    }

    //토큰이 만료됐는지 아닌지 확인
    public Boolean isTokenExpired(String token){
        final Date expiration = extractAllClaims(token).getExpiration();
        return expiration.before(new Date());
    }

    //Access, Refresh Token 형성
    public String generateToken(Member member){
        return doGenerateToken(member.getUsername(),TOKEN_VALIDATION_SECOND);
    }
    public String generateRefreshToken(Member member){
        return doGenerateToken(member.getUsername(),REFRESH_TOKEN_VALIDATION_SECOND);
    }

    //토큰 생성, 페이로드에 담길 값은 Username
    private String doGenerateToken(String username, long expireTime) {
        Claims claims = Jwts.claims();
        claims.put("username",username);

        String jwt = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expireTime))
                .signWith(getSigningKey(SECRET_KEY), SignatureAlgorithm.HS256)
                .compact();
        return jwt;
    }

    public Boolean validateToken(String token, UserDetails userDetails){
        final String username = getUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public Boolean validateToken(String token){
        final String username = getUsername(token);
        UserDetails userDetails = userDetailService.loadUserByUsername(username);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
