package com.project.Security;

import java.security.Key;
import java.util.Date;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class JwtUtils
{
    @Value("${jwtSecret}")
    private String jwtSecret;
    @Value("${jwtExpirationMs}")
    private int jwtExpirationMs;
    public String generateJwtToken(Authentication authentication)
    {
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
        return Jwts.builder()
                .setSubject((userPrincipal.getUsername()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key key()
    {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public String getUserNameFromJwtToken(String tokenWithBearer)
    {
        String token =  tokenWithBearer.replace("Bearer ", "");
        return Jwts.parserBuilder().setSigningKey(key()).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateJwtToken(String authToken)
    {
        try
        {
            Jwts.parserBuilder().setSigningKey(key()).build().parse(authToken);
            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }
}