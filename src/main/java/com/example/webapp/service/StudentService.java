package com.example.webapp.service;

import com.example.webapp.dto.StudentDTO;
import com.example.webapp.entity.Course;
import com.example.webapp.entity.Dept;
import com.example.webapp.entity.Student;
import com.example.webapp.entity.UserAccount;
import com.example.webapp.repository.CourseRepository;
import com.example.webapp.repository.DeptRepository;
import com.example.webapp.repository.StudentRepository;
import com.example.webapp.repository.UserAccountRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final DeptRepository deptRepository;
    private final CourseRepository courseRepository;
    private final UserAccountRepository userAccountRepository;

    public StudentService(StudentRepository studentRepository,
                          DeptRepository deptRepository,
                          CourseRepository courseRepository,
                          UserAccountRepository userAccountRepository) {
        this.studentRepository = studentRepository;
        this.deptRepository = deptRepository;
        this.courseRepository = courseRepository;
        this.userAccountRepository = userAccountRepository;
    }


    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Optional<Student> getStudentById(Long id) {
        return studentRepository.findById(id);
    }

    public Optional<Student> getStudentByUsername(String username) {
        return studentRepository.findByUserAccountUsername(username);
    }

    public Student createStudent(StudentDTO studentDTO) {
        Student student = new Student();
        applyDto(studentDTO, student);
        return studentRepository.save(student);
    }

    public Student updateStudent(Long id, StudentDTO studentDTO) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));
        applyDto(studentDTO, student);
        return studentRepository.save(student);
    }

    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }

    public void deleteStudentAndAccount(Student student) {
        UserAccount account = student.getUserAccount();
        studentRepository.delete(student);
        if (account != null) {
            userAccountRepository.delete(account);
        }
    }

    public void addCourseToStudent(Student student, Long courseId) {
        if (courseId == null) {
            return;
        }
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found"));
        student.getCourses().add(course);
        studentRepository.save(student);
    }

    private void applyDto(StudentDTO studentDTO, Student student) {
        student.setName(studentDTO.getName());
        student.setRoll(studentDTO.getRoll());

        Dept dept = null;
        if (studentDTO.getDeptId() != null) {
            dept = deptRepository.findById(studentDTO.getDeptId())
                    .orElseThrow(() -> new IllegalArgumentException("Department not found"));
        }
        student.setDept(dept);

        Set<Course> courses = studentDTO.getCourseIds() == null ? Set.of() :
                studentDTO.getCourseIds().stream()
                        .map(courseId -> courseRepository.findById(courseId)
                                .orElseThrow(() -> new IllegalArgumentException("Course not found")))
                        .collect(Collectors.toSet());
        student.setCourses(courses);
    }
}
