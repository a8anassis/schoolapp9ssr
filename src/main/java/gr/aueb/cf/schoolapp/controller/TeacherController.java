package gr.aueb.cf.schoolapp.controller;

import gr.aueb.cf.schoolapp.core.exceptions.EntityAlreadyExistsException;
import gr.aueb.cf.schoolapp.core.exceptions.EntityInvalidArgumentException;
import gr.aueb.cf.schoolapp.core.exceptions.EntityNotFoundException;
import gr.aueb.cf.schoolapp.dto.RegionReadOnlyDTO;
import gr.aueb.cf.schoolapp.dto.TeacherEditDTO;
import gr.aueb.cf.schoolapp.dto.TeacherInsertDTO;
import gr.aueb.cf.schoolapp.dto.TeacherReadOnlyDTO;
import gr.aueb.cf.schoolapp.service.IRegionService;
import gr.aueb.cf.schoolapp.service.ITeacherService;
import gr.aueb.cf.schoolapp.validator.TeacherEditValidator;
import gr.aueb.cf.schoolapp.validator.TeacherInsertValidator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@Controller
@RequiredArgsConstructor
@RequestMapping("/teachers")
public class TeacherController {

    private final ITeacherService teacherService;
    private final IRegionService regionService;
    private final TeacherInsertValidator teacherInsertValidator;
    private final TeacherEditValidator teacherEditValidator;

//    @Autowired
//    public TeacherController(ITeacherService teacherService) {
//        this.teacherService = teacherService;
//    }

    @GetMapping("/insert")
    public String getTeacherForm(Model model) {
        model.addAttribute("teacherInsertDTO", TeacherInsertDTO.empty());
        // model.addAttribute("regionsReadOnlyDTO", regions());
        return "teacher-insert";
    }

    @PostMapping("/insert")
    public String teacherInsert(@Valid @ModelAttribute("teacherInsertDTO") TeacherInsertDTO teacherInsertDTO,
                                BindingResult bindingResult, Model model,
                                RedirectAttributes redirectAttributes) {

        teacherInsertValidator.validate(teacherInsertDTO, bindingResult);

        if (bindingResult.hasErrors()) {
            // model.addAttribute("regionsReadOnlyDTO", regions());
            return "teacher-insert";
        }

        try {
            // save teacher to DB
//        TeacherReadOnlyDTO teacherReadOnlyDTO = new TeacherReadOnlyDTO("acfd-1234", "Αθανάσιος", "Ανδρούτσος");
            //model.addAttribute("teacherReadOnlyDTO", teacherReadOnlyDTO);
            TeacherReadOnlyDTO teacherReadOnlyDTO = teacherService.saveTeacher(teacherInsertDTO);

            // PRG - Post-Redirect-Get
            redirectAttributes.addFlashAttribute("teacherReadOnlyDTO", teacherReadOnlyDTO);
            return "redirect:/teachers/success";
        } catch (EntityAlreadyExistsException | EntityInvalidArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "teacher-insert";
        }
    }


    @GetMapping({ "", "/" })
    public String getPaginatedTeachers(@PageableDefault(page = 0, size = 5, sort = "lastname") Pageable pageable,
                                       Model model) {

        Page<TeacherReadOnlyDTO> teachersPage = teacherService.getPaginatedTeachers(pageable);
//        Page<TeacherReadOnlyDTO> teachersPage = new PageImpl<>(Stream.of(
//                        new TeacherReadOnlyDTO("ab123", "Pavlos", "Pavlopoulos", "1234", "Athens"),
//                        new TeacherReadOnlyDTO("ab124", "Nikos", "Charos", "1234", "Athens"),
//                        new TeacherReadOnlyDTO("ab125", "Kostas", "Lazaris", "1234", "Athens"),
//                        new TeacherReadOnlyDTO("ab126", "George", "petrou", "1234", "Athens"),
//                        new TeacherReadOnlyDTO("ab127", "Lydia", "Spiropoulou", "1234", "Athens"))
//                .sorted(Comparator.comparing(TeacherReadOnlyDTO::lastname))
//                .skip(pageable.getOffset())
//                .limit(pageable.getPageSize())
//                .toList(), pageable, 5
//        );
        model.addAttribute("teachers", teachersPage.getContent());
        model.addAttribute("page", teachersPage);
        return "teachers";
    }

    @GetMapping("/edit/{uuid}")         // path variable
    public String getTeacherEdit(@PathVariable UUID uuid, Model model) {
        try {
            TeacherEditDTO teacherEditDTO = teacherService.getTeacherByUUID(uuid);
            model.addAttribute("teacherEditDTO", teacherEditDTO);
        } catch (EntityNotFoundException e) {
            model.addAttribute("errorMessage", e.getMessage());
        }
        return "teacher-edit";
    }

    @PostMapping("/edit")
    public String updateTeacher(@Valid @ModelAttribute("teacherEditDTO") TeacherEditDTO teacherEditDTO,
                              BindingResult bindingResult, RedirectAttributes redirectAttributes,  Model model) {

        teacherEditValidator.validate(teacherEditDTO, bindingResult);
        if (bindingResult.hasErrors()) {
            return "teacher-edit";
        }

        try {
            TeacherReadOnlyDTO readOnlyDTO = teacherService.updateTeacher(teacherEditDTO);
            redirectAttributes.addFlashAttribute("teacherReadOnlyDTO", readOnlyDTO);
            return "redirect:/teachers/update-success";
        } catch (EntityNotFoundException | EntityAlreadyExistsException | EntityInvalidArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "teacher-edit";
        }
    }

    @GetMapping("/update-success")
    public String updateSuccess() {
        return "update-teacher-success";
    }


    @GetMapping("/success")
    public String teacherSuccess(Model model) {
        return "teacher-success";
    }


    @ModelAttribute("regionsReadOnlyDTO")       // Εκτελείται πριν από κάθε request handler
    public List<RegionReadOnlyDTO> regions() {
        return regionService.findAllRegionsSortedByName();

//        return List.of(
//                new RegionReadOnlyDTO(1L, "Αθήνα"),
//                new RegionReadOnlyDTO(2L, "Βόλος"),
//                new RegionReadOnlyDTO(3L, "Θεσσαλονίκη"));
    }
}
