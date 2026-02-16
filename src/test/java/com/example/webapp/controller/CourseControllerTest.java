package com.example.webapp.controller;

import com.example.webapp.entity.Course;
import com.example.webapp.repository.DeptRepository;
import com.example.webapp.repository.TeacherRepository;
import com.example.webapp.service.CourseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class CourseControllerTest {

    @Mock
    private CourseService courseService;

    @Mock
    private DeptRepository deptRepository;

    @Mock
    private TeacherRepository teacherRepository;

    @InjectMocks
    private CourseController courseController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(courseController)
                .setViewResolvers((viewName, locale) -> new org.springframework.web.servlet.view.InternalResourceView("/WEB-INF/templates/" + viewName + ".html"))
                .build();
    }

    @Test
    void addCourse() throws Exception {
        when(deptRepository.findAll()).thenReturn(Collections.emptyList());
        when(teacherRepository.findAll()).thenReturn(Collections.emptyList());
        
        mockMvc.perform(get("/courses/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("course-form"));
    }
}