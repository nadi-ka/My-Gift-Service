package com.epam.esm.security.jwt;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.xml.bind.DatatypeConverter;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class JwtProvider {

	@Value("${jwt.secret-key}")
	private String jwtSecret;

	private static final Logger LOG = LogManager.getLogger(JwtProvider.class);

	public String generateToken(UserDetails userDetails) {
		Date issuedAt = new Date();
		long expiredAfterDays = 1;
		return Jwts.builder().setSubject(userDetails.getUsername()).setIssuedAt(issuedAt)
				.setExpiration(new Date(issuedAt.getTime() + TimeUnit.DAYS.toMillis(expiredAfterDays)))
				.signWith(SignatureAlgorithm.HS512, jwtSecret).compact();
	}

	public Claims decodeToken(String token) {
		Claims claims = null;
		try {
			claims = Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(jwtSecret)).parseClaimsJws(token)
					.getBody();
		} catch (UnsupportedJwtException e) {
			LOG.log(Level.ERROR, "Unsupported jwt", e);
		} catch (MalformedJwtException e) {
			LOG.log(Level.ERROR, "Malformed construction of jwt", e);
		} catch (SignatureException e) {
			LOG.log(Level.ERROR, "Invalid jwt signature", e);
		}catch (ExpiredJwtException expEx) {
            LOG.log(Level.ERROR, "Token expired");
        }
		return claims;
	}

}
