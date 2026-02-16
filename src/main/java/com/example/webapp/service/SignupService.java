package com.example.webapp.service;

import com.example.webapp.dto.SignupDTO;
import com.example.webapp.entity.*;
import com.example.webapp.repository.DeptRepository;
import com.example.webapp.repository.StudentRepository;
import com.example.webapp.repository.TeacherRepository;
import com.example.webapp.repository.UserAccountRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class SignupService {
    private final UserAccountRepository userAccountRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final DeptRepository deptRepository;
    private final PasswordEncoder passwordEncoder;

    public SignupService(UserAccountRepository userAccountRepository,
                         StudentRepository studentRepository,
                         TeacherRepository teacherRepository,
                         DeptRepository deptRepository,
                         PasswordEncoder passwordEncoder) {
        this.userAccountRepository = userAccountRepository;
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;
        this.deptRepository = deptRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void signup(SignupDTO signupDTO) {
        if (userAccountRepository.findByUsername(signupDTO.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }

        Role role = Role.valueOf(signupDTO.getRole());
        Dept dept = null;
        if (signupDTO.getDeptId() != null) {
            dept = deptRepository.findById(signupDTO.getDeptId())
                    .orElseThrow(() -> new IllegalArgumentException("Department not found"));
        }

        UserAccount account = new UserAccount();
        account.setUsername(signupDTO.getUsername());
        account.setPassword(passwordEncoder.encode(signupDTO.getPassword()));
        account.setRole(role);
        userAccountRepository.save(account);

        if (role == Role.STUDENT) {
            Student student = new Student();
            student.setName(signupDTO.getName());
            student.setRoll(signupDTO.getRoll());
            student.setDept(dept);
            student.setUserAccount(account);
            studentRepository.save(student);
        } else {
            Teacher teacher = new Teacher();
            teacher.setName(signupDTO.getName());
            teacher.setEmail(signupDTO.getEmail());
            teacher.setDept(dept);
            teacherRepository.save(teacher);
        }
    }
}
