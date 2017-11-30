package com.education.repository;

import org.springframework.data.repository.CrudRepository;

import com.education.domain.security.Role;

public interface RoleRepository extends CrudRepository<Role, Long> {
	Role findByname(String name);
}
