package com.anastasiia.assignment.controller;

import com.anastasiia.assignment.dto.UserDto;
import com.anastasiia.assignment.entity.User;
import com.anastasiia.assignment.service.UserServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Validated
public class UserController {
    private final UserServiceImpl userService;

    @PostMapping
    public ResponseEntity<User> saveUser(@Valid @RequestBody User user) {
        User savedUser = userService.saveUser(user);
        return ResponseEntity.ok(savedUser);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateFullUser(@PathVariable("id") long id, @Valid @RequestBody User updatedUser) {
        User user = userService.updateFullUser(id, updatedUser);
        return ResponseEntity.ok(user);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<User> updatePartialUser(@PathVariable("id") long id, @Valid @RequestBody UserDto updatedUser) {
        User user = userService.updatePartialUser(id, updatedUser);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/birthDate")
    public ResponseEntity<List<User>> getUsersByBirthDate(@RequestParam("fromDate") LocalDate fromDate,
                                                          @RequestParam("toDate") LocalDate toDate) {
        List<User> users = userService.getUsersByBirthDate(fromDate, toDate);
        return ResponseEntity.ok(users);
    }
}