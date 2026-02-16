package com.example.webapp.service;

import com.example.webapp.entity.Course;
import com.example.webapp.repository.CourseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private CourseService courseService;

    @Test
    void getAllCourses() {
        Course course1 = new Course();
        course1.setId(1L);
        course1.setCode("CS101");
        
        Course course2 = new Course();
        course2.setId(2L);
        course2.setCode("CS102");
        
        when(courseRepository.findAll()).thenReturn(Arrays.asList(course1, course2));
        
        List<Course> result = courseService.getAllCourses();
        
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("CS101", result.get(0).getCode());
    }
}