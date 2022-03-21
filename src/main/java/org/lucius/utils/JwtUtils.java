package org.lucius.utils;

import io.jsonwebtoken.*;

import java.util.Date;
import java.util.UUID;

/**
 * @Author lucius
 * @Date 2021-06-17
 */
public class JwtUtils {

    private static long time = 1000*60*60*24;
    private static String signature = "bycx-zoll";

    public static String createJwt(String userName,String password){
        JwtBuilder jwtBuilder = Jwts.builder();
        String jwtToken = jwtBuilder.setHeaderParam("typ","JWT")
                .setHeaderParam("alg","HS256")
                .claim("userName",userName)
                .claim("password",password)
                .setSubject("Admin")
                .setExpiration(new Date(System.currentTimeMillis()+time))
                .setId(UUID.randomUUID().toString())
                .signWith(SignatureAlgorithm.HS256,signature).compact();
        return jwtToken;

    }

    public static String parseJwt(String token){
        JwtParser parser = Jwts.parser();
        Jws<Claims> claimsJws = parser.setSigningKey(signature).parseClaimsJws(token);
        Claims claims  =claimsJws.getBody();
        return claims.get("userName").toString();
    }


}
