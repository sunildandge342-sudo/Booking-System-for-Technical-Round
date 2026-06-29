package com.team.booking_system.service;

import com.team.booking_system.dto.Request.ResourceRequest;
import com.team.booking_system.dto.Response.ResourceResponse;
import com.team.booking_system.entity.Resource;
import com.team.booking_system.exception.ResourceNotFoundException;
import com.team.booking_system.repository.ResourceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ResourceService {

    private final ResourceRepository resourceRepository;

    public Page<ResourceResponse> listAll(Pageable pageable) {
        return resourceRepository.findAll(pageable)
                .map(ResourceResponse::from);
    }

    public ResourceResponse findById(Long id) {
        return ResourceResponse.from(getOrThrow(id));
    }

    @Transactional
    public ResourceResponse create(ResourceRequest req) {
        Resource resource = Resource.builder()
                .name(req.getName())
                .type(req.getType())
                .description(req.getDescription())
                .capacity(req.getCapacity())
                .active(req.isActive())
                .build();
        return ResourceResponse.from(resourceRepository.save(resource));
    }

    @Transactional
    public ResourceResponse update(Long id, ResourceRequest req) {
        Resource resource = getOrThrow(id);
        resource.setName(req.getName());
        resource.setType(req.getType());
        resource.setDescription(req.getDescription());
        resource.setCapacity(req.getCapacity());
        resource.setActive(req.isActive());
        return ResourceResponse.from(resourceRepository.save(resource));
    }

    @Transactional
    public void delete(Long id) {
        Resource resource = getOrThrow(id);
        resourceRepository.delete(resource);
    }

    private Resource getOrThrow(Long id) {
        return resourceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found with id: " + id));
    }
}