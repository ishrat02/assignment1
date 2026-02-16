package com.example.webapp.controller;

import com.example.webapp.dto.CourseDTO;
import com.example.webapp.entity.Course;
import com.example.webapp.repository.DeptRepository;
import com.example.webapp.repository.TeacherRepository;
import com.example.webapp.service.CourseService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/courses")
public class CourseController {
    private final CourseService courseService;
    private final DeptRepository deptRepository;
    private final TeacherRepository teacherRepository;

    public CourseController(CourseService courseService, DeptRepository deptRepository, TeacherRepository teacherRepository) {
        this.courseService = courseService;
        this.deptRepository = deptRepository;
        this.teacherRepository = teacherRepository;
    }

    @GetMapping
    public String listCourses(Model model) {
        model.addAttribute("courses", courseService.getAllCourses());
        return "courses";
    }

    @GetMapping("/add")
    public String addCourse(Model model) {
        model.addAttribute("course", new CourseDTO());
        model.addAttribute("departments", deptRepository.findAll());
        model.addAttribute("teachers", teacherRepository.findAll());
        return "course-form";
    }

    @PostMapping
    public String storeCourse(@ModelAttribute("course") CourseDTO courseDTO) {
        courseService.createCourse(courseDTO);
        return "redirect:/courses";
    }

    @GetMapping("/edit/{id}")
    public String editCourse(@PathVariable("id") Long id, Model model) {
        Course course = courseService.getCourse(id);
        CourseDTO courseDTO = new CourseDTO();
        courseDTO.setId(course.getId());
        courseDTO.setCode(course.getCode());
        courseDTO.setTitle(course.getTitle());
        if (course.getDept() != null) {
            courseDTO.setDeptId(course.getDept().getId());
        }
        if (course.getTeacher() != null) {
            courseDTO.setTeacherId(course.getTeacher().getId());
        }
        model.addAttribute("course", courseDTO);
        model.addAttribute("departments", deptRepository.findAll());
        model.addAttribute("teachers", teacherRepository.findAll());
        model.addAttribute("editMode", true);
        return "course-form";
    }

    @PostMapping("/update/{id}")
    public String updateCourse(@PathVariable("id") Long id, @ModelAttribute("course") CourseDTO courseDTO) {
        courseService.updateCourse(id, courseDTO);
        return "redirect:/courses";
    }

    @PostMapping("/delete/{id}")
    public String deleteCourse(@PathVariable("id") Long id) {
        courseService.deleteCourse(id);
        return "redirect:/courses";
    }
}
