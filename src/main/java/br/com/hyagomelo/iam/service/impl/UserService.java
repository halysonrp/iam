package br.com.hyagomelo.iam.service.impl;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.hyagomelo.iam.controllers.dto.UserDTO;
import br.com.hyagomelo.iam.controllers.dto.UserDetailDTO;
import br.com.hyagomelo.iam.controllers.form.UserRegisterForm;
import br.com.hyagomelo.iam.controllers.form.UserUpdateForm;
import br.com.hyagomelo.iam.exceptions.RegisterNotFoundException;
import br.com.hyagomelo.iam.models.User;
import br.com.hyagomelo.iam.repositories.UserRepository;
import br.com.hyagomelo.iam.service.IUserService;

@Service
public class UserService implements IUserService, UserDetailsService {

	@Autowired
	private ModelMapper mapper;

	@Autowired
	private UserRepository repository;

	/**
	 * 
	 */
	private static final long serialVersionUID = -908573682802167518L;

	@Override
	public UserDTO register(UserRegisterForm form) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		User user = mapper.map(form, User.class);
		user.setPassword(encoder.encode(user.getPassword()));
		User savedUser = repository.save(user);
		return mapper.map(savedUser, UserDTO.class);
	}

	@Override
	public void update(Long id, UserUpdateForm form) throws RegisterNotFoundException {
		Optional<User> user = repository.findById(id);
		if (user.isPresent()) {
			User entity = user.get();
			entity.setFirstName(form.getFirstName());
			entity.setLastName(form.getLastName());
			entity.setUsername(form.getUsername());
			repository.save(entity);
		} else {
			throw new RegisterNotFoundException("Usuário não encontrado");
		}
	}

	@Override
	public void delete(Long id) throws RegisterNotFoundException {
		Optional<User> user = repository.findById(id);
		if (user.isPresent()) {
			repository.delete(user.get());
		} else {
			throw new RegisterNotFoundException("Usuário não encontrado");
		}
	}

	@Override
	public UserDetailDTO detail(Long id) throws RegisterNotFoundException {
		Optional<User> user = repository.findById(id);
		if (!user.isPresent()) {
			throw new RegisterNotFoundException("Usuário não encontrado");
		}
		return mapper.map(user.get(), UserDetailDTO.class);
	}

	@Override
	public Page<UserDTO> list(String firstName, String lastName, String username, Pageable pageable) {
		return repository.findByFirstNameAndLastNameAndUsername(firstName + "%", 
				lastName + "%", username + "%", pageable)
				.map(user -> mapper.map(user, UserDTO.class));
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> user = repository.findByUsername(username);
		if(!user.isPresent()) {
			throw new UsernameNotFoundException("Usuário não encontrado");
		}
		return user.get();
	}
}
