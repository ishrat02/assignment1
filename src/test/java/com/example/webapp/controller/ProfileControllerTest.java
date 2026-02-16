package com.example.webapp.controller;

import com.example.webapp.entity.Student;
import com.example.webapp.entity.UserAccount;
import com.example.webapp.repository.CourseRepository;
import com.example.webapp.repository.DeptRepository;
import com.example.webapp.service.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ProfileControllerTest {

    @Mock
    private StudentService studentService;

    @Mock
    private DeptRepository deptRepository;

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private ProfileController profileController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(profileController)
                .setViewResolvers((viewName, locale) -> new org.springframework.web.servlet.view.InternalResourceView("/WEB-INF/templates/" + viewName + ".html"))
                .build();
    }

    @Test
    void getProfile() throws Exception {
        Student student = new Student();
        student.setId(1L);
        student.setName("Test Student");
        student.setCourses(new HashSet<>());
        
        UserAccount userAccount = new UserAccount();
        userAccount.setUsername("testuser");
        student.setUserAccount(userAccount);
        
        Authentication auth = new UsernamePasswordAuthenticationToken("testuser", null);
        
        when(studentService.getStudentByUsername("testuser")).thenReturn(Optional.of(student));
        when(courseRepository.findAll()).thenReturn(Collections.emptyList());
        
        mockMvc.perform(get("/profile").principal(auth))
                .andExpect(status().isOk())
                .andExpect(view().name("profile"));
    }
}