package com.example.webapp.service;

import com.example.webapp.entity.Course;
import com.example.webapp.entity.Student;
import com.example.webapp.repository.CourseRepository;
import com.example.webapp.repository.StudentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private StudentService studentService;

    @Test
    void addCourseToStudent() {
        Student student = new Student();
        student.setId(1L);
        student.setCourses(new HashSet<>());
        
        Course course = new Course();
        course.setId(1L);
        course.setCode("CS101");
        
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(studentRepository.save(any(Student.class))).thenReturn(student);
        
        studentService.addCourseToStudent(student, 1L);
        
        verify(courseRepository, times(1)).findById(1L);
        verify(studentRepository, times(1)).save(student);
        assertTrue(student.getCourses().contains(course));
    }
}