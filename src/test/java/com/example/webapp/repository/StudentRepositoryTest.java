package com.example.webapp.repository;

import com.example.webapp.entity.Role;
import com.example.webapp.entity.Student;
import com.example.webapp.entity.UserAccount;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class StudentRepositoryTest {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Test
    void findByUserAccountUsername() {
        // Create and save UserAccount
        UserAccount userAccount = new UserAccount();
        userAccount.setUsername("teststudent");
        userAccount.setPassword("password123");
        userAccount.setRole(Role.STUDENT);
        userAccountRepository.save(userAccount);

        // Create and save Student
        Student student = new Student();
        student.setName("Test Student");
        student.setRoll("12345");
        student.setUserAccount(userAccount);
        studentRepository.save(student);

        // Test the custom query method
        Optional<Student> result = studentRepository.findByUserAccountUsername("teststudent");

        assertTrue(result.isPresent());
        assertEquals("Test Student", result.get().getName());
        assertEquals("teststudent", result.get().getUserAccount().getUsername());
    }
}