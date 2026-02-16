package gr.aueb.cf.schoolapp.service;

import gr.aueb.cf.schoolapp.core.exceptions.EntityAlreadyExistsException;
import gr.aueb.cf.schoolapp.core.exceptions.EntityInvalidArgumentException;
import gr.aueb.cf.schoolapp.core.exceptions.EntityNotFoundException;
import gr.aueb.cf.schoolapp.dto.TeacherEditDTO;
import gr.aueb.cf.schoolapp.dto.TeacherInsertDTO;
import gr.aueb.cf.schoolapp.dto.TeacherReadOnlyDTO;
import gr.aueb.cf.schoolapp.mapper.Mapper;
import gr.aueb.cf.schoolapp.model.Teacher;
import gr.aueb.cf.schoolapp.model.static_data.Region;
import gr.aueb.cf.schoolapp.repository.RegionRepository;
import gr.aueb.cf.schoolapp.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.UUID;

@Service                        // IoC Container
@RequiredArgsConstructor        // DI
@Slf4j                          // Logger
public class TeacherService implements ITeacherService {

    private final TeacherRepository teacherRepository;
    private final RegionRepository regionRepository;
    private final Mapper mapper;

//    @Autowired
//    public TeacherService(TeacherRepository teacherRepository, RegionRepository regionRepository) {
//        this.teacherRepository = teacherRepository;
//        this.regionRepository = regionRepository;
//    }

