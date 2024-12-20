package store.furniture.configuration;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

  @Value("${woodcraft-furniture-store.security.jwt.secret-key}")
  private String secretKey;

  @Value("${woodcraft-furniture-store.security.jwt.expiration}")
  private long expirationSeconds;

  @Value("${woodcraft-furniture-store.security.jwt.refresh-expiration}")
  private long refreshExpirationSeconds;

  private SecretKey getSecretKey() {
    byte[] keyBytes = Decoders.BASE64.decode(secretKey);
    return Keys.hmacShaKeyFor(keyBytes);
  }

  public String extractUsername(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  private Date extractExpiration(String token) {
    return extractClaim(token, Claims::getExpiration);
  }

  public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = extractAllClaims(token);
    return claimsResolver.apply(claims);
  }

  private Claims extractAllClaims(String token) {
    return Jwts
      .parser()
      .verifyWith(getSecretKey())
      .build()
      .parseSignedClaims(token)
      .getPayload();
  }

  public String generateToken(UserDetails userDetails) {
    return generateToken(new HashMap<>(), userDetails);
  }

  public String generateToken(
    Map<String, Object> extraClaims,
    UserDetails userDetails) {
    return buildToken(extraClaims, userDetails, expirationSeconds);
  }

  public String generateRefreshToken(
    UserDetails userDetails) {
    return buildToken(new HashMap<>(), userDetails, refreshExpirationSeconds);
  }

  private String buildToken(
    Map<String, Object> extraClaims,
    UserDetails userDetails,
    long expiration) {
    return Jwts
      .builder()
      .claims()
      .empty()
      .add(extraClaims)
      .subject(userDetails.getUsername())
      .issuedAt(new Date(System.currentTimeMillis()))
      .expiration(new Date(System.currentTimeMillis() + expiration))
      .and()
      .signWith(getSecretKey(), Jwts.SIG.HS256)
      .compact();
  }

  public boolean isTokenValid(String token, UserDetails userDetails) {
    final String username = extractUsername(token);
    return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
  }

  private boolean isTokenExpired(String token) {
    return extractExpiration(token).before(new Date());
  }

}
