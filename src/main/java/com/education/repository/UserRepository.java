package com.education.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;


import com.education.domain.User;

public interface UserRepository extends CrudRepository<User, Long> {
	User findByUsername(String username);
	
	User findByEmail(String email);

	User findById(int id);
	
	List<User> findByUsernameContaining(String keyword);

	List<User> findByTeacher(boolean isTeacher);
}
