package br.com.hyagomelo.iam.service;

import java.io.Serializable;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.hyagomelo.iam.controllers.dto.UserDTO;
import br.com.hyagomelo.iam.controllers.dto.UserDetailDTO;
import br.com.hyagomelo.iam.controllers.form.UserRegisterForm;
import br.com.hyagomelo.iam.controllers.form.UserUpdateForm;
import br.com.hyagomelo.iam.exceptions.RegisterNotFoundException;

public interface IUserService extends Serializable {

	public UserDTO register(UserRegisterForm form);

	public void update(Long id, UserUpdateForm form) throws RegisterNotFoundException;

	public void delete(Long id) throws RegisterNotFoundException;

	public UserDetailDTO detail(Long id) throws RegisterNotFoundException;

	public Page<UserDTO> list(String firstName, String lastName, String username, Pageable pageable);

}
