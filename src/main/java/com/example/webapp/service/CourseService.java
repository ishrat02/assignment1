package com.example.webapp.service;

import com.example.webapp.dto.CourseDTO;
import com.example.webapp.entity.Course;
import com.example.webapp.entity.Dept;
import com.example.webapp.entity.Student;
import com.example.webapp.entity.Teacher;
import com.example.webapp.repository.CourseRepository;
import com.example.webapp.repository.DeptRepository;
import com.example.webapp.repository.StudentRepository;
import com.example.webapp.repository.TeacherRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {
    private final CourseRepository courseRepository;
    private final DeptRepository deptRepository;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;

    public CourseService(CourseRepository courseRepository,
                         DeptRepository deptRepository,
                         TeacherRepository teacherRepository,
                         StudentRepository studentRepository) {
        this.courseRepository = courseRepository;
        this.deptRepository = deptRepository;
        this.teacherRepository = teacherRepository;
        this.studentRepository = studentRepository;
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Course getCourse(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Course not found"));
    }

    public Course createCourse(CourseDTO courseDTO) {
        Course course = new Course();
        applyDto(courseDTO, course);
        return courseRepository.save(course);
    }

    public Course updateCourse(Long id, CourseDTO courseDTO) {
        Course course = getCourse(id);
        applyDto(courseDTO, course);
        return courseRepository.save(course);
    }

    public void deleteCourse(Long id) {
        Course course = getCourse(id);
        for (Student student : course.getStudents()) {
            student.getCourses().remove(course);
        }
        studentRepository.saveAll(course.getStudents());
        courseRepository.delete(course);
    }

    private void applyDto(CourseDTO courseDTO, Course course) {
        course.setCode(courseDTO.getCode());
        course.setTitle(courseDTO.getTitle());

        Dept dept = null;
        if (courseDTO.getDeptId() != null) {
            dept = deptRepository.findById(courseDTO.getDeptId())
                    .orElseThrow(() -> new IllegalArgumentException("Department not found"));
        }
        course.setDept(dept);

        Teacher teacher = null;
        if (courseDTO.getTeacherId() != null) {
            teacher = teacherRepository.findById(courseDTO.getTeacherId())
                    .orElseThrow(() -> new IllegalArgumentException("Teacher not found"));
        }
        course.setTeacher(teacher);
    }
}
