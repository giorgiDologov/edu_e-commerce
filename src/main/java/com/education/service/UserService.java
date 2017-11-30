package com.education.service;

import java.util.List;
import java.util.Set;

import com.education.domain.User;
import com.education.domain.security.PasswordResetToken;
import com.education.domain.security.UserRole;

public interface UserService {

	PasswordResetToken getPasswordResetToken(final String token);
	
	void createPasswordResetTokenForUser(final User user, final String token);
	
	User findByUsername(String username);
	
	User findByEmail (String email);
	
	User createUser(User user, Set<UserRole> userRoles) throws Exception;
	
	User save(User user);

	User findById(int id);
	
	List<User> findByUsernameContaining(String keyword);
	
	List<User> findByTeacher(Boolean isTeacher);

}
