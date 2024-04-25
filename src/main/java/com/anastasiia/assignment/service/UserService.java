package com.anastasiia.assignment.service;

import com.anastasiia.assignment.dto.UserDto;
import com.anastasiia.assignment.entity.User;

import java.time.LocalDate;
import java.util.List;

public interface UserService {

    User saveUser(User theUser);
    User updateFullUser(long theId,User updatedUser);
    User updatePartialUser(long theId, UserDto updatedUser);
    void deleteUser(long theId);
    List<User> getUsersByBirthDate(LocalDate fromDate, LocalDate toDate);
}
