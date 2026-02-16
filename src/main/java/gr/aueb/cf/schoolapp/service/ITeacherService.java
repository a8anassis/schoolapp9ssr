package gr.aueb.cf.schoolapp.service;

import gr.aueb.cf.schoolapp.core.exceptions.EntityAlreadyExistsException;
import gr.aueb.cf.schoolapp.core.exceptions.EntityInvalidArgumentException;
import gr.aueb.cf.schoolapp.core.exceptions.EntityNotFoundException;
import gr.aueb.cf.schoolapp.dto.TeacherEditDTO;
import gr.aueb.cf.schoolapp.dto.TeacherInsertDTO;
import gr.aueb.cf.schoolapp.dto.TeacherReadOnlyDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ITeacherService {
    TeacherReadOnlyDTO saveTeacher(TeacherInsertDTO teacherInsertDTO)
        throws EntityAlreadyExistsException, EntityInvalidArgumentException;

    TeacherReadOnlyDTO updateTeacher(TeacherEditDTO teacherEditDTO)
            throws EntityNotFoundException, EntityAlreadyExistsException, EntityInvalidArgumentException;

    TeacherReadOnlyDTO deleteTeacherByUUID(UUID uuid) throws EntityNotFoundException;

    TeacherEditDTO getTeacherByUUID(UUID uuid) throws EntityNotFoundException;

    Page<TeacherReadOnlyDTO> getPaginatedTeachers(Pageable pageable);
    Page<TeacherReadOnlyDTO> getPaginatedTeachersDeletedFalse(Pageable pageable);

    boolean isTeacherExists(String uuid);
}
