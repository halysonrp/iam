package br.com.hyagomelo.iam.controllers;

import java.io.Serializable;
import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.hyagomelo.iam.controllers.dto.UserDTO;
import br.com.hyagomelo.iam.controllers.dto.UserDetailDTO;
import br.com.hyagomelo.iam.controllers.form.UserRegisterForm;
import br.com.hyagomelo.iam.controllers.form.UserUpdateForm;
import br.com.hyagomelo.iam.exceptions.RegisterNotFoundException;
import br.com.hyagomelo.iam.service.IUserService;

@RestController
@RequestMapping("/user")
public class UserController implements Serializable {

	@Autowired
	private IUserService userService;

	/**
	 * 
	 */
	private static final long serialVersionUID = 6310051705354566218L;

	@GetMapping
	@Cacheable(value = "user_list")
	public ResponseEntity<Page<UserDTO>> list(@RequestParam(required = false, defaultValue = "%") String firstName,
			@RequestParam(required = false, defaultValue = "%") String lastName,
			@RequestParam(required = false, defaultValue = "%") String username,
			@PageableDefault(sort = "firstName", direction = Direction.ASC, size = 10, page = 0) Pageable pageable) {
		return ResponseEntity.ok(userService.list(firstName, lastName, username, pageable));
	}

	@GetMapping("/{id}")
	@Cacheable(value = "user_detail")
	public ResponseEntity<UserDetailDTO> findById(@PathVariable Long id) {
		try {
			return ResponseEntity.ok(userService.detail(id));
		} catch (RegisterNotFoundException e) {
			return ResponseEntity.notFound().build();
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}

	@PostMapping
	@CacheEvict(value = { "user_list", "user_detail" }, allEntries = true)
	public ResponseEntity<UserDTO> register(@RequestBody UserRegisterForm form, UriComponentsBuilder uriBuilder) {
		try {
			UserDTO user = userService.register(form);
			URI uri = uriBuilder.path("/user/{id}").buildAndExpand(user.getId()).toUri();
			return ResponseEntity.created(uri).body(user);
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}

	@PutMapping("/{id}")
	@CacheEvict(value = { "user_list", "user_detail" }, allEntries = true)
	public ResponseEntity<?> update(@PathVariable Long id, @RequestBody UserUpdateForm form) {
		try {
			userService.update(id, form);
			return ResponseEntity.ok().build();
		} catch (RegisterNotFoundException e) {
			return ResponseEntity.notFound().build();
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}

	@DeleteMapping("/{id}")
	@CacheEvict(value = { "user_list", "user_detail" }, allEntries = true)
	public ResponseEntity<?> delete(@PathVariable Long id) {
		try {
			userService.delete(id);
			return ResponseEntity.ok().build();
		} catch (RegisterNotFoundException e) {
			return ResponseEntity.notFound().build();
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}
}
