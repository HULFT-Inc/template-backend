/*
 * Copyright (c) 2025 Saison Technology International
 * https://saison-technology-intl.com/
 * All rights reserved.
 */
package com.saisontechnologyintl.template;

import com.saisontechnologyintl.template.entity.User;
import com.saisontechnologyintl.template.service.UserService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;
import io.micronaut.http.annotation.QueryValue;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller("/users")
@Tag(name = "Users", description = "User management operations")
@RequiredArgsConstructor
@Slf4j
public class UserController {

  @Inject private final UserService userService;

  @Post
  @Operation(summary = "Create user", description = "Creates a new user")
  public HttpResponse<User> createUser(@Body User user) {
    log.info("Creating new user: {}", user.getEmail());
    try {
      User createdUser = userService.createUser(user);
      return HttpResponse.created(createdUser);
    } catch (Exception e) {
      log.error("Error creating user", e);
      return HttpResponse.badRequest();
    }
  }

  @Get("/{id}")
  @Operation(summary = "Get user by ID", description = "Retrieves a user by their ID")
  public HttpResponse<User> getUser(@PathVariable Long id) {
    log.debug("Getting user by id: {}", id);
    Optional<User> user = userService.findById(id);
    return user.map(HttpResponse::ok).orElse(HttpResponse.notFound());
  }

  @Get
  @Operation(summary = "Get all users", description = "Retrieves all users or search by name")
  public List<User> getAllUsers(@QueryValue Optional<String> search) {
    if (search.isPresent()) {
      log.debug("Searching users by name: {}", search.get());
      return userService.searchByName(search.get());
    }
    log.debug("Getting all users");
    return userService.getAllUsers();
  }

  @Get("/active")
  @Operation(summary = "Get active users", description = "Retrieves all active users")
  public List<User> getActiveUsers() {
    log.debug("Getting active users");
    return userService.findAllActiveUsers();
  }

  @Put("/{id}")
  @Operation(summary = "Update user", description = "Updates an existing user")
  public HttpResponse<User> updateUser(@PathVariable Long id, @Body User user) {
    log.info("Updating user with id: {}", id);
    try {
      User updatedUser = userService.updateUser(id, user);
      return HttpResponse.ok(updatedUser);
    } catch (RuntimeException e) {
      log.error("Error updating user", e);
      return HttpResponse.notFound();
    }
  }

  @Delete("/{id}")
  @Operation(summary = "Deactivate user", description = "Deactivates a user (soft delete)")
  public HttpResponse<Map<String, Object>> deactivateUser(@PathVariable Long id) {
    log.info("Deactivating user with id: {}", id);
    try {
      userService.deactivateUser(id);
      Map<String, Object> response = new HashMap<>();
      response.put("message", "User deactivated successfully");
      response.put("id", id);
      return HttpResponse.ok(response);
    } catch (RuntimeException e) {
      log.error("Error deactivating user", e);
      return HttpResponse.notFound();
    }
  }

  @Get("/stats")
  @Operation(summary = "User statistics", description = "Get user statistics")
  public Map<String, Object> getUserStats() {
    log.debug("Getting user statistics");
    Map<String, Object> stats = new HashMap<>();
    stats.put("totalUsers", userService.getAllUsers().size());
    stats.put("activeUsers", userService.getActiveUserCount());
    stats.put("inactiveUsers", userService.getAllUsers().size() - userService.getActiveUserCount());
    return stats;
  }
}
