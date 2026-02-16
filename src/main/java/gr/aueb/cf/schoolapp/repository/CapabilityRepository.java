package gr.aueb.cf.schoolapp.repository;

import gr.aueb.cf.schoolapp.model.Capability;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CapabilityRepository extends JpaRepository<Capability, Long> {
}
