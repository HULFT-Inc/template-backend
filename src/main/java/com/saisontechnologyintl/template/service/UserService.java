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
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class UserService {

  private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

  @Inject private UserRepository userRepository;

  @Transactional
  public User createUser(User user) {
    LOG.info("Creating user with email: {}", user.getEmail());
    user.setCreatedAt(LocalDateTime.now());
    user.setUpdatedAt(LocalDateTime.now());
    return userRepository.save(user);
  }

  public Optional<User> findById(Long id) {
    LOG.debug("Finding user by id: {}", id);
    return userRepository.findById(id);
  }

  public Optional<User> findByEmail(String email) {
    LOG.debug("Finding user by email: {}", email);
    return userRepository.findByEmail(email);
  }

  public List<User> findAllActiveUsers() {
    LOG.debug("Finding all active users");
    return userRepository.findByActive(true);
  }

  public List<User> searchByName(String name) {
    LOG.debug("Searching users by name: {}", name);
    List<User> byFirstName = userRepository.findByFirstNameContainsIgnoreCase(name);
    List<User> byLastName = userRepository.findByLastNameContainsIgnoreCase(name);
    byFirstName.addAll(byLastName);
    return byFirstName.stream().distinct().toList();
  }

  @Transactional
  public User updateUser(Long id, User updatedUser) {
    LOG.info("Updating user with id: {}", id);
    return userRepository
        .findById(id)
        .map(
            user -> {
              user.setFirstName(updatedUser.getFirstName());
              user.setLastName(updatedUser.getLastName());
              user.setEmail(updatedUser.getEmail());
              user.setActive(updatedUser.getActive());
              user.setUpdatedAt(LocalDateTime.now());
              return userRepository.save(user);
            })
        .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
  }

  @Transactional
  public void deactivateUser(Long id) {
    LOG.info("Deactivating user with id: {}", id);
    userRepository
        .findById(id)
        .ifPresentOrElse(
            user -> {
              user.setActive(false);
              user.setUpdatedAt(LocalDateTime.now());
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
    LOG.debug("Finding all users");
    return userRepository.findAll();
  }
}
