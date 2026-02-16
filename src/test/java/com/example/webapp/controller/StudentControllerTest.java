package com.example.webapp.controller;

import com.example.webapp.dto.StudentDTO;
import com.example.webapp.entity.Dept;
import com.example.webapp.entity.Student;
import com.example.webapp.repository.CourseRepository;
import com.example.webapp.repository.DeptRepository;
import com.example.webapp.service.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class StudentControllerTest {

    @Mock
    private StudentService studentService;

    @Mock
    private DeptRepository deptRepository;

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private StudentController studentController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(studentController).build();
    }

    @Test
    void updateStudent() throws Exception {
        Student student = new Student();
        student.setId(1L);
        student.setName("Updated Name");
        
        Dept dept = new Dept();
        dept.setId(1L);
        student.setDept(dept);
        
        when(studentService.updateStudent(eq(1L), any(StudentDTO.class))).thenReturn(student);
        
        mockMvc.perform(post("/students/update/1")
                        .param("name", "Updated Name")
                        .param("roll", "12345")
                        .param("deptId", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/students"));
    }
}