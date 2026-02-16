package gr.aueb.cf.schoolapp.repository;

import gr.aueb.cf.schoolapp.model.Teacher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

// @Repository, not needed if we extend JpaRepository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {

    Optional<Teacher> findByVat(String vat);
    Optional<Teacher> findByUuid(UUID uuid);

    @EntityGraph(attributePaths = {"region"})
    Page<Teacher> findAllByDeletedFalse(Pageable pageable);

    Optional<Teacher> findByVatAndDeletedFalse(String vat);
    Optional<Teacher> findByUuidAndDeletedFalse(UUID uuid);

}
