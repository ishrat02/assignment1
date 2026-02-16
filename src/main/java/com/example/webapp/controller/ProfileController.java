package com.example.webapp.controller;

import com.example.webapp.dto.StudentDTO;
import com.example.webapp.entity.Course;
import com.example.webapp.entity.Student;
import com.example.webapp.repository.CourseRepository;
import com.example.webapp.repository.DeptRepository;
import com.example.webapp.service.StudentService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/profile")
public class ProfileController {
    private final StudentService studentService;
    private final DeptRepository deptRepository;
    private final CourseRepository courseRepository;

    public ProfileController(StudentService studentService, DeptRepository deptRepository, CourseRepository courseRepository) {
        this.studentService = studentService;
        this.deptRepository = deptRepository;
        this.courseRepository = courseRepository;
    }

    @GetMapping
    public String getProfile(Authentication authentication, Model model) {
        String username = authentication.getName();
        Student student = studentService.getStudentByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Profile not found"));
        model.addAttribute("student", student);
        model.addAttribute("allCourses", courseRepository.findAll());
        return "profile";
    }

    @GetMapping("/edit")
    public String editProfile(Authentication authentication, Model model) {
        String username = authentication.getName();
        Student student = studentService.getStudentByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Profile not found"));
        StudentDTO studentDTO = new StudentDTO();
        studentDTO.setId(student.getId());
        studentDTO.setName(student.getName());
        studentDTO.setRoll(student.getRoll());
        if (student.getDept() != null) {
            studentDTO.setDeptId(student.getDept().getId());
        }
        studentDTO.setCourseIds(student.getCourses().stream().map(Course::getId).toList());
        model.addAttribute("student", studentDTO);
        model.addAttribute("departments", deptRepository.findAll());
        model.addAttribute("courses", courseRepository.findAll());
        return "profile-edit";
    }

    @PostMapping("/update")
    public String updateProfile(Authentication authentication, @ModelAttribute("student") StudentDTO studentDTO) {
        String username = authentication.getName();
        Student student = studentService.getStudentByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Profile not found"));
        studentService.updateStudent(student.getId(), studentDTO);
        return "redirect:/profile";
    }

    @PostMapping("/delete")
    public String deleteProfile(Authentication authentication) {
        String username = authentication.getName();
        Student student = studentService.getStudentByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Profile not found"));
        studentService.deleteStudentAndAccount(student);
        return "redirect:/login?deleted";
    }

    @PostMapping("/courses/add")
    public String addCourse(Authentication authentication, @ModelAttribute("courseId") Long courseId) {
        String username = authentication.getName();
        Student student = studentService.getStudentByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Profile not found"));
        studentService.addCourseToStudent(student, courseId);
        return "redirect:/profile";
    }
}
