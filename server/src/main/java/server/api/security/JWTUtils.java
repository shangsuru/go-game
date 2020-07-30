package server.api.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;


@Component
public class JWTUtils {

  private final long EXPIRATION_TIME = 86_400_000; // 1 day
  private final String secret;
  private CustomUserDetailsService userDetailsService;

  public JWTUtils(Environment env, CustomUserDetailsService userDetailsService) {
    this.userDetailsService = userDetailsService;
    this.secret = env.getProperty("security.jwt.token.secret");
  }

  public String createJWT(String username) {
    return JWT.create()
      .withSubject(username)
      .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
      .sign(HMAC512(secret.getBytes()));
  }

  public String createJWT(String username, long expirationTime) {
    return JWT.create()
      .withSubject(username)
      .withExpiresAt(new Date(System.currentTimeMillis() + expirationTime))
      .sign(HMAC512(secret.getBytes()));
  }

  public Authentication getAuthentication(String token) {
    UserDetails userDetails = this.userDetailsService.loadUserByUsername(getUsernameFromJWT(token));
    return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
  }

  public String getUsernameFromJWT(String token) {
    return JWT.decode(token).getSubject();
  }

  public boolean validateJWT(String token) {
    try {
      DecodedJWT jwt = JWT.require(Algorithm.HMAC512(secret.getBytes())).build().verify(token);
      return true;
    } catch (JWTVerificationException e) {
      return false;
    }
  }

  public String getTokenFromHeader(HttpServletRequest req) {
    String bearerToken = req.getHeader("Authorization");
    if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
      return bearerToken.substring(7);
    }
    return null;
  }
}
