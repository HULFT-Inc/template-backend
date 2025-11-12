/*
 * Copyright (c) 2025 Saison Technology International
 * https://saison-technology-intl.com/
 * All rights reserved.
 */
package com.saisontechnologyintl.template;

import static org.assertj.core.api.Assertions.assertThat;

import com.saisontechnologyintl.template.entity.User;
import com.saisontechnologyintl.template.repository.UserRepository;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.test.support.TestPropertyProvider;
import jakarta.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@MicronautTest
@Testcontainers
class UserRepositoryTest implements TestPropertyProvider {

  @Container
  static PostgreSQLContainer<?> postgres =
      new PostgreSQLContainer<>("postgres:15-alpine")
          .withDatabaseName("testdb")
          .withUsername("test")
          .withPassword("test");

  @Inject UserRepository userRepository;

  @Override
  public Map<String, String> getProperties() {
    return Map.of(
        "datasources.default.url", postgres.getJdbcUrl(),
        "datasources.default.username", postgres.getUsername(),
        "datasources.default.password", postgres.getPassword(),
        "jpa.default.properties.hibernate.hbm2ddl.auto", "create-drop");
  }

  @Test
  @DisplayName("Should save and find user by email")
  void shouldSaveAndFindUserByEmail() {
    // Given
    User user = User.builder().firstName("Test").lastName("User").email("test@example.com").build();

    // When
    User savedUser = userRepository.save(user);
    Optional<User> foundUser = userRepository.findByEmail("test@example.com");

    // Then
    assertThat(savedUser.getId()).isNotNull();
    assertThat(foundUser).isPresent();
    assertThat(foundUser.get().getFullName()).isEqualTo("Test User");
  }

  @Test
  @DisplayName("Should find active users")
  void shouldFindActiveUsers() {
    // Given
    User activeUser =
        User.builder()
            .firstName("Active")
            .lastName("User")
            .email("active@example.com")
            .active(true)
            .build();
    activeUser.setActive(true);
    User inactiveUser =
        User.builder()
            .firstName("Inactive")
            .lastName("User")
            .email("inactive@example.com")
            .active(false)
            .build();
    inactiveUser.setActive(false);

    userRepository.save(activeUser);
    userRepository.save(inactiveUser);

    // When
    List<User> activeUsers = userRepository.findByActive(true);

    // Then
    assertThat(activeUsers).hasSize(1);
    assertThat(activeUsers.get(0).getEmail()).isEqualTo("active@example.com");
  }

  @Test
  @DisplayName("Should search users by name")
  void shouldSearchUsersByName() {
    // Given
    User user1 = User.builder().firstName("John").lastName("Doe").email("john@example.com").build();
    User user2 = User.builder().firstName("Jane").lastName("Doe").email("jane@example.com").build();
    User user3 = User.builder().firstName("Bob").lastName("Smith").email("bob@example.com").build();

    userRepository.save(user1);
    userRepository.save(user2);
    userRepository.save(user3);

    // When
    List<User> doeUsers = userRepository.findByLastNameContainsIgnoreCase("doe");
    List<User> johnUsers = userRepository.findByFirstNameContainsIgnoreCase("john");

    // Then
    assertThat(doeUsers).hasSize(2);
    assertThat(johnUsers).hasSize(1);
    assertThat(johnUsers.get(0).getFirstName()).isEqualTo("John");
  }
}
