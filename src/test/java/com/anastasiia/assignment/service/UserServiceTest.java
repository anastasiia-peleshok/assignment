package com.anastasiia.assignment.service;

import com.anastasiia.assignment.dto.UserDto;
import com.anastasiia.assignment.entity.User;
import com.anastasiia.assignment.exception.MinUserAgeException;
import com.anastasiia.assignment.exception.SwitchedToAndFromDatesException;
import com.anastasiia.assignment.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;


import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;
    @Value("${assignment.majorityAge}")
    private int majorityAge;

    @Test
    public void testSaveUser_ValidUser_ReturnsSavedUser() {
        User user = new User();
        user.setEmail("Kate@Doril.com");
        user.setFirstName("Kate");
        user.setLastName("Doril");
        user.setBirthDate(LocalDate.now().minusYears(20));
        user.setAddress("123 Main St");
        user.setPhoneNumber("380976847376");

        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User savedUser = invocation.getArgument(0);
            savedUser.setId(1L);
            return savedUser;
        });
        User savedUser = userService.saveUser(user);

        assertNotNull(savedUser);
        assertEquals(user.getId(), savedUser.getId());
        assertEquals(user.getEmail(), savedUser.getEmail());
        assertEquals(user.getFirstName(), savedUser.getFirstName());
        assertEquals(user.getLastName(), savedUser.getLastName());
        assertEquals(user.getBirthDate(), savedUser.getBirthDate());
        assertEquals(user.getAddress(), savedUser.getAddress());
        assertEquals(user.getPhoneNumber(), savedUser.getPhoneNumber());
    }

    @Test
    public void testSaveUser_UserBelowMajorityAge_ThrowsMinUserAgeException() {
        User user = new User();
        user.setBirthDate(LocalDate.now().minusYears(majorityAge).plusDays(1)); // User is below majority age

        MinUserAgeException exception = assertThrows(MinUserAgeException.class, () -> userService.saveUser(user));
        assertEquals("User must be at least " + majorityAge + " years old.", exception.getMessage());
    }

    @Test
    public void testUpdateFullUser_ValidInput_ReturnsUpdatedUser() {
        long userId = 1L;
        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setEmail("old@user.com");
        existingUser.setFirstName("Old");
        existingUser.setLastName("User");
        existingUser.setBirthDate(LocalDate.of(1990, 1, 1));
        existingUser.setAddress("111 Main St");
        existingUser.setPhoneNumber("1234567890");

        User updatedUser = new User();
        updatedUser.setEmail("new@user.com");
        updatedUser.setFirstName("New");
        updatedUser.setLastName("User");
        updatedUser.setBirthDate(LocalDate.of(2000, 1, 1));
        updatedUser.setAddress("222 Main St");
        updatedUser.setPhoneNumber("9876543210");

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User result = userService.updateFullUser(userId, updatedUser);

        assertNotNull(result);
        assertEquals(userId, result.getId());
        assertEquals(updatedUser.getEmail(), result.getEmail());
        assertEquals(updatedUser.getFirstName(), result.getFirstName());
        assertEquals(updatedUser.getLastName(), result.getLastName());
        assertEquals(updatedUser.getBirthDate(), result.getBirthDate());
        assertEquals(updatedUser.getAddress(), result.getAddress());
        assertEquals(updatedUser.getPhoneNumber(), result.getPhoneNumber());
    }

    @Test
    public void testUpdateFullUser_UserNotFound_ThrowsNoSuchElementException() {
        long nonExistingUserId = 999L;
        User updatedUser = new User();
        updatedUser.setEmail("new@user.com");
        updatedUser.setFirstName("New");
        updatedUser.setLastName("User");
        updatedUser.setBirthDate(LocalDate.of(2000, 1, 1));
        updatedUser.setAddress("222 Main St");
        updatedUser.setPhoneNumber("9876543210");

        when(userRepository.findById(nonExistingUserId)).thenReturn(Optional.empty());
        NoSuchElementException exception = assertThrows(
                NoSuchElementException.class,
                () -> userService.updateFullUser(nonExistingUserId, updatedUser)
        );
        assertEquals("User with id: " + nonExistingUserId + " does not exist.", exception.getMessage());
    }

    @Test
    public void testUpdatePartialUser_ValidInput_ReturnsUpdatedUser() {
        long userId = 1L;
        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setEmail("old@user.com");
        existingUser.setFirstName("Old");
        existingUser.setLastName("User");
        existingUser.setBirthDate(LocalDate.of(1990, 1, 1));
        existingUser.setAddress("111 Main St");
        existingUser.setPhoneNumber("1234567890");

        UserDto updatedUserDto = new UserDto();
        updatedUserDto.setEmail("new@user.com");
        updatedUserDto.setFirstName("New");
        updatedUserDto.setLastName("User");
        updatedUserDto.setBirthDate(LocalDate.of(2000, 1, 1));
        updatedUserDto.setAddress("222 Main St");
        updatedUserDto.setPhoneNumber("9876543210");

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User result = userService.updatePartialUser(userId, updatedUserDto);

        assertNotNull(result);
        assertEquals(userId, result.getId());
        assertEquals(updatedUserDto.getEmail(), result.getEmail());
        assertEquals(updatedUserDto.getFirstName(), result.getFirstName());
        assertEquals(updatedUserDto.getLastName(), result.getLastName());
        assertEquals(updatedUserDto.getBirthDate(), result.getBirthDate());
        assertEquals(updatedUserDto.getAddress(), result.getAddress());
        assertEquals(updatedUserDto.getPhoneNumber(), result.getPhoneNumber());
    }

    @Test
    public void testUpdatePartialUser_UserNotFound_ThrowsNoSuchElementException() {
        long nonExistingUserId = 999L;
        UserDto updatedUserDto = new UserDto();
        updatedUserDto.setEmail("new@user.com");
        updatedUserDto.setFirstName("New");
        updatedUserDto.setLastName("User");
        updatedUserDto.setBirthDate(LocalDate.of(2000, 1, 1));
        updatedUserDto.setAddress("222 Main St");
        updatedUserDto.setPhoneNumber("9876543210");

        when(userRepository.findById(nonExistingUserId)).thenReturn(Optional.empty());
        NoSuchElementException exception = assertThrows(
                NoSuchElementException.class,
                () -> userService.updatePartialUser(nonExistingUserId, updatedUserDto)
        );
        assertEquals("User with id: " + nonExistingUserId + " does not exist.", exception.getMessage());
    }

    @Test
    public void testDeleteUser_ValidInput_DeletesUser() {
        long userId = 1L;
        User existingUser = new User();
        existingUser.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));

        userService.deleteUser(userId);

        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    public void testDeleteUser_UserNotFound_ThrowsNoSuchElementException() {
        long nonExistingUserId = 99L;

        when(userRepository.findById(nonExistingUserId)).thenReturn(Optional.empty());
        NoSuchElementException exception = assertThrows(
                NoSuchElementException.class,
                () -> userService.deleteUser(nonExistingUserId)
        );
        assertEquals("User with id: " + nonExistingUserId + " does not exist.", exception.getMessage());
    }
    @Test
    public void testGetUsersByBirthDate_ValidDates_ReturnsListOfUsers() {
        LocalDate fromDate = LocalDate.of(1990, 1, 1);
        LocalDate toDate = LocalDate.of(2000, 1, 1);
        List<User> users = new ArrayList<>();
        users.add(new User());
        users.add(new User());

        when(userRepository.findUsersByBirthDateBetween(fromDate, toDate)).thenReturn(users);

        List<User> result = userService.getUsersByBirthDate(fromDate, toDate);

        assertNotNull(result);
        assertEquals(users.size(), result.size());
    }

    @Test
    public void testGetUsersByBirthDate_FromDateAfterToDate_ThrowsSwitchedToAndFromDatesException() {
        LocalDate fromDate = LocalDate.of(2000, 1, 1);
        LocalDate toDate = LocalDate.of(1990, 1, 1);
        SwitchedToAndFromDatesException exception = assertThrows(
                SwitchedToAndFromDatesException.class,
                () -> userService.getUsersByBirthDate(fromDate, toDate)
        );
        assertEquals("'From' date must be less than 'To' date", exception.getMessage());
 }
}
