package com.electronics.store.security;

import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.context.annotation.Configuration;
import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


//this is used to perform jwt operations

//jwt generation etc,
@Configuration
public class JwtHelper {

    //1.validity
    public static final long TOKEN_VALIDITY = 5*60*1000; // 5 minutes in ms

    //2.secret key
    public static final String SECRET_KEY = "jsdbhkjbhdkskbvosbobvibeihvwuvucivicviibsacnibncisabicsbicbiasiabicbninbcivwjnvdsoioinv9dsaouosvbi";

    //retrieve username from token
    public String getUsernameFromToken(String token){
        return getClaimFromToken(token,claims -> claims.get("username",String.class));
    }

    public <T> T getClaimFromToken(String token, Function<Claims,T> claimsResolver){
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = SECRET_KEY.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parser() // parser() now returns JwtParserBuilder
                .verifyWith(getSigningKey()) // Set the signing key
                .build() // Build the JwtParser
                .parseSignedClaims(token) // Parse the token
                .getPayload(); // Retrieve the Claims
    }

    //to check if the token expired or not
    public Boolean isTokenExpired(String token){
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }



    private Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token,Claims::getExpiration);
    }


    //this function is used to generate token
    public String generateToken(UserDetails userDetails,String role){
        Map<String,Object> claims = new HashMap<>();
        claims.put("role",role);
        claims.put("username",userDetails.getUsername()); //email
        return doGenerateToken(claims);
    }



    private String doGenerateToken(Map<String, Object> claims) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + TOKEN_VALIDITY);

        return Jwts.builder()
                .claims(claims)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getSigningKey()) // Let JJWT determine the algorithm
                .compact();
    }

    public Boolean validateToken(String token,UserDetails userDetails){
        String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

}
