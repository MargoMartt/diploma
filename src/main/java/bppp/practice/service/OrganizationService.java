package bppp.practice.service;

import bppp.practice.entity.OrganizationEntity;

import java.util.List;

public interface OrganizationService {
    public void saveOrganization(OrganizationEntity organization);

    public OrganizationEntity getOrganization(int id);

    public void deleteOrganization(int id);

    public List<OrganizationEntity> getAllOrganizations();
}
