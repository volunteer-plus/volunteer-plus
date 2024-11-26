package volunteer.plus.backend.service.security.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.NoArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import volunteer.plus.backend.service.security.JwtSecurity;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.function.Function;

@Component
@NoArgsConstructor
public class JwtServiceIml implements JwtSecurity {

    private static final String SECRET_KEY = "462D4A614E645267556B58703273357638792F423F4528482B4B6250655368566D597133743677397A24432646294A404E635166546A576E5A72347537782141";
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
    public String generateToken(UserDetails userDetails) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("roles", userDetails.getAuthorities());
        return generateToken(hashMap, userDetails);
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
            , UserDetails userDetails){
        return Jwts
                .builder()
                .setClaims(hashMap)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000*60*60*24))
                .signWith(getSignInKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    private Key getSignInKey() {
        byte[] keysBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keysBytes);
    }
}
