package com.example.webapp.service;

import com.example.webapp.dto.SignupDTO;
import com.example.webapp.entity.Role;
import com.example.webapp.entity.UserAccount;
import com.example.webapp.repository.DeptRepository;
import com.example.webapp.repository.StudentRepository;
import com.example.webapp.repository.TeacherRepository;
import com.example.webapp.repository.UserAccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SignupServiceTest {

    @Mock
    private UserAccountRepository userAccountRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private TeacherRepository teacherRepository;

    @Mock
    private DeptRepository deptRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private SignupService signupService;

    @Test
    void signup() {
        SignupDTO signupDTO = new SignupDTO();
        signupDTO.setUsername("testuser");
        signupDTO.setPassword("password123");
        signupDTO.setRole("STUDENT");
        
        when(userAccountRepository.findByUsername("testuser")).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userAccountRepository.save(any(UserAccount.class))).thenAnswer(i -> i.getArguments()[0]);
        when(studentRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);
        
        assertDoesNotThrow(() -> signupService.signup(signupDTO));
        
        verify(userAccountRepository, times(1)).findByUsername("testuser");
        verify(passwordEncoder, times(1)).encode("password123");
        verify(userAccountRepository, times(1)).save(any(UserAccount.class));
    }
}