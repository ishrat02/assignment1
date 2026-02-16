package com.example.webapp.controller;

import com.example.webapp.dto.StudentDTO;
import com.example.webapp.entity.Course;
import com.example.webapp.entity.Dept;
import com.example.webapp.entity.Student;
import com.example.webapp.repository.CourseRepository;
import com.example.webapp.repository.DeptRepository;
import com.example.webapp.service.StudentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;
    private final DeptRepository deptRepository;
    private final CourseRepository courseRepository;

    public StudentController(StudentService studentService, DeptRepository deptRepository, CourseRepository courseRepository) {
        this.studentService = studentService;
        this.deptRepository = deptRepository;
        this.courseRepository = courseRepository;
    }

    @GetMapping
    public String getStudents(Model model) {
        model.addAttribute("students", studentService.getAllStudents());
        return "students";
    }

    @GetMapping("/edit/{id}")
    public String editStudent(@PathVariable("id") Long id, Model model) {
        Student student = studentService.getStudentById(id)
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));
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
        model.addAttribute("editMode", true);
        return "student-form";
    }

    @PostMapping("/update/{id}")
    public String updateStudent(@PathVariable("id") Long id, @ModelAttribute("student") StudentDTO studentDTO) {
        studentService.updateStudent(id, studentDTO);
        return "redirect:/students";
    }

    @PostMapping("/delete/{id}")
    public String deleteStudent(@PathVariable("id") Long id) {
        studentService.deleteStudent(id);
        return "redirect:/students";
    }
}
