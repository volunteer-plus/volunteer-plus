package volunteer.plus.backend.service.security;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;
import volunteer.plus.backend.domain.dto.TokenPairResponse;

import java.util.function.Function;


public interface TokenFactoryService {

    String extractUsername(String token);

    Claims extractAllClaims(String token);

    <T> T extractClaim(String token, Function<Claims, T> resolveClaims);

    TokenPairResponse generateTokenPair(UserDetails userDetails);

    boolean isTokenValid(String token, UserDetails userDetails);

}
