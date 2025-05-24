package volunteer.plus.backend.service.security.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import volunteer.plus.backend.domain.dto.TokenPairResponse;
import volunteer.plus.backend.domain.enums.TokenClaim;
import volunteer.plus.backend.service.security.TokenFactoryService;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class TokenFactoryServiceIml implements TokenFactoryService {

    private static final String SECRET_KEY = "462D4A614E645267556B58703273357638792F423F4528482B4B6250655368566D597133743677397A24432646294A404E635166546A576E5A72347537782141";
    private static final Long JWT_TOKEN_EXPIRATION_TIME = 43200000L;
    private static final Long REFRESH_TOKEN_EXPIRATION_TIME = 86400000L;

    @Override
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    @Override
    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    @Override
    public <T> T extractClaim(String token, Function<Claims, T> resolveClaims) {
        var claims = extractAllClaims(token);
        return resolveClaims.apply(claims);
    }

    @Override
    public TokenPairResponse generateTokenPair(UserDetails userDetails) {
        String accessToken = generateJwtToken(userDetails);
        String refreshToken = generateRefreshToken(userDetails);
        return TokenPairResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    private String generateJwtToken(UserDetails userDetails) {
        HashMap<String, Object> claims = new HashMap<>();
        claims.put(TokenClaim.ROLES.getName(), userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList());

        return generateToken(claims, userDetails, System.currentTimeMillis() + JWT_TOKEN_EXPIRATION_TIME);
    }

    private String generateRefreshToken(UserDetails userDetails) {
        HashMap<String, Object> claims = new HashMap<>();
        return generateToken(claims, userDetails, System.currentTimeMillis() +  REFRESH_TOKEN_EXPIRATION_TIME);
    }

    @Override
    public boolean isTokenValid(String token, UserDetails userDetails) {
        return (userDetails.getUsername().equals(extractUsername(token)) &&
                isTokenNonExpired(token));
    }

    private Date extractExpiration(String token){
        return extractClaim(token, Claims::getExpiration);
    }

    private boolean isTokenNonExpired(String token){
        return extractExpiration(token).after(new Date(System.currentTimeMillis()));
    }

    private String generateToken(HashMap<String, Object> hashMap
            , UserDetails userDetails, long expirationTime){
        return Jwts
                .builder()
                .setClaims(hashMap)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(expirationTime))
                .signWith(getSignInKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    private Key getSignInKey() {
        byte[] keysBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keysBytes);
    }

}
