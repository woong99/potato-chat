package potatowoong.potatochat.jwt.component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import potatowoong.potatochat.jwt.dto.AccessTokenDto;

@Component
public class JwtTokenProvider implements InitializingBean {

    private Key key;

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration_time}")
    private Long expirationTime;

    @Override
    public void afterPropertiesSet() throws Exception {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * 토큰 생성
     *
     * @param authentication Authentication
     * @return AccessTokenDto
     */
    public AccessTokenDto createToken(Authentication authentication) {
        final String authorities = authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.joining(","));

        // 토큰의 expire 시간 설정
        final long now = new Date().getTime();
        final Date validity = new Date(now + this.expirationTime);

        final String jwt = Jwts.builder()
            .setSubject(authentication.getName())
            .claim("authorities", authorities)
            .signWith(this.key, SignatureAlgorithm.HS512)
            .setExpiration(validity)
            .compact();

        return AccessTokenDto.builder()
            .accessToken(jwt)
            .expireDate(validity)
            .build();
    }

    /**
     * 토큰으로부터 Authentication 객체를 가져옴
     *
     * @param token 토큰
     * @return Authentication
     */
    public Authentication getAuthentication(final String token) {
        Claims claims = Jwts
            .parserBuilder()
            .setSigningKey(this.key)
            .build()
            .parseClaimsJws(token)
            .getBody();

        Collection<? extends GrantedAuthority> authorities =
            Arrays.stream(claims.get("authorities").toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .toList();

        User principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    /**
     * 토큰 유효성 검사
     *
     * @param token 토큰
     * @return 유효하면 true, 그렇지 않으면 false
     */
    public boolean validateToken(final String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(this.key)
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
