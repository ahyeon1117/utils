package com.hyeon.backend.utils;

import com.hyeon.backend.dto.JwtTokenVerify;
import com.hyeon.backend.exceptions.JwtNotFoundException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;

public class JwtUtils {

  @Value("${szs.secretKey.jwt}")
  private String secretKey;

  @Value("${szs.periodMinute}")
  private Long periodMinute;

  SecretKey key = Jwts.SIG.HS256.key().build();

  @PostConstruct
  protected void init() {
    key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
  }

  public String generateToken(String id) {
    // refreshToken과 accessToken을 생성한다.
    long tokenPeriod = 1000L * 60L * periodMinute; // 30분
    // 새로운 클레임 객체를 생성하고, 이메일과 역할(권한)을 셋팅
    Map<String, String> claims = new HashMap<>();
    claims.put("id", id);

    Date now = new Date();
    return Jwts
      .builder()
      //    Payload를 구성하는 속성들을 정의한다.
      .claims(claims)
      // 발행일자를 넣는다.
      .issuedAt(now)
      // 토큰의 만료일시를 설정한다.
      .expiration(new Date(now.getTime() + tokenPeriod))
      // 지정된 서명 알고리즘과 비밀 키를 사용하여 토큰을 서명한다.
      .signWith(key)
      .compact();
  }

  public JwtTokenVerify verifyToken(String token) {
    JwtTokenVerify jwtTokenVerify = new JwtTokenVerify(false, "");
    try {
      Jws<Claims> claims = Jwts
        .parser()
        .verifyWith(key) // 비밀키를 설정하여 파싱한다.
        .build()
        .parseSignedClaims(token); // 주어진 토큰을 파싱하여 Claims 객체를 얻는다.

      // 토큰의 만료 시간과 현재 시간비교
      boolean expired = claims.getPayload().getExpiration().after(new Date());
      // 만료 시간이 현재 시간 이후인지 확인하여 유효성 검사 결과를 반환
      if (!expired) {
        jwtTokenVerify.setErr("different issuer");
      }
      jwtTokenVerify.setValid(true);
    } catch (ExpiredJwtException e) {
      jwtTokenVerify.setErr("expired token");
    } catch (JwtException | IllegalArgumentException e) {
      jwtTokenVerify.setErr("invalid token");
    }
    return jwtTokenVerify;
  }

  public String resolveToken(HttpServletRequest req) {
    String bearerToken = req.getHeader("Authorization");
    if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
      return bearerToken.substring(7, bearerToken.length());
    }
    throw new JwtNotFoundException("Authorization Header Does not Exists");
  }

  public String getUid(String token) {
    return Jwts
      .parser()
      .verifyWith(key)
      .build()
      .parseSignedClaims(token)
      .getPayload()
      .getOrDefault("id", String.class)
      .toString();
  }
}
