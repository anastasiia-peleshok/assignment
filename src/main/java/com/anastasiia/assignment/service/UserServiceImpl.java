package com.anastasiia.assignment.service;

import com.anastasiia.assignment.dto.UserDto;
import com.anastasiia.assignment.entity.User;
import com.anastasiia.assignment.exception.MinUserAgeException;
import com.anastasiia.assignment.exception.SwitchedToAndFromDatesException;
import com.anastasiia.assignment.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    @Value("${assignment.majorityAge}")
    private int majorityAge;

    @Override
    @Transactional
    public User saveUser(User user) {
        checkMajority(user);
        return userRepository.save(user);
    }

    private void checkMajority(User user) {
        LocalDate now = LocalDate.now();
        LocalDate minBirthDate = now.minusYears(majorityAge);
        if (user.getBirthDate().isAfter(minBirthDate)) {
            throw new MinUserAgeException("User must be at least " + majorityAge + " years old.");
        }

    }

    private void checkMajority(UserDto user) {
        LocalDate now = LocalDate.now();
        LocalDate minBirthDate = now.minusYears(majorityAge);
        if (user.getBirthDate().isAfter(minBirthDate)) {
            throw new MinUserAgeException("User must be at least " + majorityAge + " years old.");
        }
    }

    @Override
    @Transactional
    public User updateFullUser(long theId, User updatedUser) {
        User userToUpdate = userRepository.findById(theId).orElseThrow(() -> new NoSuchElementException("User with id: " + theId + " does not exist."));
        userToUpdate.setEmail(updatedUser.getEmail());
        userToUpdate.setFirstName(updatedUser.getFirstName());
        userToUpdate.setLastName(updatedUser.getLastName());
        checkMajority(updatedUser);
        userToUpdate.setBirthDate(updatedUser.getBirthDate());
        userToUpdate.setAddress(updatedUser.getAddress());
        userToUpdate.setPhoneNumber(updatedUser.getPhoneNumber());

        return userRepository.save(userToUpdate);
    }

    @Override
    @Transactional
    public User updatePartialUser(long theId, UserDto updatedUser) {
        User userToUpdate = userRepository.findById(theId).orElseThrow(() -> new NoSuchElementException("User with id: " + theId + " does not exist."));

        if (updatedUser.getEmail() != null) {
            userToUpdate.setEmail(updatedUser.getEmail());
        }
        if (updatedUser.getFirstName() != null) {
            userToUpdate.setFirstName(updatedUser.getFirstName());
        }
        if (updatedUser.getLastName() != null) {
            userToUpdate.setLastName(updatedUser.getLastName());
        }
        if (updatedUser.getBirthDate() != null) {
            checkMajority(updatedUser);
            userToUpdate.setBirthDate(updatedUser.getBirthDate());
        }
        if (updatedUser.getAddress() != null) {
            userToUpdate.setAddress(updatedUser.getAddress());
        }
        if (updatedUser.getPhoneNumber() != null) {
            userToUpdate.setPhoneNumber(updatedUser.getPhoneNumber());
        }

        return userRepository.save(userToUpdate);
    }

    @Override
    @Transactional
    public void deleteUser(long theId) {
        userRepository.findById(theId).orElseThrow(() -> new NoSuchElementException("User with id: " + theId + " does not exist."));
        userRepository.deleteById(theId);
    }

    @Override
    public List<User> getUsersByBirthDate(LocalDate fromDate, LocalDate toDate) {
        if (fromDate.isAfter(toDate)) {
            throw new SwitchedToAndFromDatesException("'From' date must be less than 'To' date");
        }
        return userRepository.findUsersByBirthDateBetween(fromDate, toDate);

    }
}
