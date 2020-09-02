package br.com.hyagomelo.iam.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.hyagomelo.iam.models.User;

public interface UserRepository extends JpaRepository<User, Long> {

	@Query("SELECT u FROM user u WHERE u.firstName LIKE :firstName AND u.lastName LIKE :lastName AND u.username LIKE :username")
	Page<User> findByFirstNameAndLastNameAndUsername(String firstName, String lastName, String username,
			Pageable pageable);
	
	Optional<User> findByUsername(String username);
	
}
