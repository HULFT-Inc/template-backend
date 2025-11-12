/*
 * Copyright (c) 2025 Saison Technology International
 * https://saison-technology-intl.com/
 * All rights reserved.
 */
package com.saisontechnologyintl.template.repository;

import com.saisontechnologyintl.template.entity.User;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByEmail(String email);

  List<User> findByActive(Boolean active);

  List<User> findByFirstNameContainsIgnoreCase(String firstName);

  List<User> findByLastNameContainsIgnoreCase(String lastName);

  long countByActive(Boolean active);
}
