package br.com.hyagomelo.iam.config;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import br.com.hyagomelo.iam.models.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class TokenService {

	@Value("${app.token.secret}")
	private String secretKey;

	@Value("${app.token.ttl}")
	private String expiresIn;
	
	public String obtainToken(Authentication authenticate) {

		User principal = (User) authenticate.getPrincipal();

		Date today = new Date();
		Date expiresDate = new Date(today.getTime() + Long.parseLong(expiresIn));

		return Jwts.builder().setIssuer("API").setSubject(principal.getId().toString()).setIssuedAt(today)
				.setExpiration(expiresDate).signWith(SignatureAlgorithm.HS256, secretKey).compact();
	}

	public boolean isValidToken(String token) {
		try {
			Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public Long obtainUserId(String token) {
		return Long.parseLong(Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject());
	}
}
