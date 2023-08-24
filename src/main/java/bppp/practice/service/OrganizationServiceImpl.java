package bppp.practice.service;

import bppp.practice.entity.OrganizationEntity;
import bppp.practice.repository.OrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class OrganizationServiceImpl implements OrganizationService {
    @Autowired
    OrganizationRepository organizationRepository;

    @Override
    public void saveOrganization(OrganizationEntity organization) {
        organizationRepository.save(organization);
    }

    @Override
    public OrganizationEntity getOrganization(int id) {
        OrganizationEntity organization = null;
        Optional<OrganizationEntity> optional = organizationRepository.findById(id);
        if (optional.isPresent()){
            organization = optional.get();
        }
        return organization;    }

    @Override
    public void deleteOrganization(int id) {
        organizationRepository.deleteById(id);
    }

    @Override
    public List<OrganizationEntity> getAllOrganizations() {
        return organizationRepository.findAll();
    }
}
