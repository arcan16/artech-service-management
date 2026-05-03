package com.Ar_Tech.infra.security.utils;

import com.Ar_Tech.models.UserEntity;
import com.Ar_Tech.repositories.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtUtils {

    @Value("${jwt.expiration.time}")
    private String expirationTime;

    @Value("${jwt.secret.key}")
    private String secretKey;

    @Autowired
    private UserRepository userRepository;

    public String generateAccessToken(String username){
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + Long.parseLong(expirationTime)))
                .signWith(getSignature())
                .compact();
    }

    public SecretKey getSignature(){
        byte [] key = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(key);
    }

    public boolean isValidToken(String token){
        try {
            Jwts.parser().verifyWith(getSignature()).build().parseSignedClaims(token);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public String getUsernameFromToken(String token){
        return getClaim(token, Claims::getSubject);
    }

    public <T> T getClaim(String token, Function<Claims, T> claimsTFunction){
        Claims claims = getAllClaims(token);
        return claimsTFunction.apply(claims);
    }

    public Claims getAllClaims(String token){
        Jws<Claims> claimsJws = Jwts.parser().verifyWith(getSignature()).build().parseSignedClaims(token);
        return claimsJws.getPayload();
    }

    public UserEntity getUserFromToken(String token){
        String username = getUsernameFromToken(token);

        return userRepository.findByUsername(username)
                .orElseThrow(()-> new UsernameNotFoundException("El usuario indicado en el token no existe"));

    }
    public UserEntity getUserFromRequest(HttpServletRequest request) {
        String tokenHeader = request.getHeader("Authorization");
        String token = tokenHeader.substring(7);
        return getUserFromToken(token);
    }
}