    @Override
    @PreAuthorize("hasAuthority('INSERT_TEACHER')")
    @Transactional(rollbackFor = { EntityAlreadyExistsException.class, EntityInvalidArgumentException.class} )
    public TeacherReadOnlyDTO saveTeacher(TeacherInsertDTO dto)
            throws EntityAlreadyExistsException, EntityInvalidArgumentException {

        try {
            if (dto.vat() != null && teacherRepository.findByVat(dto.vat()).isPresent()) {
                throw new EntityAlreadyExistsException("Teacher with vat=" + dto.vat() + " already exists");
            }

            Region region = regionRepository.findById(dto.regionId())
                    .orElseThrow(() -> new EntityInvalidArgumentException("Region id=" + dto.regionId() + " invalid"));

            Teacher teacher = mapper.mapToTeacherEntity(dto);
            region.addTeacher(teacher);
            teacherRepository.save(teacher);        // saved teacher
            log.info("Teacher with vat={} saved successfully", dto.vat());
            return mapper.mapToTeacherReadOnlyDTO(teacher);
        } catch (EntityAlreadyExistsException e) {
            log.error("Save failed for teacher with vat={}. Teacher already exists", dto.vat(), e);     // Structured Logging
            throw e;
        } catch (EntityInvalidArgumentException e) {
            log.error("Save failed for teacher with vat={}. Region id={} invalid", dto.vat(), dto.regionId());
            throw e;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isTeacherExists(String vat) {
        return teacherRepository.findByVat(vat).isPresent();
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional(readOnly = true)
    public Page<TeacherReadOnlyDTO> getPaginatedTeachers(Pageable pageable) {
        Page<Teacher> teachersPage = teacherRepository.findAll(pageable);
        log.debug("Get paginated returned successfully page={} and size={}", teachersPage.getNumber(), teachersPage.getSize());
        return teachersPage.map(mapper::mapToTeacherReadOnlyDTO);
    }

    @Override
    @PreAuthorize("hasAuthority('VIEW_TEACHERS')")
    @Transactional(readOnly = true)
    public Page<TeacherReadOnlyDTO> getPaginatedTeachersDeletedFalse(Pageable pageable) {
        Page<Teacher> teachersPage = teacherRepository.findAllByDeletedFalse(pageable);
        log.debug("Get paginated not deleted returned successfully page={} and size={}", teachersPage.getNumber(), teachersPage.getSize());
        return teachersPage.map(mapper::mapToTeacherReadOnlyDTO);
    }

    @Override
    @PreAuthorize("hasAuthority('EDIT_TEACHER')")
    @Transactional(rollbackFor = { EntityNotFoundException.class, EntityAlreadyExistsException.class, EntityInvalidArgumentException.class} )
    public TeacherReadOnlyDTO updateTeacher(TeacherEditDTO dto)
            throws EntityNotFoundException, EntityAlreadyExistsException, EntityInvalidArgumentException {
        try {
            Teacher teacher = teacherRepository.findByUuid(dto.uuid())
                    .orElseThrow(() -> new EntityNotFoundException("Teacher with uuid=" + dto.uuid() + " not found"));

            teacher.setFirstname(dto.firstname());
            teacher.setLastname(dto.lastname());
            if (!teacher.getVat().equals(dto.vat())) {
                if (teacherRepository.findByVat(dto.vat()).isPresent()) {
                    throw new EntityAlreadyExistsException("Teacher with vat=" + dto.vat() + " already exists");
                }
                teacher.setVat(dto.vat());
            }

            if (!Objects.equals(dto.regionId(), teacher.getRegion().getId())) {
                Region region = regionRepository.findById(dto.regionId())
                        .orElseThrow(() -> new EntityInvalidArgumentException("Region id=" + dto.regionId() + " invalid"));
                Region oldRegion = teacher.getRegion();
                if (oldRegion != null) {
                    oldRegion.removeTeacher(teacher);
                }
                region.addTeacher(teacher);
            }

            teacherRepository.save(teacher);    // προαιρετικό
            log.info("Teacher with uuid={} updated successfully", dto.uuid());
            return mapper.mapToTeacherReadOnlyDTO(teacher);
        } catch (EntityNotFoundException e) {
            log.error("Update failed for teacher with uuid={}. Teacher not found", dto.uuid(), e);
            throw e;
        } catch (EntityAlreadyExistsException e) {
            log.error("Update failed for teacher with uuid={}. Teacher with vat={} already exists", dto.uuid(), dto.vat(), e);
            throw e;
        } catch (EntityInvalidArgumentException e) {
            log.error("Update failed for teacher with uuid={}. Region id={} invalid", dto.uuid(), dto.regionId(), e);
            throw e;
        }
    }

    @Override
    @PreAuthorize("hasAuthority('DELETE_TEACHER')")
    @Transactional(rollbackFor = EntityNotFoundException.class)
    public TeacherReadOnlyDTO deleteTeacherByUUID(UUID uuid) throws EntityNotFoundException {
        try {
            Teacher teacher = teacherRepository.findByUuidAndDeletedFalse(uuid)
                    .orElseThrow(() -> new EntityNotFoundException("Teacher with uuid=" + uuid + " not found"));

            teacher.softDelete();
            // No save needed if Teacher is managed
//            teacherRepository.save(teacher);
            log.info("Teacher with uuid={} deleted successfully", uuid);
            return mapper.mapToTeacherReadOnlyDTO(teacher);
        } catch (EntityNotFoundException e) {
            log.error("Update failed for teacher with uuid={}. Teacher not found", uuid, e);

            // Automatic rollback due to @Transactional annotation
            throw e;
        }
    }

    @Override
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Transactional(readOnly = true)
    public TeacherEditDTO getTeacherByUUID(UUID uuid) throws EntityNotFoundException {

        try {
            Teacher teacher = teacherRepository.findByUuid(uuid)
                    .orElseThrow(() -> new EntityNotFoundException("Teacher with uuid=" + uuid + " not found"));
            log.debug("Get teacher by uuid={} returned successfully", uuid);
            return mapper.mapToTeacherEditDTO(teacher);
        } catch (EntityNotFoundException e) {
            log.error("Get teacher by uuid={} failed", uuid, e);
            throw e;
        }
    }

    @Override
    @PreAuthorize("hasAuthority('EDIT_TEACHER')")
    @Transactional(readOnly = true)
    public TeacherEditDTO getTeacherByUUIDDeletedFalse(UUID uuid) throws EntityNotFoundException {

        try {
            Teacher teacher = teacherRepository.findByUuidAndDeletedFalse(uuid)
                    .orElseThrow(() -> new EntityNotFoundException("Teacher with uuid=" + uuid + " not found"));
            log.debug("Get non-deleted teacher by uuid={} returned successfully", uuid);
            return mapper.mapToTeacherEditDTO(teacher);
        } catch (EntityNotFoundException e) {
            log.error("Get teacher by uuid={} failed", uuid, e);
            throw e;
        }
    }
}
