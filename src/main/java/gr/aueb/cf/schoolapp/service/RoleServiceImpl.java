package gr.aueb.cf.schoolapp.service;

import gr.aueb.cf.schoolapp.dto.RoleReadOnlyDTO;
import gr.aueb.cf.schoolapp.mapper.Mapper;
import gr.aueb.cf.schoolapp.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class RoleServiceImpl implements IRoleService {
    private final RoleRepository roleRepository;
    private final Mapper mapper;

    @Override
    public List<RoleReadOnlyDTO> findAllRolesSortedByName() {
        return roleRepository.findAllByOrderByNameAsc()
                .stream()
                .map(mapper::mapToRoleReadOnlyDTO)
                .toList();
    }
}
