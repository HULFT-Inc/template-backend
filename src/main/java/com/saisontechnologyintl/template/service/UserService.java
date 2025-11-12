/*
 * Copyright (c) 2025 Saison Technology International
 * https://saison-technology-intl.com/
 * All rights reserved.
 */
package com.saisontechnologyintl.template.service;

import com.saisontechnologyintl.template.entity.User;
import com.saisontechnologyintl.template.repository.UserRepository;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Singleton
@RequiredArgsConstructor
@Slf4j
public class UserService {

  @Inject private final UserRepository userRepository;

  @Transactional
  public User createUser(User user) {
    log.info("Creating user with email: {}", user.getEmail());
    return userRepository.save(user);
  }

  public Optional<User> findById(Long id) {
    log.debug("Finding user by id: {}", id);
    return userRepository.findById(id);
  }

  public Optional<User> findByEmail(String email) {
    log.debug("Finding user by email: {}", email);
    return userRepository.findByEmail(email);
  }

  public List<User> findAllActiveUsers() {
    log.debug("Finding all active users");
    return userRepository.findByActive(true);
  }

  public List<User> searchByName(String name) {
    log.debug("Searching users by name: {}", name);
    List<User> byFirstName = userRepository.findByFirstNameContainsIgnoreCase(name);
    List<User> byLastName = userRepository.findByLastNameContainsIgnoreCase(name);
    byFirstName.addAll(byLastName);
    return byFirstName.stream().distinct().toList();
  }

  @Transactional
  public User updateUser(Long id, User updatedUser) {
    log.info("Updating user with id: {}", id);
    return userRepository
        .findById(id)
        .map(
            user -> {
              user.setFirstName(updatedUser.getFirstName());
              user.setLastName(updatedUser.getLastName());
              user.setEmail(updatedUser.getEmail());
              user.setActive(updatedUser.getActive());
              return userRepository.save(user);
            })
        .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
  }

  @Transactional
  public void deactivateUser(Long id) {
    log.info("Deactivating user with id: {}", id);
    userRepository
        .findById(id)
        .ifPresentOrElse(
            user -> {
              user.setActive(false);
              userRepository.save(user);
            },
            () -> {
              throw new RuntimeException("User not found with id: " + id);
            });
  }

  public long getActiveUserCount() {
    return userRepository.countByActive(true);
  }

  public List<User> getAllUsers() {
    log.debug("Finding all users");
    return userRepository.findAll();
  }
}
