package gr.aueb.cf.schoolapp.controller;

import gr.aueb.cf.schoolapp.dto.RegionReadOnlyDTO;
import gr.aueb.cf.schoolapp.dto.TeacherInsertDTO;
import gr.aueb.cf.schoolapp.model.Teacher;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/teachers")
public class TeacherController {

    @GetMapping("/insert")
    public String getTeacherForm(Model model) {
        model.addAttribute("teacherInsertDTO", TeacherInsertDTO.empty());
        // model.addAttribute("regionsReadOnlyDTO", regions());
        return "teacher-insert";
    }


    @ModelAttribute("regionsReadOnlyDTO")       // Εκτελείται πριν από κάθε request handler
    public List<RegionReadOnlyDTO> regions() {
        return List.of(
                new RegionReadOnlyDTO(1L, "Αθήνα"),
                new RegionReadOnlyDTO(2L, "Βόλος"),
                new RegionReadOnlyDTO(3L, "Θεσσαλονίκη"));
    }
}
