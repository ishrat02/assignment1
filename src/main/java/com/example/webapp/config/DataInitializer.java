package com.example.webapp.config;

import com.example.webapp.entity.*;
import com.example.webapp.repository.CourseRepository;
import com.example.webapp.repository.DeptRepository;
import com.example.webapp.repository.StudentRepository;
import com.example.webapp.repository.TeacherRepository;
import com.example.webapp.repository.UserAccountRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner seedData(DeptRepository deptRepository,
                                      TeacherRepository teacherRepository,
                                      CourseRepository courseRepository,
                                      StudentRepository studentRepository,
                                      UserAccountRepository userAccountRepository,
                                      PasswordEncoder passwordEncoder) {
        return args -> {
            if (userAccountRepository.count() > 0) {
                return;
            }

            Dept cs = new Dept();
            cs.setName("Computer Science");
            deptRepository.save(cs);

            Dept ee = new Dept();
            ee.setName("Electrical Engineering");
            deptRepository.save(ee);

            Teacher teacher = new Teacher();
            teacher.setName("Dr. Taylor");
            teacher.setEmail("taylor@school.edu");
            teacher.setDept(cs);
            teacherRepository.save(teacher);

            Course course = new Course();
            course.setCode("CS101");
            course.setTitle("Intro to Programming");
            course.setDept(cs);
            course.setTeacher(teacher);
            courseRepository.save(course);

            UserAccount teacherAccount = new UserAccount();
            teacherAccount.setUsername("teacher1");
            teacherAccount.setPassword(passwordEncoder.encode("teacher123"));
            teacherAccount.setRole(Role.TEACHER);
            userAccountRepository.save(teacherAccount);

            UserAccount studentAccount = new UserAccount();
            studentAccount.setUsername("student1");
            studentAccount.setPassword(passwordEncoder.encode("student123"));
            studentAccount.setRole(Role.STUDENT);
            userAccountRepository.save(studentAccount);

            Student student = new Student();
            student.setName("Alex Carter");
            student.setRoll("S1001");
            student.setDept(cs);
            student.setCourses(Set.of(course));
            student.setUserAccount(studentAccount);
            studentRepository.save(student);
        };
    }
}
