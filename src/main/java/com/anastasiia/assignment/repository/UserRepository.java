package com.anastasiia.assignment.repository;

import com.anastasiia.assignment.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    List<User> findUsersByBirthDateBetween(LocalDate fromDate, LocalDate toDate);

}
