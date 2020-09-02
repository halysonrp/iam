package br.com.hyagomelo.iam.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.hyagomelo.iam.config.TokenService;
import br.com.hyagomelo.iam.controllers.dto.TokenDTO;
import br.com.hyagomelo.iam.controllers.form.FormLogin;

@RestController
@RequestMapping("auth")
public class AuthController {

	@Autowired
	private AuthenticationManager authManager;

	@Autowired
	private TokenService tokenService;

	@PostMapping
	public ResponseEntity<TokenDTO> authenticate(@RequestBody FormLogin formLogin) {
		UsernamePasswordAuthenticationToken dados = new UsernamePasswordAuthenticationToken(formLogin.getUsername(),
				formLogin.getPassword());
		try {
			Authentication authenticate = authManager.authenticate(dados);
			String token = tokenService.obtainToken(authenticate);
			return ResponseEntity.ok(new TokenDTO("Bearer", token));
		} catch (AuthenticationException e) {
			return ResponseEntity.badRequest().build();
		}

	}
}
